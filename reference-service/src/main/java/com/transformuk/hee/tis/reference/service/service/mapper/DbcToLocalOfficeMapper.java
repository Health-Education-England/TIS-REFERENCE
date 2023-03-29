package com.transformuk.hee.tis.reference.service.service.mapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Maps between Designated Body Codes and Local Office Name
 */
public class DbcToLocalOfficeMapper {

  static {
    if (StringUtils.contains(System.getenv("SPRING_PROFILES_ACTIVE"), "stage")) {
      // Use the temporary test values
      dbcToLocalOfficeMap = ImmutableMap.<String, List<String>>builder()
          .put("1-AIIDR8", Lists.newArrayList("Health Education England Kent, Surrey and Sussex"))
          .put("1-AIIDWA", Lists.newArrayList("Health Education England North West London"))
          .put("1-AIIDVS",
              Lists.newArrayList("Health Education England North Central and East London"))
          .put("1-AIIDWI", Lists.newArrayList("Health Education England South London"))
          .put("1-AIIDSA", Lists.newArrayList("Health Education England East Midlands"))
          .put("1-AIIDWT", Lists.newArrayList("Health Education England East of England"))
          .put("1-AIIDSI", Lists.newArrayList("Health Education England North East"))
          .put("1-AIIDH1", Lists.newArrayList("Health Education England Thames Valley"))
          .put("1-1P9Y9QH", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
          .put("1-AIIDMY", Lists.newArrayList("Health Education England West Midlands"))
          .put("1-AIIDMQ", Lists.newArrayList("Health Education England South West"))
          .put("1-AIIDHJ", Lists.newArrayList("Health Education England Wessex"))
          .put("1-1P9Y9R1", Lists.newArrayList("Health Education England North West"))
          .put("1-25U-830",
              Lists.newArrayList("Northern Ireland Medical and Dental Training Agency"))
          .put("1-AIIDQQ", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
          .build();
    } else {
      dbcToLocalOfficeMap = ImmutableMap.<String, List<String>>builder()
          .put("1-1RUZV1D", Lists.newArrayList("Health Education England Kent, Surrey and Sussex"))
          .put("1-1RUZV6H", Lists.newArrayList("Health Education England North West London"))
          .put("1-1RUZV4H",
              Lists.newArrayList("Health Education England North Central and East London"))
          .put("1-1RSSQ5L", Lists.newArrayList("Health Education England South London"))
          .put("1-1RSSPZ7", Lists.newArrayList("Health Education England East Midlands"))
          .put("1-1RSSQ05", Lists.newArrayList("Health Education England East of England"))
          .put("1-1RSSQ1B", Lists.newArrayList("Health Education England North East"))
          .put("1-1RSSQ6R", Lists.newArrayList("Health Education England Thames Valley"))
          .put("1-1RSG4X0", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
          .put("1-1RUZUYF", Lists.newArrayList("Health Education England West Midlands"))
          .put("1-1RUZUVB", Lists.newArrayList("Health Education England South West"))
          .put("1-1RUZUSF", Lists.newArrayList("Health Education England Wessex"))
          .put("1-1RSSQ2H", Lists.newArrayList("Health Education England North West"))
          .put("1-25U-830",
              Lists.newArrayList("Northern Ireland Medical and Dental Training Agency"))
          .build();
    }
  }

  private static final Map<String, List<String>> dbcToLocalOfficeMap;

  /**
   * @param dbcs list of Designation Body Codes not null
   * @return the list of local offices that match the given list of designated body codes
   */
  public static Set<String> map(Set<String> dbcs) {
    Preconditions.checkNotNull(dbcs);
    Set<String> localOffices = new HashSet<>();
    for (String dbc : dbcs) {
      if (dbcToLocalOfficeMap.containsKey(dbc)) {
        localOffices.addAll(dbcToLocalOfficeMap.get(dbc));
      }
    }
    return localOffices;
  }
}
