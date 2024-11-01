package data

import domain.BasicUserData
import domain.User

class DataRequesterStub: DataRequester {
    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> {
        return Result.success(BasicUserData("stubName"))
    }
}