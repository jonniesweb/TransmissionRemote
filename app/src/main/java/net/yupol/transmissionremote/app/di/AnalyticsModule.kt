package net.yupol.transmissionremote.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.yupol.transmissionremote.app.analytics.AnalyticsProvider
import net.yupol.transmissionremote.app.analytics.NoOpAnalyticsProvider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AnalyticsModule {
    @Binds
    @Singleton
    abstract fun bindAnalyticsProvider(provider: NoOpAnalyticsProvider): AnalyticsProvider
}
