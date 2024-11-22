package data

import domain.BasicUserData

@Deprecated("Use new DataRequesterStub")
class OldDataRequesterStub : OldDataRequester {
    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> {
        return Result.success(BasicUserData("stubName"))
    }
}