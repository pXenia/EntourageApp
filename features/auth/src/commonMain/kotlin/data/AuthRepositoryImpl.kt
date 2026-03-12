package data

import com.entourageapp.core.network.TokenStorage
import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.dto.LoginRequestDto
import com.entourageapp.core.network.dto.RegisterRequestDto
import domain.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override val isAuthenticated: Flow<Boolean> =
        tokenStorage.accessToken.map { it != null }

    override suspend fun login(email: String, password: String): Result<Unit> =
        runCatching {
            val token = api.login(LoginRequestDto(email, password))
            tokenStorage.saveTokens(token.access_token, token.refresh_token)
        }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> =
        runCatching {
            api.register(RegisterRequestDto(name, email, password))
            login(email, password).getOrThrow()
        }

    override fun logout() {
        tokenStorage.clearTokens()
    }

    override suspend fun refreshTokens(): Result<Unit> =
        runCatching {
            val refresh = tokenStorage.refreshToken.first()
                ?: error("No refresh token")
            val token = api.refreshToken(refresh)
            tokenStorage.saveTokens(token.access_token, token.refresh_token)
        }
}