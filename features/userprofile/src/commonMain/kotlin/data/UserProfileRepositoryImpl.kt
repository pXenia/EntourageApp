package com.entourageapp.features.userprofile.data

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.dto.UserDto
import com.entourageapp.features.userprofile.domain.UserProfileRepository

class UserProfileRepositoryImpl(
    private val api: AuthApi
) : UserProfileRepository {

    override suspend fun getMe(): UserDto = api.getMe()

    override suspend fun updateName(name: String) {
        api.updateName(name)
    }

    override suspend fun updatePassword(current: String, new: String) {
        api.updatePassword(current, new)
    }

    override suspend fun deleteAccount(password: String) {
        api.deleteAccount(password)
    }

    override suspend fun logout() {
        api.logout()
    }
}
