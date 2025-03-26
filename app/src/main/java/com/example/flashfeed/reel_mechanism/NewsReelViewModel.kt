import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateMapOf

class NewsReelViewModel : ViewModel() {
    // Map to store liked states (Key: Article ID, Value: Boolean)
    val likedNewsMap = mutableStateMapOf<String, Boolean>()

    // Function to toggle like state
    fun toggleLike(newsId: String) {
        likedNewsMap[newsId] = !(likedNewsMap[newsId] ?: false)
    }

    // Function to check if an article is liked
    fun isLiked(newsId: String): Boolean {
        return likedNewsMap[newsId] ?: false
    }
}
