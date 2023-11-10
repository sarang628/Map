package com.sryang.torang_repository.di.repository.repository.impl

import com.sryang.torang_repository.api.ApiLogin
import com.sryang.torang_repository.api.handle
import com.sryang.torang_repository.data.dao.LoggedInUserDao
import com.sryang.torang_repository.data.entity.LoggedInUserEntity
import com.sryang.torang_repository.data.remote.response.LoginResponse
import com.sryang.torang_repository.data.remote.response.RemoteUser
import com.sryang.torang_repository.repository.LoginRepository
import com.sryang.torang_repository.session.SessionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val apiLogin: ApiLogin,
    private val sessionService: SessionService,
    private val loggedInUserDao: LoggedInUserDao
) : LoginRepository {
    override suspend fun emailLogin(
        email: String,
        password: String
    ): LoginResponse {
        val result = apiLogin.emailLogin(email = email, password = password)
        loggedInUserDao.insert(
            result.profile.toLoggedInUserEntity()
        )
        sessionService.saveToken(result.token)
        return result
    }

    override suspend fun logout() {
        loggedInUserDao.clear()
        sessionService.removeToken()
    }

    override suspend fun sessionCheck(): Boolean {
        sessionService.getToken()?.let {
            return apiLogin.sessionCheck(it)
        }
        return false;
    }

    override val isLogin: StateFlow<Boolean> get() = sessionService.isLogin
    override fun getUserName(): Flow<String> {
        return loggedInUserDao.getUserName()
    }

    override suspend fun checkEmail(email: String, password: String): String {
        try {
            return apiLogin.checkEmail(email, password)
        } catch (e: HttpException) {
            throw Exception(e.handle())
        }
    }

    override suspend fun confirmCode(
        token: String,
        confirmCode: String,
        name: String,
        email: String,
        password: String
    ): Boolean {
        try {
            return apiLogin.confirmCode(token, confirmCode, name, email, password);
        } catch (e: HttpException) {
            throw Exception(e.handle())
        }
    }

}

fun RemoteUser.toLoggedInUserEntity(): LoggedInUserEntity {
    return LoggedInUserEntity(
        userId = this.userId,
        userName = this.userName,
        email = this.email,
        loginPlatform = this.loginPlatform,
        createDate = this.createDate,
        profilePicUrl = profilePicUrl
    )
}