package com.example.mangkyuproject.study;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class AStudyTest {
    @Test
    void testEnumName() {
        // given


        // when


        // then
        assertThat(TestEnum.THIS_IS_NAME.name()).isEqualTo("this is value");
    }
}

enum TestEnum {
    THIS_IS_NAME("this is value")
    ;

    private final String text;

    TestEnum(String text) {
        this.text = text;
    }
}
