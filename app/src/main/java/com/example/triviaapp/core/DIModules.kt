package com.example.triviaapp.core

import com.example.triviaapp.features.numbers.data.NumbersNetworkService
import com.example.triviaapp.features.numbers.data.NumbersRepositoryImpl
import com.example.triviaapp.features.numbers.data.models.DateTriviaModel
import com.example.triviaapp.features.numbers.data.models.NumberTriviaModel
import com.example.triviaapp.features.numbers.domain.NumbersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object SingletonModules {

    @Provides
    fun provideNumbersRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(strings.numbersBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    }

    @Provides
    fun provideLoggingInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor(interceptor)
            .readTimeout(100, TimeUnit.SECONDS).build()
    }

    @Provides
    fun provideNumbersNetworkService(retrofit: Retrofit): NumbersNetworkService {
        return retrofit.create(NumbersNetworkService::class.java)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModules {
    @Binds
    abstract fun bindNumbersRepository(
        numbersRepositoryImpl: NumbersRepositoryImpl): NumbersRepository

    @Binds
    abstract fun bindNumbersNetworkCall(
        networkCallImpl: NetworkCallImpl<NumberTriviaModel>): NetworkCall<NumberTriviaModel>

    @Binds
    abstract fun bindDateNetworkCall(
        networkCallImpl: NetworkCallImpl<DateTriviaModel>): NetworkCall<DateTriviaModel>

}

@Module
@InstallIn(ActivityComponent::class)
object FactoryModules{

    @Provides
    fun provideNumbersNetworkCall() = NetworkCallImpl<NumberTriviaModel>()

    @Provides
    fun provideDateNetworkCall() = NetworkCallImpl<DateTriviaModel>()
}