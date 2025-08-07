package com.marvilanundry.marvi.presentation.di

import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.data.repository.ApiRepositoryImpl
import com.marvilanundry.marvi.data.repository.ClientRepositoryImpl
import com.marvilanundry.marvi.data.repository.OrderRepositoryImpl
import com.marvilanundry.marvi.data.repository.ServiceRepositoryImpl
import com.marvilanundry.marvi.domain.repository.ApiRepository
import com.marvilanundry.marvi.domain.repository.ClientRepository
import com.marvilanundry.marvi.domain.repository.OrderRepository
import com.marvilanundry.marvi.domain.repository.ServiceRepository
import com.marvilanundry.marvi.domain.usecase.GetApiWakeUpUseCase
import com.marvilanundry.marvi.domain.usecase.GetClientCodeUseCase
import com.marvilanundry.marvi.domain.usecase.GetOrderByIdUseCase
import com.marvilanundry.marvi.domain.usecase.GetOrdersByClientUseCase
import com.marvilanundry.marvi.domain.usecase.GetServicesUseCase
import com.marvilanundry.marvi.domain.usecase.PostLoginClientUseCase
import com.marvilanundry.marvi.domain.usecase.PostNewClientUseCase
import com.marvilanundry.marvi.domain.usecase.PostResetPasswordClientUseCase
import com.marvilanundry.marvi.domain.usecase.PutUpdateClientUseCase
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
    fun provideOrderRepository(api: ApiService): OrderRepository = OrderRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideClientRepository(api: ApiService): ClientRepository = ClientRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideServiceRepository(api: ApiService): ServiceRepository = ServiceRepositoryImpl(api)

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
    fun provideGetOrderByIdUseCase(repo: OrderRepository): GetOrderByIdUseCase =
        GetOrderByIdUseCase(repo)

    @Provides
    @Singleton
    fun provideGetOrdersByClientUseCase(repo: OrderRepository): GetOrdersByClientUseCase =
        GetOrdersByClientUseCase(repo)

    @Provides
    @Singleton
    fun provideGetServicesUseCase(repo: ServiceRepository): GetServicesUseCase =
        GetServicesUseCase(repo)

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

    @Provides
    @Singleton
    fun providePutUpdateClientUseCase(repo: ClientRepository): PutUpdateClientUseCase =
        PutUpdateClientUseCase(repo)
}