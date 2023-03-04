package com.example.mangkyuproject.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RatePointServiceTest {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    private RatePointService ratePointService;

    @ParameterizedTest
    @CsvSource(value = {"10000,100", "20000,200"})
    public void _10000원의적립은_100원(int price, int expected) {
        // when
        final int result = ratePointService.calculateAmount(price);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
