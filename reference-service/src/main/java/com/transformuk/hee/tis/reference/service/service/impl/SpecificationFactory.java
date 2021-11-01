package com.transformuk.hee.tis.reference.service.service.impl;

import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Contains convenience pieces used to build specifications as query building blocks. For more info
 * please {@see https://jverhoelen.github.io/spring-data-queries-jpa-criteria-api/}
 */
public final class SpecificationFactory {

  private static final String DOT = ".";
  private static final String TRUE = "true";
  private static final String FALSE = "false";

  private SpecificationFactory() {
  }

  public static Specification containsLike(String attribute, String value) {
    return (root, query, cb) -> {
      if (StringUtils.isNotEmpty(attribute) && attribute.contains(DOT)) {
        String[] joinTable = StringUtils.split(attribute, DOT);
        Join tableJoin = root.join(joinTable[0], JoinType.LEFT);
        for (int i = 1; i < joinTable.length - 1; i++) {
          tableJoin = tableJoin.join(joinTable[i], JoinType.LEFT);
        }
        return cb.like(tableJoin.get(joinTable[joinTable.length - 1]), "%" + value + "%");
      } else {
        return cb.like(root.get(attribute), "%" + value + "%");
      }
    };
  }

  public static Specification isEqual(String attribute, Object value) {
    return (root, query, cb) -> {
      if (StringUtils.isNotEmpty(attribute) && attribute.contains(DOT)) {
        String[] joinTable = StringUtils.split(attribute, DOT);
        Join tableJoin = root.join(joinTable[0], JoinType.LEFT);
        for (int i = 1; i < joinTable.length - 1; i++) {
          tableJoin = tableJoin.join(joinTable[i], JoinType.LEFT);
        }
        return cb.equal(tableJoin.get(joinTable[joinTable.length - 1]), value);
      } else {
        return cb.equal(root.get(attribute), value);
      }
    };
  }

  /**
   * In condition for entity property, if property is from sub entity then it should contain '.' e.g
   * sites.siteId
   *
   * @param attribute
   * @param values
   * @return
   */
  public static Specification in(String attribute, Collection<Object> values) {
    return (root, query, cb) -> {
      CriteriaBuilder.In cbi;
      if (StringUtils.isNotEmpty(attribute) && attribute.contains(DOT)) {
        // this support multiple entity in criteria e.g specialties.specialty.name or sites.siteId
        String[] joinTable = StringUtils.split(attribute, DOT);
        Join tableJoin = root.join(joinTable[0], JoinType.INNER);
        for (int i = 1; i < joinTable.length - 1; i++) {
          tableJoin = tableJoin.join(joinTable[i], JoinType.INNER);
        }
        cbi = cb.in(tableJoin.get(joinTable[joinTable.length - 1])); // attribute
      } else {
        cbi = cb.in(root.get(attribute));
      }
      values.forEach(v -> {
        //handle booleans
        if (v.equals(TRUE) || v.equals(FALSE)) {
          v = new Boolean(v.toString());
        }
        if (StringUtils.isNumeric(v.toString())) {
          v = new Long(v.toString());
        }
        cbi.value(v);
      });
      return cbi;
    };
  }

  /**
   * In condition for entity property, if property is from sub entity then it should contain '.' e.g
   * sites.siteId, and is made specifically for strings where case is not relevant.
   *
   * @param attribute table name
   * @param values values to be searched
   * @return Specification
   */
  public static Specification inIgnoreCase(String attribute, Collection<String> values) {
    return (root, query, cb) -> {
      CriteriaBuilder.In cbi;
      if (StringUtils.isNotEmpty(attribute) && attribute.contains(DOT)) {
        // this support multiple entity in criteria e.g specialties.specialty.name or sites.siteId
        String[] joinTable = StringUtils.split(attribute, DOT);
        Join tableJoin = root.join(joinTable[0], JoinType.INNER);
        for (int i = 1; i < joinTable.length - 1; i++) {
          tableJoin = tableJoin.join(joinTable[i], JoinType.INNER);
        }
        cbi = cb.in(cb.upper(tableJoin.get(joinTable[joinTable.length - 1]))); // attribute
      } else {
        cbi = cb.in(cb.upper(root.get(attribute)));
      }
      values.forEach(v ->
          cbi.value(v.toUpperCase()));
      return cbi;
    };
  }

  public static Specification isBetween(String attribute, int min, int max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }

  public static Specification isBetween(String attribute, double min, double max) {
    return (root, query, cb) -> cb.between(root.get(attribute), min, max);
  }
}