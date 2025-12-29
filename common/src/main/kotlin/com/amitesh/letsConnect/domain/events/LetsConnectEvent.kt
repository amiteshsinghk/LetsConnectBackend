package com.amitesh.letsConnect.domain.events

import java.time.Instant

interface LetsConnectEvent {
    val eventId: String
    val eventKey: String
    val occurredAt: Instant
    val exchange: String
}