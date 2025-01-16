package domain

import java.io.Serializable

data class User(
    var account: Account,
    var userInfo: UserInfo
) : Serializable {
    companion object {
        fun empty(): User {
            return User(Account.empty(), UserInfo.empty())
        }
    }
}