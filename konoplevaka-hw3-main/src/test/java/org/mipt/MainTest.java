package org.mipt;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class MainTest {
    @Test
    void helloTest() {
        Assertions.assertThat(2 + 2).isEqualTo(4);
    }
}
