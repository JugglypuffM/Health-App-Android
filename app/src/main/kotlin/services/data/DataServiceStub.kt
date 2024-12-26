package services.data

import domain.UserInfo

class DataServiceStub: DataService {
    override suspend fun getUserData(): Result<UserInfo> {
        return Result.success(UserInfo("stubName", 1, 100, 10000000))
    }

    override suspend fun updateUserData(info: UserInfo): Result<Unit> {
        return Result.success(Unit)
    }
}