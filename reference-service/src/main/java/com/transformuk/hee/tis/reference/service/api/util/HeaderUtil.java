package com.transformuk.hee.tis.reference.service.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

  private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

  private static final String APPLICATION_NAME = "referenceApp";

  private HeaderUtil() {
  }

  public static HttpHeaders createAlert(String message, String param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-referenceApp-alert", message);
    headers.add("X-referenceApp-params", param);
    return headers;
  }

  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
  }

  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
  }

  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
  }

  public static HttpHeaders createFailureAlert(String entityName, String errorKey,
      String defaultMessage) {
    log.error("Entity creation failed, {}", defaultMessage);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-referenceApp-error", "error." + errorKey);
    headers.add("X-referenceApp-params", entityName);
    return headers;
  }
}
