package com.example.gomesrodris.archburgers.domain.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {

    @Test
    public void isEmpty_nullString_returnsTrue() {
        boolean result = StringUtils.isEmpty(null);
        assertThat(result).isTrue();
    }

    @Test
    public void isEmpty_emptyString_returnsTrue() {
        boolean result = StringUtils.isEmpty("");
        assertThat(result).isTrue();
    }

    @Test
    public void isEmpty_stringWithOnlySpaces_returnsTrue() {
        boolean result = StringUtils.isEmpty("   ");
        assertThat(result).isTrue();
    }

    @Test
    public void isEmpty_nonEmptyString_returnsFalse() {
        boolean result = StringUtils.isEmpty("nonEmptyString");
        assertThat(result).isFalse();
    }
}