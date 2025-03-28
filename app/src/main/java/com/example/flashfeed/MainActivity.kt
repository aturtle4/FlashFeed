package com.example.flashfeed

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.flashfeed.ui.theme.FlashFeedTheme
import com.example.flashfeed.reel_mechanism.NewsArticle
import com.example.flashfeed.reel_mechanism.NewsReelScreen
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            FlashFeedTheme {
                NewsApp()
            }
        }
    }
}


@Composable
fun NewsApp() {
    val sampleNews = listOf(
        NewsArticle(
            id = 1,
            title = "Breaking News: AI Revolution",
            shortDescription = "Artificial Intelligence is reshaping industries at an unprecedented pace. From healthcare to finance, AI is bringing efficiency, automation, and intelligent decision-making, reducing human effort while increasing accuracy and speed.",
            mediumDescription = "Artificial Intelligence (AI) is no longer a concept of the future; it is the driving force behind some of the most transformative changes in the modern world. In the healthcare sector, AI is being used for early disease detection, robotic surgeries, and predictive analytics that help doctors provide better treatment. Finance is also witnessing a major shift, with AI-powered chatbots, fraud detection systems, and algorithmic trading revolutionizing traditional banking and investment. \n\n AI’s impact extends to journalism, where AI-powered tools generate news summaries and automated reports in real-time. However, concerns about data privacy, security, and ethical AI development continue to spark debates among experts. Governments and tech giants are working together to establish guidelines to ensure AI is used responsibly.",
            imageUrl = "https://img.freepik.com/premium-photo/3d-rendering-robot-artificial-intelligence-black-background-futuristic-technology-robot_844516-420.jpg",
            source = "Tech Times",
            timestamp = "2h ago",
            articleLink = "https://www.techtimes.com"
        ),
        NewsArticle(
            id = 2,
            title = "शेयर बाजार में भारी गिरावट",
            shortDescription = "बढ़ती महंगाई, वैश्विक मंदी की आशंका और ब्याज दरों में बढ़ोतरी के कारण निवेशकों में घबराहट है। शेयर बाजार में बड़ी गिरावट देखी जा रही है, जिससे निवेशकों को भारी नुकसान उठाना पड़ा है।",
            mediumDescription = "भारतीय शेयर बाजार में आज भारी गिरावट देखने को मिली, जिसमें सेंसेक्स 1200 अंक से अधिक गिर गया और निफ्टी भी 18,000 के स्तर से नीचे चला गया। विशेषज्ञों का मानना है कि यह गिरावट मुख्य रूप से वैश्विक मंदी की आशंका, अमेरिकी फेडरल रिजर्व द्वारा ब्याज दरों में वृद्धि, और महंगाई के बढ़ते दबाव के कारण आई है। निवेशकों को इस समय सतर्क रहने की सलाह दी गई है, क्योंकि बाजार में अस्थिरता अभी बनी रह सकती है। \n\n फाइनेंशियल विशेषज्ञों के अनुसार, यह गिरावट अस्थायी हो सकती है, और निवेशकों को दीर्घकालिक निवेश की रणनीति अपनाने की सलाह दी गई है। सरकार और भारतीय रिजर्व बैंक बाजार स्थिरता बनाए रखने के लिए विभिन्न उपायों पर काम कर रहे हैं। हालांकि, आने वाले कुछ हफ्तों में निवेशकों को सतर्क रहने की आवश्यकता है, खासकर यदि वैश्विक बाजारों में और अधिक नकारात्मक संकेत मिलते हैं।",
            imageUrl = "https://source.unsplash.com/random/800x601",
            source = "Financial Daily",
            timestamp = "4h ago",
            articleLink = "https://www.financialdaily.com"
        ),
        NewsArticle(
            id = 3,
            title = "World Cup Final Approaching",
            shortDescription = "The cricketing world is eagerly awaiting the World Cup final, as two powerhouse teams get ready to battle it out for the prestigious trophy. Fans worldwide are anticipating a thrilling encounter between the best teams in the tournament.",
            mediumDescription = "As the World Cup final draws closer, cricket fever has reached its peak. The two strongest teams of the tournament will face off in what promises to be a historic battle. Experts are analyzing team strategies, player form, and pitch conditions to predict the possible outcome. \n\n Cricket legends have weighed in on the upcoming match, praising the consistency and determination of both teams. The final will not only determine the champion but also etch new heroes into the history books. Fans are traveling from across the globe to witness the showdown, and social media is abuzz with discussions, predictions, and excitement. With record-breaking viewership expected, this match is set to become one of the most memorable finals in cricket history.",
            imageUrl = "https://source.unsplash.com/random/800x602",
            source = "Sports Weekly",
            timestamp = "6h ago",
            articleLink = "https://www.sportsweekly.com"
        ),
        NewsArticle(
            id = 4,
            title = "दिल्ली में बारिश से जनजीवन अस्त-व्यस्त",
            shortDescription = "राजधानी दिल्ली में भारी बारिश के कारण सड़कों पर जलभराव हो गया है, जिससे यातायात प्रभावित हुआ। कई इलाकों में बिजली आपूर्ति बाधित हो गई, और प्रशासन स्थिति को नियंत्रित करने के लिए जुटा हुआ है।",
            mediumDescription = "दिल्ली-एनसीआर में पिछले 24 घंटों से लगातार हो रही बारिश के कारण जनजीवन पूरी तरह अस्त-व्यस्त हो गया है। मुख्य सड़कों पर जलभराव के चलते वाहनों की लंबी कतारें देखने को मिल रही हैं, जिससे यातायात व्यवस्था चरमरा गई है। कई इलाकों में जलभराव इतना अधिक हो गया है कि लोग घरों से बाहर निकलने में असमर्थ हैं। \n\n मौसम विभाग ने अगले 48 घंटों तक भारी बारिश की चेतावनी जारी की है, जिससे हालात और बिगड़ सकते हैं। प्रशासन ने जलभराव वाले क्षेत्रों में राहत कार्य शुरू कर दिए हैं, लेकिन लोगों को गैर-जरूरी यात्रा करने से बचने की सलाह दी गई है। इस बीच, बारिश के कारण कई स्कूल और ऑफिस बंद कर दिए गए हैं, जिससे लोगों को कुछ राहत मिली है।",
            imageUrl = "https://source.unsplash.com/random/800x603",
            source = "भारत समाचार",
            timestamp = "3h ago",
            articleLink = "https://www.bharatsamachar.com"
        ),
        NewsArticle(
            id = 5,
            title = "Tech: Apple Unveils New iPhone",
            shortDescription = "Apple has unveiled its latest iPhone, featuring a powerful AI chip, improved battery life, and a revolutionary camera system that enhances low-light photography. The new device promises to set a new benchmark in smartphone innovation.",
            mediumDescription = "Apple’s latest iPhone has been officially launched, and it is packed with cutting-edge features designed to enhance user experience. The device comes with an AI-powered chip that improves performance and efficiency, making multitasking smoother than ever. The battery life has also been significantly enhanced, allowing users to go longer without charging. \n\n One of the standout features of the new iPhone is its revolutionary camera system. With advanced AI algorithms and a larger sensor, the phone delivers exceptional low-light photography. Additionally, the device boasts improved security features, such as enhanced Face ID recognition and privacy-focused settings. Apple fans and tech enthusiasts are excited about these developments, with pre-orders already surpassing expectations.",
            imageUrl = "https://source.unsplash.com/random/800x604",
            source = "Gadget Insider",
            timestamp = "5h ago",
            articleLink = "https://www.gadgetinsider.com"
        )
    )



    NewsReelScreen(newsList = sampleNews)
}
