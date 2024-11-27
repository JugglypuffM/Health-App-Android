package viewmodel

import auth.GrpcAuthenticator
import data.GrpcDataRequester
import utils.UserSerializer
import utils.Validator
/**
 * Провайдер ViewModel для всех активностей
 */
object ViewModelProvider : ViewModel by LoginViewModel(
    UserSerializer(),
    GrpcAuthenticator(),
    GrpcDataRequester(),
    Validator()
)