package com.example;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Value;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaTest {

  @Test
  public void bigdecimal_test() {
    BigDecimal aa = BigDecimal.valueOf(1.0101);
    aa = aa.setScale(2, RoundingMode.UP);
    System.out.println(aa);
  }

  @Test
  public void equalsIgnoreCase_test() {
    assertThat("ｓ".equalsIgnoreCase("Ｓ")).isTrue();
    assertThat("s".equalsIgnoreCase("Ｓ")).isFalse();
    assertThat("S".equalsIgnoreCase("s")).isTrue();
  }

  @Test
  public void generics_invariant() {
    List<String> strList = new ArrayList<String>();
    strList.add("test1");
    strList.add("test2");
    List<Object> objList = new ArrayList<Object>();
    // objList = strList; // コンパイルエラー
  }

  @Test
  public void generics_covariant() {
    String[] strArray = { "test1", "test2" };
    Object[] objArray = strArray; // 配列は共変なので代入可能
    objArray[0] = new Integer(3); // java.lang.ArrayStoreException

    for (Object obj : objArray) {
      System.out.println(obj.toString());
    }
  }

  @Test
  public void string_test() {
    String a = "aaaa";
    String b = null;
    String c = "cccc";
    System.out.println(a + " " + b + " " + c);

    Map<String, BigDecimal> map = new HashMap<>();
    map.put("a", BigDecimal.ONE);
    map.put("a", map.get("a").add(BigDecimal.TEN));
    System.out.println(map.get("a"));
  }

  @Value
  private static class Row {
    private String key1;
    private String key2;
    private BigDecimal value;

    public String getKey() {
      return this.key1 + ":" + this.key2;
    }
  }

  @Test
  public void row_summary() {
    List<Row> rows = new ArrayList<>();
    Row row1 = new Row("hoge1", "hoge2", BigDecimal.valueOf(2.5));
    Row row2 = new Row("hoge1", "hoge2", BigDecimal.valueOf(4.5));
    Row row3 = new Row("moge1", "moge2", BigDecimal.valueOf(8));
    Row row4 = new Row("fuga1", "fuga2", BigDecimal.valueOf(12));
    Row row5 = new Row("fuga1", "moge2", BigDecimal.valueOf(1000));
    rows.add(row1);
    rows.add(row2);
    rows.add(row3);
    rows.add(row4);
    rows.add(row5);

    Map<String, List<Row>> mm = rows.stream().collect(Collectors.groupingBy(Row::getKey));

    mm.forEach((k, v) -> {
      System.out.println(String.format("%s -> %f", k, v.stream().map(Row::getValue).reduce(BigDecimal.ZERO, BigDecimal::add)));
    });
  }

  @Test
  public void bigdecimal_multiple_test() {
    BigDecimal one = BigDecimal.valueOf(1);
    BigDecimal three = BigDecimal.valueOf(3);
    BigDecimal ans = one.multiply(three);
    ans = ans.divide(three);
    ans = one.subtract(ans);
    System.out.println(ans);
    System.out.println(one.subtract(one.multiply(three).divide(three)));

  }

}
