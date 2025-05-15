from fastapi import APIRouter, Query, Depends
import requests
from .data import NewsService  # Import the NewsService class from new_test.py

router = APIRouter()

@router.get("/news/fetch")
def fetch_news(category: str, initial: str, count: int = Query(..., le=25), language: str = "english"):
    # Initialize the NewsService
    news_service = NewsService()
    
    # Determine which method to call based on category
    offset = 0 if initial == "true" else count
    limit = count

    if language.lower() == "english":
        language = "en"
    elif language.lower() == "hindi":
        language = "hi"
    elif language.lower() == "bengali":
        language = "bn"
    elif language.lower() == "urdu":
        language = "ur"

    if category.lower() == "all":
        news_response = news_service.get_all_news(offset, limit)
    elif category.lower() == "top":
        news_response = news_service.get_top_news(offset, limit)
    elif category.lower() == "trending":
        news_response = news_service.get_trending_news(offset, limit, language)
    else:
        # For specific categories, use get_topic_news
        news_response = news_service.get_topic_news(category.lower(), offset, limit, language)
    
    if not news_response or "articles" not in news_response:
        return {"error": "Failed to fetch news", "details": "Unable to retrieve articles"}

    # Convert unix to datetime and the published_at field to something like 2 hours ago
    from datetime import datetime
    for article in news_response["articles"]:
        if "published_at" in article:
            published_at = datetime.utcfromtimestamp(article["published_at"] // 1000).strftime('%Y-%m-%d %H:%M:%S')
            # Convert to a human-readable format like "2 hours ago"
            article["published_at"] = f"{(datetime.now() - datetime.strptime(published_at, '%Y-%m-%d %H:%M:%S')).seconds // 3600} hours ago"

    # Format the results
    result = []
    for i, article in enumerate(news_response["articles"], 1):
        news_item = {
            "id": article["hash_id"],
            "title": article["title"],
            "content": article["content"],
            "articleLink": article["url"],
            "imageUrl": article["image_url"],
            "source": article["author"] or article["source_name"],
            "timestamp": article["published_at"],  # This is a Unix timestamp, may need formatting
            "category": category,
        }
        
        # Translate if needed
        # if language.lower() != "english":
        #     news_item = translate_text(news_item, language)
            
        result.append(news_item)
    
    return result

# Helper function to translate
def translate_text(article: dict, language: str) -> dict:
    # Translate using api call on localhost
    # Api call is http://localhost:5000/translate

    # Change to language code for the entire string
    # For example, if language is 'hindi', change to 'hi'
    if language.lower() == 'hindi':
        language = 'hi'
    elif language.lower() == 'bengali':
        language = 'bn'
    elif language.lower() == 'urdu':
        language = 'ur'
    else:
        language = 'en'


    # Setting up the headers and data for the request
    headers = {
        'Content-Type': 'application/json'
    }

    # Json Payload for my request
    payloed_content = {
        'source': 'en',
        'q': article['content'],
        'target': language
    }

    payloed_title = {
        'source': 'en',
        'q': article['title'],
        'target': language
    }
    response_content = requests.post('http://localhost:5000/translate', headers=headers, json=payloed_content)
    response_title = requests.post('http://localhost:5000/translate', headers=headers, json=payloed_title)
    if response_content.status_code == 200 and response_title.status_code == 200:
        article['title'] = response_title.json().get('translatedText', article['title'])
        article['content'] = response_content.json().get('translatedText', article['content'])
    else:
        print(f"Error: {response_content.status_code}, {response_content.text}")
        article['title'] = article['title']
        article['content'] = article['content']

    return article
