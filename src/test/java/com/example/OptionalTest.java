package com.example;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptionalTest {

  /**
   * Optional の isPresent により、取得値を切り分ける.
   */
  @Test
  public void howto_optional_return() {
    Optional<String> notNullOpt = Optional.of("aaa");
    String actual1 = notNullOpt.map(v -> v).orElse(null);
    assertThat(actual1).isEqualTo("aaa");

    Optional<String> nullOpt = Optional.empty();
    String actual2 = nullOpt.map(v -> v).orElse(null);
    assertThat(actual2).isNull();

  }

}
