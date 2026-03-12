package domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    fun logout()
    suspend fun refreshTokens(): Result<Unit>
    val isAuthenticated: Flow<Boolean>
}