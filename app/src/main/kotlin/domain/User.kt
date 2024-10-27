package domain

import java.io.Serializable

//TODO поменять на account
data class User(
    val name: String?,
    val login: String,
    val password: String
) : Serializable