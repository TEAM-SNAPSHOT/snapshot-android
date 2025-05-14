package saveSetting

import android.content.Context

fun saveShotTime(context: Context, value: String) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    prefs.edit().putString("shotTime", value).apply()
}

fun saveAlbumName(context: Context, value: String) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    prefs.edit().putString("albumName", value).apply()
}
