package net.yupol.transmissionremote.app.analytics

import javax.inject.Inject

class NoOpAnalyticsProvider @Inject constructor() : AnalyticsProvider {
    override fun logEvent(name: String, vararg params: Pair<String, Any?>) = Unit

    override fun setUserProperty(name: String, value: String?) = Unit
}
