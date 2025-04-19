from fastapi import FastAPI
from .database import Base, engine
from .auth import router as auth_router
from .users import router as user_router
from .news import router as news_router

Base.metadata.create_all(bind=engine)

app = FastAPI()
app.include_router(auth_router)
app.include_router(user_router)
app.include_router(news_router)