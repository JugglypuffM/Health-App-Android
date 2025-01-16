package domain

import grpc.DataProto
import java.io.Serializable

/**
 * Класс с информацией о пользователе
 */
data class UserInfo(
    val name: String? = null,
    val age: Int? = null,
    val weight: Int? = null,
    val distance: Int? = null,
) : Serializable {
    constructor(userData: DataProto.UserData) : this(
        userData.name,
        userData.age,
        userData.weight,
        userData.totalDistance
    )

    fun toUserData(): DataProto.UserData = DataProto.UserData.newBuilder()
        .setName(name)
        .setAge(age ?: 0)
        .setWeight(weight ?: 0)
        .setTotalDistance(distance ?: 0)
        .build()

    companion object {
        fun empty(): UserInfo {
            return UserInfo("empty name", 0, 0, 0)
        }
    }
}
