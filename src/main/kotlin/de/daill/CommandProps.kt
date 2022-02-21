package de.daill

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("commands")
class CommandProps {
    private val news: Map<String, String>? = null
}