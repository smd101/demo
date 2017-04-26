package com.example;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DateAPITest {

  @Data
  @AllArgsConstructor
  private static class Fuga {
    private String key1;
    private String key2;
    private BigDecimal val1;
  }

  @Test
  public void Test2() {
    System.out.println(LocalDateTime.now().toString());
  }

  private String getSummaryKey(final Fuga fuga) {
    return fuga.getKey1() + ":" + fuga.getKey2();
  }

  @Test
  public void Test() {
    System.out.println("#####" + new Date());
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    df.setTimeZone(cal.getTimeZone());
    String timestamp = df.format(cal.getTime());

    System.out.println("#####" + timestamp);

    LocalDateTime localDateTime = LocalDateTime.now();
    System.out.println("#####" + localDateTime);

    ZonedDateTime jst = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
    ZonedDateTime utc = ZonedDateTime.ofInstant(jst.toInstant(), ZoneId.of("UTC"));
    System.out.println("#####" + jst);
    System.out.println("#####" + utc);

    ZonedDateTime zonedDateTime = new Date().toInstant().atZone(ZoneId.systemDefault());
    System.out.println("#####" + zonedDateTime);
    System.out.println("#####" + ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("UTC")));
    System.out.println("#####" + Date.from(zonedDateTime.toInstant()));
  }

  @Test
  public void JsonValueがISO8601_UTC準拠であること() throws JsonProcessingException, ParseException {
    Date date = DateUtils.parseDate("2017/03/14 13:32:09", "yyyy/MM/dd HH:mm:ss");
    Date utc = new Date();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(date);
    assertThat(json).isEqualTo("\"2017-03-14T04:32:09\"");
  }
}
