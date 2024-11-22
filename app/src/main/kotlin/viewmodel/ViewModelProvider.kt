package viewmodel

import auth.GrpcAuthenticator
import data.OldGrpcDataRequester
import utils.UserSerializer
import utils.Validator
/**
 * Провайдер ViewModel для всех активностей
 */
object ViewModelProvider : ViewModel by LoginViewModel(
    UserSerializer(),
    GrpcAuthenticator(),
    OldGrpcDataRequester(),
    Validator()
)