package id.go.kebumenkab.realcount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.kebumenkab.realcount.model.LoginResponse
import id.go.kebumenkab.realcount.model.UserResponse
import id.go.kebumenkab.realcount.network.ApiConfig
import id.go.kebumenkab.realcount.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _login = MutableLiveData<Result<LoginResponse>>()
    val login: LiveData<Result<LoginResponse>> = _login

    fun login(baseUrl: String, appVersion: String, nip: String, password: String) {
        _login.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiServiceRealcount().login(appVersion, nip, password)
                _login.value = Result.Success(response)
            } catch (e: Exception) {
                _login.value = Result.Error(e.message.toString())
            }
        }
    }
}