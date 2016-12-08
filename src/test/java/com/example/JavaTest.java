package com.example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaTest {

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

}
