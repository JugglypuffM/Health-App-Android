package viewmodel

import auth.GrpcAuthenticatorStub
import utils.LocalStorage
import utils.Validator
/**
 * Провайдер ViewModel для всех активностей
 */
object ViewModelProvider: ViewModel by LoginViewModel(LocalStorage(), GrpcAuthenticatorStub(), Validator())