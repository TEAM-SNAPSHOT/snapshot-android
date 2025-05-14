package getSetting

import android.content.Context

fun getShotTime(context: Context): String {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return prefs.getString("shotTime", "8") ?: "8"
}

fun getAlbumName(context: Context): String {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return prefs.getString("albumName", "스냅샷") ?: "스냅샷"
}
