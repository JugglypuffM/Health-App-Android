package viewmodel

import auth.GrpcAuthenticatorStub
import utils.LocalStorage

object ViewModelProvider: ViewModel by LoginViewModel(LocalStorage(), GrpcAuthenticatorStub())