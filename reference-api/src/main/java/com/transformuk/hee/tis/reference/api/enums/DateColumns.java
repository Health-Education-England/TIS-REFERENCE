package com.transformuk.hee.tis.reference.api.enums;

/**
 * Represents any date/datetime columns. This is used to filter results based on a given date range.
 * Any date columns needed to searched on date range and using column filter and ColumnFilterUtil should added here.
 */
public enum DateColumns {

  // Placement entity date columns
  DATE_FROM("dateFrom"),
  DATE_TO("dateTo");

  private String value;

  DateColumns(String dateFrom) {
    this.value = dateFrom;
  }

  public String getValue() {
    return value;
  }

  public static boolean contains(String dateColumn) {

    for (DateColumns c : DateColumns.values()) {
      if (c.getValue().equals(dateColumn)) {
        return true;
      }
    }
    return false;
  }

}
