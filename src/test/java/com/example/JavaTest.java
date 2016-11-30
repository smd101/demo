package com.example;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Value;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaTest {

  /**
   * Optional の isPresent により、取得値を切り分ける.
   */
  @Test
  public void howto_optional_return() {
    Optional<String> notNullOpt = Optional.of("aaa");
    String actual1 = notNullOpt.map(v->v).orElse(null);
    assertThat(actual1).isEqualTo("aaa");

    Optional<String> nullOpt = Optional.empty();
    String actual2 = nullOpt.map(v->v).orElse(null);
    assertThat(actual2).isNull();

  }

  
  @Value
  private static class Hoge {
    private String key;
    private String value;
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
        .collect(Collectors.toMap(Hoge::getKey, v->v));

    assertThat(dictionary.containsKey("key1")).isTrue();
    assertThat(dictionary.containsKey("key2")).isTrue();
    assertThat(dictionary.containsKey("key3")).isFalse();

    assertThat(dictionary.get("key1").getValue()).isEqualTo("value1");

  }

  /**
   * 重複を除外したリストの作り方.
   * distinct の場合、equalsが適用されてしまうので、自前の除外条件を使用したい場合用.
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
