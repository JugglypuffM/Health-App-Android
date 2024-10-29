package viewmodel

import auth.GrpcAuthenticatorStub
import utils.LocalStorage

/**
 * Провайдер ViewModel для всех активностей
 */
object ViewModelProvider: ViewModel by LoginViewModel(LocalStorage(), GrpcAuthenticatorStub())