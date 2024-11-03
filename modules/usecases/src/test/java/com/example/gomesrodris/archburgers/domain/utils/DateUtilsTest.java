package com.example.gomesrodris.archburgers.domain.utils;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateUtilsTest {

    @Test
    void validarTimestamp(){
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 1, 0,0,0);
        assertThat(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).isEqualTo(DateUtils.toTimestamp(dateTime));
    }

    @Test
    void validarTimestampErrado(){
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 1, 0,0,0);
        assertThat(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).isNotEqualTo(LocalDateTime.now());
    }

    @Test
    public void testToTimestampWithNullInput() {
        assertThrows(NullPointerException.class, () -> DateUtils.toTimestamp(null));
    }
}
