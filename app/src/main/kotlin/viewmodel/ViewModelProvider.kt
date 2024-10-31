package viewmodel

import auth.GrpcAuthenticatorStub
import utils.UserSerializer
import utils.Validator
/**
 * Провайдер ViewModel для всех активностей
 */
object ViewModelProvider: ViewModel by LoginViewModel(UserSerializer(), GrpcAuthenticatorStub(), Validator())