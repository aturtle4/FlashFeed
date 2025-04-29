import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleUtils {
    fun setAppLocale(context: Context, languageCode: String): ContextWrapper {
        // Save language code to preferences
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        // Use AppCompatDelegate for API 24+ (more consistent behavior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.create(locale)
            )
        }

        // Update configuration for the context
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        }

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Return wrapped context
        return ContextWrapper(context.createConfigurationContext(config))
    }

    fun getLocaleString(context: Context): String {
        return context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .getString("language", "en") ?: "en"
    }
}

// Extension function for easier access
fun Context.setAppLocale(languageCode: String): ContextWrapper {
    return LocaleUtils.setAppLocale(this, languageCode)
}