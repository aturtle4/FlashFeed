import datetime
import uuid
import requests
import pytz


headers = {
    'authority': 'inshorts.com',
    'accept': '*/*',
    'accept-language': 'en-GB,en;q=0.5',
    'content-type': 'application/json',
    'referer': 'https://inshorts.com/en/read',
    'sec-ch-ua': '"Not/A)Brand";v="99", "Brave";v="115", "Chromium";v="115"',
    'sec-ch-ua-mobile': '?0',
    'sec-ch-ua-platform': '"macOS"',
    'sec-fetch-dest': 'empty',
    'sec-fetch-mode': 'cors',
    'sec-fetch-site': 'same-origin',
    'sec-gpc': '1',
    'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36',
}


def getNews(category, count):
    url = f'https://inshorts.com/api/en/news?category=all_news&max_limit=1000000000000000000&include_card_data=true'
    response = requests.get(url, headers=headers)

    try:
        news_data = response.json()['data']['news_list']
    except Exception as e:
        print(response.text)
        return {
            'success': False,
            'error': 'Failed to fetch data from Inshorts'
        }

    filtered_news = []
    for entry in news_data:
            news = entry['news_obj']
            # print(news)
            # if category is trending, then skip category check
            if category == 'trending':
                pass
            elif (category.lower() not in news['category_names']):
                continue  

            author = news['author_name']
            title = news['title']
            imageUrl = news['image_url']
            url = news['shortened_url']
            content = news['content']
            timestamp = news['created_at'] / 1000
            dt_utc = datetime.datetime.utcfromtimestamp(timestamp)
            tz_utc = pytz.timezone('UTC')
            dt_utc = tz_utc.localize(dt_utc)
            tz_ist = pytz.timezone('Asia/Kolkata')
            dt_ist = dt_utc.astimezone(tz_ist)
            date = dt_ist.strftime('%A, %d %B, %Y')
            time = dt_ist.strftime('%I:%M %p').lower()
            readMoreUrl = news['source_url']

            newsObject = {
                'id': uuid.uuid4().hex,
                'title': title,
                'imageUrl': imageUrl,
                'url': url,
                'content': content,
                'author': author,
                'date': date,
                'time': time,
                'readMoreUrl': readMoreUrl,
                'category': news['category_names'],
            }
            filtered_news.append(newsObject)
    return {
        'success': True,
        'category': category,
        'data': filtered_news
    }
