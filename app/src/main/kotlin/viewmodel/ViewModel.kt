package viewmodel

import auth.AuthenticationWithValidation
import auth.GrpcAuthenticatorStub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.LocalStorage

object ViewModel {
    val authenticator = AuthenticationWithValidation(GrpcAuthenticatorStub())
    val storage = LocalStorage()
}