package data

import com.entourageapp.core.network.PersistentCookiesStorage
import com.entourageapp.core.network.TokenStore
import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.dto.LoginRequestDto
import com.entourageapp.core.network.dto.RegisterRequestDto
import domain.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {

    override val isAuthenticated: Flow<Boolean> =
        tokenStore.hasAccessToken

    override suspend fun login(email: String, password: String): Result<Unit> =
        runCatching {
            api.login(LoginRequestDto(email, password))
        }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> =
        runCatching {
            api.register(RegisterRequestDto(name, email, password))
            login(email, password).getOrThrow()
        }

    override suspend fun logout(): Result<Unit> =
        runCatching {
            api.logout()
            tokenStore.clear()
        }

    override suspend fun refreshTokens(): Result<Unit> =
        runCatching {
            api.refreshToken()
        }
}