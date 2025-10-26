package com.nanit.bday.data.di

import com.nanit.bday.data.repository.ConnectionRepositoryImpl
import com.nanit.bday.data.source.WebSocketClient
import com.nanit.bday.domain.ConnectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Or another component if more appropriate
object DataModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets)
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000 // 30 seconds
                connectTimeoutMillis = 15_000 // 15 seconds for connection
                socketTimeoutMillis = 30_000
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(httpClient: HttpClient): WebSocketClient {

        return WebSocketClient(httpClient)
    }


    @Provides
    @Singleton
    fun provideConnectionRepository(webSocketClient: WebSocketClient): ConnectionRepository {
         return ConnectionRepositoryImpl(webSocketClient)
    }

}
