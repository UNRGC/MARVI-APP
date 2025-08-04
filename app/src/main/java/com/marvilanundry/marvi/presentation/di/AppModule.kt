package com.marvilanundry.marvi.presentation.di

import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.data.repository.ApiRepositoryImpl
import com.marvilanundry.marvi.data.repository.ClientRepositoryImpl
import com.marvilanundry.marvi.domain.repository.ApiRepository
import com.marvilanundry.marvi.domain.repository.ClientRepository
import com.marvilanundry.marvi.domain.usecase.GetApiWakeUpUseCase
import com.marvilanundry.marvi.domain.usecase.GetClientCodeUseCase
import com.marvilanundry.marvi.domain.usecase.PostLoginClientUseCase
import com.marvilanundry.marvi.domain.usecase.PostNewClientUseCase
import com.marvilanundry.marvi.domain.usecase.PostResetPasswordClientUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // API
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiService()

    // Repositorios
    @Provides
    @Singleton
    fun provideApiRepository(api: ApiService): ApiRepository = ApiRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideClientRepository(api: ApiService): ClientRepository = ClientRepositoryImpl(api)

    // Casos de uso
    @Provides
    @Singleton
    fun provideGetApiWakeUpUseCase(repo: ApiRepository): GetApiWakeUpUseCase =
        GetApiWakeUpUseCase(repo)

    @Provides
    @Singleton
    fun provideGetClientCodeUseCase(repo: ClientRepository): GetClientCodeUseCase =
        GetClientCodeUseCase(repo)

    @Provides
    @Singleton
    fun providePostNewClientUseCase(repo: ClientRepository): PostNewClientUseCase =
        PostNewClientUseCase(repo)

    @Provides
    @Singleton
    fun providePostResetPasswordClientUseCase(repo: ClientRepository): PostResetPasswordClientUseCase =
        PostResetPasswordClientUseCase(repo)

    @Provides
    @Singleton
    fun providePostLoginClientUseCase(repo: ClientRepository): PostLoginClientUseCase =
        PostLoginClientUseCase(repo)
}