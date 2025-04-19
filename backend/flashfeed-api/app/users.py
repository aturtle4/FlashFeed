from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from . import models, schemas, utils

router = APIRouter()
get_db = utils.get_db
get_current_user = utils.get_current_user

@router.post("/news/save")
def save_news(news: schemas.NewsSave, db: Session = Depends(get_db), user: models.User = Depends(get_current_user)):
    saved = models.SavedNews(**news.dict(), user_id=user.id)
    db.add(saved)
    db.commit()
    db.refresh(saved)
    return {"message": "News saved successfully", "id": saved.id}

@router.delete("/news/delete/{news_id}")
def delete_saved_news(news_id: int, db: Session = Depends(get_db), user: models.User = Depends(get_current_user)):
    article = db.query(models.SavedNews).filter(models.SavedNews.id == news_id, models.SavedNews.user_id == user.id).first()
    if not article:
        raise HTTPException(status_code=404, detail="Article not found")
    db.delete(article)
    db.commit()
    return {"message": "News deleted"}

@router.get("/news/saved", response_model=list[schemas.NewsResponse])
def get_saved_news(db: Session = Depends(get_db), user: models.User = Depends(get_current_user)):
    return db.query(models.SavedNews).filter(models.SavedNews.user_id == user.id).all()
