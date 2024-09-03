package com.transformuk.hee.tis.reference.service.service.mapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maps between Designated Body Codes and Local Office Name
 */
public class DbcToLocalOfficeMapper {

  private static final Map<String, List<String>> dbcToLocalOfficeMap =
      ImmutableMap.<String, List<String>>builder()
      .put("1-1RUZV1D", Lists.newArrayList("Kent, Surrey and Sussex"))
      .put("1-1RUZV6H", Lists.newArrayList("North West London"))
      .put("1-1RUZV4H",
          Lists.newArrayList("North Central and East London"))
      .put("1-1RSSQ5L", Lists.newArrayList("South London"))
      .put("1-1RSSPZ7", Lists.newArrayList("East Midlands"))
      .put("1-1RSSQ05", Lists.newArrayList("East of England"))
      .put("1-1RSSQ1B", Lists.newArrayList("North East"))
      .put("1-1RSSQ6R", Lists.newArrayList("Thames Valley"))
      .put("1-1RSG4X0", Lists.newArrayList("Yorkshire and the Humber"))
      .put("1-1RUZUYF", Lists.newArrayList("West Midlands"))
      .put("1-1RUZUVB", Lists.newArrayList("South West"))
      .put("1-1RUZUSF", Lists.newArrayList("Wessex"))
      .put("1-1RSSQ2H", Lists.newArrayList("North West"))
      .put("1-25U-830",
          Lists.newArrayList("Northern Ireland Medical and Dental Training Agency"))
      .build();

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
