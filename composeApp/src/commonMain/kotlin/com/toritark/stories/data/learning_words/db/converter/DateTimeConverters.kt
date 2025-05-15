package com.toritark.stories.data.learning_words.db.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

internal object DateTimeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun dateToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }
}