package domain

import java.io.Serializable

@Deprecated("Use Account instead")
data class User(
    val name: String?,
    val login: String,
    val password: String
) : Serializable