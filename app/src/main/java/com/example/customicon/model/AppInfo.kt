data class IconInfo(
    val name: String,
    val img_url: String
)

data class AppInfo(
    val appName: String,
    val packageName: String,
    val uriName: String,
    val listIcon: List<IconInfo>
)