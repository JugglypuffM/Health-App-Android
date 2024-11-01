package viewmodel

import auth.GrpcAuthenticatorStub
import data.DataRequester
import data.DataRequesterStub
import data.GrpcDataRequester
import utils.UserSerializer
import utils.Validator
/**
 * Провайдер ViewModel для всех активностей
 */
object ViewModelProvider: ViewModel by LoginViewModel(UserSerializer(), GrpcAuthenticatorStub(), DataRequesterStub(), Validator())