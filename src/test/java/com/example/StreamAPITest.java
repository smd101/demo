package com.example;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamAPITest {

  @Data
  @AllArgsConstructor
  private static class Moge {
    private String key;
    private String val1;
    private String val2;
  }

  @Test
  public void stream_parallel() {
    List<String> alphabet = Arrays.asList("a", "b", "c", "d", "e", "f");

    List<String> serial = new ArrayList<>();
    alphabet.stream().forEach(v -> serial.add(v)); // side-effects
    serial.forEach(System.out::println);
    assertThat(serial).containsExactly("a", "b", "c", "d", "e", "f");

    System.out.println("----------------------");

    List<String> parallel = new ArrayList<>();
    alphabet.stream().parallel().forEach(v -> parallel.add(v)); // side-effects
    parallel.forEach(System.out::println);
    assertThat(parallel).containsExactly("a", "b", "c", "d", "e", "f"); // Assertion ERROR!!

  }

  @Test
  public void stream_concatenate() {
    List<Moge> listA = Arrays.asList(
        new Moge("key1", "val1", null),
        new Moge("key2", "val2", null),
        new Moge("key3", "val3", null),
        new Moge("key4", "val4", null),
        new Moge("key5", "val5", null));

    List<Moge> listB = Arrays.asList(
        new Moge("key5", null, "VAL5"),
        new Moge("key7", null, "VAL7"),
        new Moge("key8", null, "VAL8"),
        new Moge("key1", null, "VAL1"),
        new Moge("key5", null, "VAL5"));

    List<Moge> listAB = listA.stream()
        .map(a -> {
          Moge findMoge = listB.stream().filter(b -> a.getKey().equals(b.getKey())).findFirst().orElse(a);
          return new Moge(a.getKey(), a.getVal1(), findMoge.getVal2());
        })
        .collect(Collectors.toList());

    listAB.forEach(System.out::println);
  }

  @Test
  public void stream_side_effects() {
    List<String> alphabet = Arrays.asList("a", "b", "c", "d", "e", "f");
    List<String> actual = new ArrayList<>();

    alphabet.stream()
        .filter(v -> v.compareTo("c") <= 0)
        .forEach(v -> actual.add(v)); // No side-effects!

    assertThat(actual).containsAll(Arrays.asList("a", "b", "c"));
  }

  @Test
  public void stream_generate() {
    Stream<Double> stream = Stream.generate(Math::random);
    stream.limit(10).forEach(System.out::println);
  }

  @Test
  public void stream_predicate() {
    List<String> alphabet = Arrays.asList("a", "b", "c", "d", "e", "f");
    List<String> actual = alphabet.stream()
        .filter(v -> v.compareTo("c") <= 0) // v を受取り、真偽値を返却する
        .collect(Collectors.toList());

    assertThat(actual).containsAll(Arrays.asList("a", "b", "c"));
  }

  @Test
  public void stream_function() {
    List<String> alphabet = Arrays.asList("a", "b", "c", "d", "e", "f");
    List<String> upperCase = alphabet.stream()
        .map(v -> v.toUpperCase()) // function. v を受取り、大文字にして返す
        .collect(Collectors.toList());

    assertThat(upperCase.get(0)).isEqualTo("A");
    assertThat(upperCase.get(1)).isEqualTo("B");
    assertThat(upperCase.get(2)).isEqualTo("C");
    assertThat(upperCase.get(3)).isEqualTo("D");
    assertThat(upperCase.get(4)).isEqualTo("E");
    assertThat(upperCase.get(5)).isEqualTo("F");
  }

  @Test
  public void stream_consumer() {
    List<String> alphabet = Arrays.asList("a", "b", "c", "d", "e", "f");
    alphabet.stream().forEach(System.out::println); // 外部出力に対して影響を及ぼしている→副作用を与えている
  }

  @Test
  public void stream_forEach() {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    numbers.stream().forEach(System.out::println);

    for (int i = 0; i < numbers.size(); i++) {
      System.out.println(numbers.get(i));
      // System.out::println // NG
    }

  }

  @Value
  private static class Hoge {
    private String key;
    private String value;
  }

  @Test
  public void stream_newInstance() {
    Map<String, String> dictionary = new HashMap<>();
    dictionary.put("hoge1", "HOGE1");
    dictionary.put("hoge2", "HOGE2");
    dictionary.put("hoge3", "HOGE3");

    List<Hoge> hoges = dictionary.entrySet().stream()
        .filter(v -> v.getKey().equals("hoge2"))
        .map(v -> new Hoge(v.getKey(), v.getValue()))
        .collect(Collectors.toList());

    assertThat(hoges.get(0).getValue()).isEqualTo("HOGE2");

  }

  @Test
  public void stream_map() {
    List<Hoge> hoges = new ArrayList<>();
    Hoge hoge1 = new Hoge("hoge1", "HOGE1");
    hoges.add(hoge1);
    Hoge hoge2 = new Hoge("hoge2", "HOGE2");
    hoges.add(hoge2);
    Hoge hoge3 = new Hoge("hoge3", "HOGE3");
    hoges.add(hoge3);

    List<String> dispNames = hoges.stream()
        .map(v -> new Formatter().format("%s: %s", v.getKey(), v.getValue()).toString())
        .collect(Collectors.toList());

    assertThat(dispNames.get(0)).isEqualTo("hoge1: HOGE1");

  }

  @Test
  public void stream_intemediate_operation_any() {
    List<String> nuumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    int sum = nuumbers.stream() // 1. numbers collection から、 Stream<String> を作る
        .mapToInt(Integer::parseInt) // 2-1. 中間処理で、Stringから、int に変換
        .filter(v -> v % 2 == 0) // 2-2. 中間処理で、偶数のみ抽出
        .sum(); // 3. 終端処理で、総和を求める

    assertThat(sum).isEqualTo(30);

  }

  @Test
  public void stream_step() {
    List<String> nuumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    int sum = nuumbers.stream() // 1. numbers collection から、 Stream<String> を作る
        .mapToInt(Integer::parseInt) // 2. 中間処理で、Stringから、int に変換
        .sum(); // 3. 終端処理で、総和を求める

    assertThat(sum).isEqualTo(55);
  }

  @Test
  public void doubleStream_sum() {
    double sum = DoubleStream.of(2.5d, 3.7d, 4.98d).sum();
    assertThat(sum).isEqualTo(11.18);
  }

  @Test
  public void intStream_sum() {
    // 1 - 100 までの総和
    int sum = IntStream.rangeClosed(1, 100).sum();
    assertThat(sum).isEqualTo(5050);
  }

  @Test
  public void objectStream_sum() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    int sum = list.stream().mapToInt(Integer::intValue).sum();
    assertThat(sum).isEqualTo(55);
  }

  /**
   * stream の値を元に、Map を作成する.
   */
  @Test
  public void howto_stream_to_map() {
    List<Hoge> hoges = new ArrayList<>();
    hoges.add(new Hoge("key1", "value1"));
    hoges.add(new Hoge("key2", "value2"));
    hoges.add(new Hoge("key3", "value3"));

    Map<String, Hoge> dictionary = hoges.stream()
        .filter(v -> v.key.equals("key1")
            || v.key.equals("key2"))
        .collect(Collectors.toMap(Hoge::getKey, v -> v));

    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key2")).isTrue();
    assertThat(dictionary.containsKey("key3")).isFalse();

    assertThat(dictionary.get("key1").getValue()).isEqualTo("value1");

  }

  /**
   * 重複を除外したリストの作り方. distinct の場合、equalsが適用されてしまうので、自前の除外条件を使用したい場合用.
   */
  @Test
  public void howto_no_duplicate_list() {
    List<Hoge> hoges = new ArrayList<>();
    hoges.add(new Hoge("key1", "value1"));
    hoges.add(new Hoge("key2", "value2"));
    hoges.add(new Hoge("key3", "value3"));
    hoges.add(new Hoge("key1", "valueX"));

    Set<String> set = new HashSet<>();
    List<Hoge> noDuplicateList = hoges.stream()
        .filter(v -> set.add(v.getKey()))
        .collect(Collectors.toList());

    assertThat(noDuplicateList.size()).isEqualTo(3);

  }

}
