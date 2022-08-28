package de.daill

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class WeserBotHealth: HealthIndicator {
    override fun health(): Health {
       return Health.up().build()
    }
}