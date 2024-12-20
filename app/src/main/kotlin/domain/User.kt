package domain

import java.io.Serializable

data class User(
    val account: Account,
    val userInfo: UserInfo
): Serializable