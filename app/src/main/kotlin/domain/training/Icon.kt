package domain.training

data class Icon(
    var title: String,
    var description: String,
    var imageResId: Int,
    var activityClass: Class<*>
)
