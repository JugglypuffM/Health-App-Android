package domain

import java.io.Serializable

data class Account(
    val login: String,
    val password: String
) : Serializable {
    companion object {
        fun empty(): Account {
            return Account("empty login", "empty password")
        }
    }
}