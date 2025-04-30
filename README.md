
# FlashFeed

FlashFeed is a modern news aggregator application built for Android that provides personalized news content in multiple languages. The app features a TikTok-style vertical scrolling feed for news consumption, customizable categories, and user profiles.

## Features

### Core Features

- Personalized News Feed: User-tailored news based on selected categories
- Vertical News Reels: TikTok-style vertical scrolling news articles
- Multi-language Support: Content available in English, Hindi, Bengali, and Urdu
- Profile Management: Customizable user profiles with profile pictures
- Category Selection: Up to 5 customizable news categories
- Bookmarking: Save articles for later reading
- Article Sharing: Easy sharing of news to other apps
- Web View: In-app browser for reading full articles
- Text-to-Speech: Automated reading of news content

### Technical Features
- Adaptive Loading: Adjusts content loading based on battery level and network type
- Local Database: Persistent storage for user preferences and saved articles
- Splash Screen: Animated app intro screen
- Dark/Light Mode: Supports system theme settings
- Responsive UI: Material Design 3 components
## API Reference

#### Get all items

```http
  GET /news/fetch
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `category`| `string` | **Required**. Auth Token   |
| `count`   | `int`    | **Required**. Auth Token   |
| `initial` | `string` | **Required**. Auth Token   |
| `language`| `string` | **Required**. Auth Token   |





## Documentation

## Technical Documentation

### Architecture

The app follows MVVM (Model-View-ViewModel) architecture:  

- Model: Data classes and Room database entities
- View: Jetpack Compose UI components
- ViewModel: Handles business logic and UI state

### Database Structure

FlashFeed uses Room database with the following entities:  
- UserPreferencesEntity: Stores user info and language preferences
- CategoryEntity: Tracks selected news categories
- SavedReelEntity: Stores bookmarked articles

### Key Components
- MainActivity: Main entry point after splash screen
- SplashActivity: Initial loading screen
- NewsReelScreen: Core news display component
- UserPreferencesViewModel: Manages user settings
- CategoryViewModel: Handles category preferences
- NewsReelViewModel: Manages news content and interactions

## FAQ

#### How do I change my news preferences?

Go to your Profile screen and tap the Settings icon in the top right corner. There you can modify your selected categories (up to 5), change language, and update your profile.

#### Why do I sometimes see fewer new articles?

FlashFeed adapts content loading based on your battery level and network connection. When your battery is below 30% or you're on cellular data, the app loads fewer articles to conserve resources.

#### How can I save an article for later?

While viewing an article in the news feed, tap the bookmark icon on the right side of the screen to save it. Saved articles can be found in your Profile screen.

#### Can I read the full article?

Yes, long-press on any news card to open the full article in the in-app browser. 

#### How do I change the app language?

Go to your Profile, tap Settings, and select Language. Choose from available options: English, Hindi, Bengali, or Urdu.


## Authors

- [@Aditya Jagadale](https://github.com/jaagss)
- [@Armaan Singh](https://github.com/aturtle4)
- [@Harsh Mistry](https://github.com/FakePickle)
- [@Hemanth Dindigallu](https://github.com/hemanthdindigallu)