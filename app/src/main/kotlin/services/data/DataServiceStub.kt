package services.data

import domain.BasicUserData

class DataServiceStub: DataService {
    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> {
        return Result.success(BasicUserData("stubName"))
    }
}