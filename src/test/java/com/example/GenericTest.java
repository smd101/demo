package com.example;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Data;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenericTest {

  @Data
  public static class GenericSample<T> {
    private T hoge;
  }

  @Test
  public void class_test() {
    GenericSample<String> sample = new GenericSample<>();
    sample.setHoge("hoge!");
    assertThat(sample.getHoge()).isEqualTo("hoge!");
  }

  public static <T> T getValue(final T val, final T defaultValue) {
    return val == null ? defaultValue : val;
  }

  public static <T, V> String getValueB(final T val, final V defaultValue) {
    return val == null ? defaultValue.toString() : val.toString();
  }

  @Test
  public void method_test() {
    assertThat(getValueB(null, 2)).isEqualTo("2");
    assertThat(getValueB("hoge1", 2)).isEqualTo("hoge1");
    assertThat(getValueB("hoge1", "hoge2")).isEqualTo("hoge1");

    assertThat(getValue(null, "hoge")).isEqualTo("hoge");
    assertThat(getValue(null, 1)).isEqualTo(1);
    assertThat(getValue("hoge", 1)).isEqualTo("hoge"); // Objectとして判断されている
  }

}
