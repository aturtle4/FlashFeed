import requests

class NewsService:
    def __init__(self):
        self.read_api_base_url = "https://read-api.newsinshorts.com"
        self.search_api_base_url = "https://news-search.newsinshorts.com"
        self.headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Content-Type": "application/json",
            "x-device-id": "1234567890abcdef",
        }
    
    def create_news_payload(self, news_objs):
        """Convert raw news objects to formatted news payload"""
        articles = []
        for news in news_objs:
            article = {
                "author": news.get("author_name", ""),
                "title": news.get("title", ""),
                "content": news.get("content", ""),
                "url": news.get("source_url", ""),
                "image_url": news.get("image_url", ""),
                "published_at": news.get("created_at", 0),
                "category": news.get("category_names", []),
                "source_name": news.get("source_name", "Inshorts")
            }
            articles.append(article)
        return articles
    
    def get_all_news(self, offset, limit):
        max_page_limit = (offset + limit + 189) // 190  # Equivalent to ceil((offset + limit) / 190)
        
        # Get initial news list
        response = requests.get(f"{self.read_api_base_url}/en/v4/news/top_refresh", headers=self.headers)
        news_list_meta = response.json()
        
        initial_offset_news_id = news_list_meta["min_news_id"]
        
        # Get paginated news
        paginated_news_items = self.get_paginated_news(max_page_limit, initial_offset_news_id)
        
        # Combine news lists
        news_list_meta["news_list"].extend(paginated_news_items)
        
        # Extract news IDs
        news_ids = [news["hash_id"] for news in news_list_meta["news_list"]
                  if news["type"] == "NEWS"][offset:offset+limit]
        
        # Get full news items
        payload = {"news_hash_ids": news_ids}
        response = requests.post(f"{self.read_api_base_url}/en/v2/hash_id/search", 
                               json=payload, headers=self.headers)
        all_news_items = response.json()
        
        # Sort by created_at (descending)
        all_news_items["news_objs"].sort(key=lambda x: x["created_at"], reverse=True)
        
        # Create response payload
        all_news_response = self.create_news_payload(all_news_items["news_objs"])
        
        return {"count": len(all_news_response), "articles": all_news_response}
    
    def get_top_news(self, offset, limit):
        max_page_limit = (offset + limit + 189) // 190  # Equivalent to ceil((offset + limit) / 190)
        
        # Get initial news list
        response = requests.get(f"{self.read_api_base_url}/en/v4/news/top_stories/top_refresh", 
                              headers=self.headers)
        news_list_meta = response.json()
        
        initial_offset_news_id = news_list_meta["min_news_id"]
        
        # Get paginated news
        paginated_news_items = self.get_paginated_news(max_page_limit, initial_offset_news_id)
        
        # Combine news lists
        news_list_meta["news_list"].extend(paginated_news_items)
        
        # Extract news IDs
        news_ids = [news["hash_id"] for news in news_list_meta["news_list"]
                  if news["type"] == "NEWS"][offset:offset+limit]
        
        # Get full news items
        payload = {"news_hash_ids": news_ids}
        response = requests.post(f"{self.read_api_base_url}/en/v2/hash_id/search", 
                               json=payload, headers=self.headers)
        all_news_items = response.json()
        
        # Sort by created_at (descending)
        all_news_items["news_objs"].sort(key=lambda x: x["created_at"], reverse=True)
        
        # Create response payload
        top_news_response = self.create_news_payload(all_news_items["news_objs"])
        
        return {"count": len(top_news_response), "articles": top_news_response}
    
    def get_trending_news(self, offset, limit):
        # Get trending news
        response = requests.get(f"{self.read_api_base_url}/en/v4/news/trending", headers=self.headers)
        news_list_meta = response.json()
        
        # Extract news IDs
        news_ids = [news["hash_id"] for news in news_list_meta["news_list"]
                  if news["type"] == "NEWS"][offset:offset+limit]
        
        # Get full news items
        payload = {"news_hash_ids": news_ids}
        response = requests.post(f"{self.read_api_base_url}/en/v2/hash_id/search", 
                               json=payload, headers=self.headers)
        all_news_items = response.json()
        
        # Sort by created_at (descending)
        all_news_items["news_objs"].sort(key=lambda x: x["created_at"], reverse=True)
        
        # Create response payload
        trending_news_response = self.create_news_payload(all_news_items["news_objs"])
        
        return {"count": len(trending_news_response), "articles": trending_news_response}

    def get_trending_topics(self):
        response = requests.get(f"{self.search_api_base_url}/en/v3/trending_topics", headers=self.headers)
        trending_topics = response.json()
        all_topics = []
        for topic in trending_topics["trending_tags"]:
            all_topics.append({
                "topic": topic["tag"],
                "label": topic["label"],
                "type": topic["type"],
                "imageUrl": topic["image_url"],
                "nightImageUrl": topic["night_image_url"],
                "relevanceTag": topic["relevance_tag"],
                "priority": topic["priority"]
            })
        
        return {"count": len(all_topics), "topics": all_topics}
    
    def get_topic_news(self, topic, offset, limit):
        max_page_limit = (offset + limit + 19) // 20  # Equivalent to ceil((offset + limit) / 20)
        
        topic_news_items = []
        
        for i in range(1, max_page_limit + 1):
            params = {
                "type": "NEWS_CATEGORY",
                "tag_id": topic,
                "max_limit": 20,
                "page": i
            }
            response = requests.get(f"{self.search_api_base_url}/en/v3/news_tag_search", 
                                  params=params, headers=self.headers)
            topic_news = response.json()
            
            topic_news_items.extend(topic_news["suggested_news"])
        
        # Extract and filter news items
        sanitized_news_items = [item["news_obj"] for item in topic_news_items 
                              if item["type"] == "NEWS"][offset:offset+limit]
        
        # Create response payload
        topic_news_response = self.create_news_payload(sanitized_news_items)
        
        return {"count": len(topic_news_response), "articles": topic_news_response}
    
    def get_searched_news(self, search_query, offset, limit):
        max_page_limit = (offset + limit + 19) // 20  # Equivalent to ceil((offset + limit) / 20)
        
        searched_news_items = []
        
        for i in range(1, max_page_limit + 1):
            params = {
                "query_str": search_query,
                "max_limit": 20,
                "page": i
            }
            response = requests.get(f"{self.search_api_base_url}/en/v3/news_search", 
                                  params=params, headers=self.headers)
            searched_news = response.json()
            
            searched_news_items.extend(searched_news["suggested_news"])
        
        # Extract and filter news items
        sanitized_news_items = [item["news_obj"] for item in searched_news_items 
                              if item["type"] == "NEWS"][offset:offset+limit]
        
        # Sort by created_at (descending)
        sanitized_news_items.sort(key=lambda x: x["created_at"], reverse=True)
        
        # Create response payload
        searched_news_response = self.create_news_payload(sanitized_news_items)
        
        return {"count": len(searched_news_response), "articles": searched_news_response}
    
    def get_paginated_news(self, max_page_limit, initial_offset_id=None):
        offset_news_id = initial_offset_id
        paginated_news_item = []
        
        for i in range(max_page_limit, 1, -1):
            params = {"news_offset": offset_news_id}
            response = requests.get(f"{self.read_api_base_url}/en/v4/news/load_more", 
                                  params=params, headers=self.headers)
            paginated_news = response.json()
            
            paginated_news_item.extend(paginated_news["news_list"])
            offset_news_id = paginated_news["min_news_id"]
        
        return paginated_news_item
