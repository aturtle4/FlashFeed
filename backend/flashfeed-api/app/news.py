from fastapi import APIRouter, Query, Depends
import requests
from .utils import translate_text
from . import models
from .utils import get_current_user
from .data import NewsService  # Import the NewsService class from new_test.py

router = APIRouter()

@router.get("/news/fetch")
def fetch_news(category: str, initial: str, count: int = Query(..., le=25), language: str = "english"):
    # Initialize the NewsService
    news_service = NewsService()
    
    # Determine which method to call based on category
    offset = 0 if initial == "true" else count
    limit = count
    
    if category.lower() == "all":
        news_response = news_service.get_all_news(offset, limit)
    elif category.lower() == "top":
        news_response = news_service.get_top_news(offset, limit)
    elif category.lower() == "trending":
        news_response = news_service.get_trending_news(offset, limit)
    else:
        # For specific categories, use get_topic_news
        news_response = news_service.get_topic_news(category.lower(), offset, limit)
    
    if not news_response or "articles" not in news_response:
        return {"error": "Failed to fetch news", "details": "Unable to retrieve articles"}

    # Convert unix to datetime
    from datetime import datetime
    for article in news_response["articles"]:
        if "published_at" in article:
            article["published_at"] = datetime.utcfromtimestamp(article["published_at"] // 1000).strftime('%Y-%m-%d %H:%M:%S')

    # Format the results
    result = []
    for i, article in enumerate(news_response["articles"], 1):
        news_item = {
            "id": i,
            "title": article["title"],
            "content": article["content"],
            "articleLink": article["url"],
            "imageUrl": article["image_url"],
            "source": article["author"] or article["source_name"],
            "timestamp": article["published_at"],  # This is a Unix timestamp, may need formatting
            "category": category,
        }
        
        # Translate if needed
        if language.lower() != "english":
            news_item["title"] = translate_text(article["title"], language)
            news_item["content"] = translate_text(article["content"], language)
            news_item["language"] = language
            
        result.append(news_item)
    
    return result

# Add a new endpoint to get available categories via trending topics
@router.get("/news/categories")
def get_categories():
    news_service = NewsService()
    trending_topics = news_service.get_trending_topics()
    
    categories = [
        topic["topic"].lower() 
        for topic in trending_topics.get("topics", [])
        if topic["type"] == "NEWS_CATEGORY"
    ]
    
    # Add standard categories
    standard_categories = ["all", "top", "trending"]
    categories = standard_categories + sorted(list(set(categories)))
    
    return {
        "success": True,
        "categories": categories
    }

# Add a new search endpoint
@router.get("/news/search")
def search_news(query: str, count: int = Query(..., le=25), language: str = "english"):
    news_service = NewsService()
    search_response = news_service.get_searched_news(query, 0, count)
    
    if not search_response or "articles" not in search_response:
        return {"error": "No results found", "details": "No articles matching your search query"}
    
    # Format the results
    result = []
    for i, article in enumerate(search_response["articles"], 1):
        news_item = {
            "id": i,
            "title": article["title"],
            "content": article["content"],
            "articleLink": article["url"],
            "imageUrl": article["image_url"],
            "source": article["author"] or article["source_name"],
            "timestamp": article["published_at"],
            "category": article["category"][0] if article["category"] else "general",
        }
        
        # Translate if needed
        if language.lower() != "english":
            news_item["title"] = translate_text(article["title"], language)
            news_item["content"] = translate_text(article["content"], language)
            news_item["language"] = language
            
        result.append(news_item)
    
    return result