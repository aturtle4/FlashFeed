from fastapi import APIRouter, Query, Depends
import requests
from .utils import translate_text
from . import models
from .utils import get_current_user

router = APIRouter()

from .inshorts import getNews  # Import the getNews function from inshorts.py

@router.get("/news/fetch")
def fetch_news(category: str, count: int = Query(..., le=25),  user: models.User = Depends(get_current_user), language: str = "english"):
    # Step 1: Fetch news using our custom inshorts module
    news_response = getNews(category.lower())
    
    if not news_response.get('success') or isinstance(news_response.get('success'), str):
        return {"error": "Failed to fetch news", "details": news_response.get('error', 'Unknown error')}

    all_news = news_response.get("data", [])
    selected = all_news[:count]

    # Step 2: Translate if needed
    result = []
    for article in selected:
        news_item = {
            "title": article["title"],
            "content": article["content"],
            "url": article["readMoreUrl"],
            "imageUrl": article["imageUrl"],
            "author": article["author"],
            "date": article["date"],
            "time": article["time"],
            "category": category,
            "language": "english"
        }
        
        # Translate if needed
        if language.lower() != "english":
            news_item["title"] = translate_text(article["title"], language)
            news_item["content"] = translate_text(article["content"], language)
            news_item["language"] = language
            
        result.append(news_item)
    
    return result