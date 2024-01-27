package kz.zhanarys.data.configurations

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Timestamp {
    fun generateTimestamp(): Long = System.currentTimeMillis() / 1000

    fun generateFormattedTimestamp(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(formatter)
    }
}