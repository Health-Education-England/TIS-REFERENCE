package com.transformuk.hee.tis.reference.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

/**
 * Tests the Designated Body Code to Local Office Mapper class
 */
public class DbcToLocalOfficeMapperTest {

  @Test
  public void shouldMap() {
    //Given
    Set<String> dbcs = Sets.newHashSet("1-1RUZV6H", "1-1RUZV1D", "1-1RSSQ2H");
    //When
    Set<String> mapResult = DbcToLocalOfficeMapper.map(dbcs);
    //Then
    assertThat(mapResult)
        .hasSize(3)
        .contains("Health Education England North West London",
            "Health Education England Kent, Surrey and Sussex",
            "Health Education England North West");
  }

  @Test
  public void shouldMapNimdtaLocalOffice() {
    Set<String> map = DbcToLocalOfficeMapper.map(Collections.singleton("1-25U-830"));

    assertThat(map)
        .hasSize(1)
        .contains("Northern Ireland Medical and Dental Training Agency");
  }

  @Test
  public void shouldHandleUnknownDbcs() {
    //Given
    Set<String> unknownDbcs = Sets.newHashSet("unknown");
    //When
    Set<String> unknownDbcsResult = DbcToLocalOfficeMapper.map(unknownDbcs);
    //Then
    assertThat(unknownDbcsResult).isEmpty();
  }

  @Test(expected = NullPointerException.class)
  public void shouldHandleNull() {
    //When
    DbcToLocalOfficeMapper.map(null);
  }
}
