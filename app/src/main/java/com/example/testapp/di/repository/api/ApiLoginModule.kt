package com.sryang.torang_repository.di.repository.api

import android.content.Context
import com.sryang.torang_repository.api.ApiLogin
import com.sryang.torang_repository.data.Filter
import com.sryang.torang_repository.data.Restaurant
import com.sryang.torang_repository.data.remote.response.LoginResult
import com.sryang.torang_repository.repository.LoginService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiLoginModule @Inject constructor(
    private val torangOkhttpClient: TorangOkhttpClient,
    private val retrofitModule: RetrofitModule
) {
    private var url = "http://sarang628.iptime.org:8081/"

    //    private var url = "http://192.168.0.14:8081/"
    private fun createLoginRetrofitService(url: String): ApiLogin {
        return retrofitModule.getRetrofit(torangOkhttpClient.getHttpClient(), url).create(
            ApiLogin::class.java
        )
    }

    fun create(url: String = this@ApiLoginModule.url): LoginService {
        val loginServiceForRetrofit = createLoginRetrofitService(url)
        return object : LoginService {
            override suspend fun emailLogin(email: String, password: String): LoginResult {
                val response = loginServiceForRetrofit.emailLogin(email, password)

                if (response.body() == null)
                    throw Exception("")
                else
                    return response.body()!!

            }

            override suspend fun join(filter: Filter): ArrayList<Restaurant> {
                TODO("Not yet implemented")
            }
        }
    }
}

fun getLoginService(context: Context): LoginService {
    return ApiLoginModule(
        torangOkhttpClient = TorangOkHttpClientImpl(context),
        retrofitModule = RetrofitModule()
    ).create()
}