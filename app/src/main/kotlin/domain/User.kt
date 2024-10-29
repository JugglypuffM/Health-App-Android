package domain

import java.io.Serializable

data class User(
    val name: String?,
    val login: String,
    val password: String
) : Serializable