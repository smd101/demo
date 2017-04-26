package com.example;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  @Data
  public static class GenericA {
    private String hoge;

    public <T> GenericA(T args) {
      System.out.println(args);
    }

  }

  @Test
  public void constructor_test() {
    GenericA genericA = new GenericA("hoge");
  }

  private List<?> getList(boolean flag) {
    return flag ? Arrays.asList("1", "2") : Arrays.asList(1, 2);
  }

  @Test
  public void wildcard_test() {
    Object hoge = getList(false).get(0);
    // Integer hoge = getList(false).get(0); // ERROR!! Type mismatch: cannot convert from capture#13-of ? to Intege

    System.out.println(hoge);
  }

  @Test
  public void wildcard_list_add_test() {
    List<?> wildcardList = new ArrayList<>();
    List<Number> numList = new ArrayList<>();
    List<Integer> intList = new ArrayList<>();

    numList.add(new Double(1));
    numList.add(new Integer(1));

    // intList.add(new Double(1)); // ERROR!! The method add(Integer) in the type List<Integer> is not applicable for the arguments (Double)
    intList.add(new Integer(1));

    // wildcardList.add(new Double(1)); // ERROR!! The method add(capture#13-of ?) in the type List<capture#13-of ?> is not applicable for the
    // arguments (Double)
    // wildcardList.add(new Integer(1)); // ERROR!! The method add(capture#14-of ?) in the type List<capture#14-of ?> is not applicable for the
    // arguments (Integer)
  }

  @Test
  public void wildcard_extends_list_test() {
    List<?> wildcardList = Arrays.asList(new Integer(1), new Integer(2));
    List<? extends Number> numList = Arrays.asList(new Integer(1), new Integer(2));

    // Number wildcardNum = wildcardList.get(0); // ERROR!! Type mismatch: cannot convert from capture#13-of ? to Number
    Object wildcardNum = wildcardList.get(0);
    Number number = numList.get(0);
  }

  @Test
  public void wildcard_super_list_test() {
    GenericSample<? super Integer> superSample = new GenericSample<>();

    // superSample.setHoge(new Double(1)); // The method setHoge(capture#15-of ? super Integer) in the type GenericTest.GenericSample<capture#15-of
    // ?super Integer> is not applicable for the arguments (Double)
    superSample.setHoge(new Integer(1));

  }

  public static class A {
  }

  public static class B extends A {
  }

  @Test
  public void vovariant() {
    Number[] numbers;
    Integer[] integers = new Integer[] { 1, 2, 3 };

    numbers = integers;

    Number num = new Number() {

      @Override
      public long longValue() {
        return 0;
      }

      @Override
      public int intValue() {
        return 0;
      }

      @Override
      public float floatValue() {
        return 0;
      }

      @Override
      public double doubleValue() {
        return 0;
      }
    };

    numbers[0] = num; // ERROR!! ArrayStoreException

  }

}
