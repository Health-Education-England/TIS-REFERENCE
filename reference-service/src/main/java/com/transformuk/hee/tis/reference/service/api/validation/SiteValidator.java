package com.transformuk.hee.tis.reference.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.exception.ErrorConstants;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SiteValidator {

  private static final String START_DATE_BEFORE_END_DATE = "Start Date needs to be before End Date";
  private final Logger log = LoggerFactory.getLogger(SiteValidator.class);

  public void validate(SiteDTO siteDTO) {

    LocalDate startDate = siteDTO.getStartDate();
    LocalDate endDate = siteDTO.getEndDate();
    if (startDate != null && endDate != null) {
      log.debug("VALIDATION of startDate and endDate");
      if (startDate.isAfter(endDate)) {
        throw new CustomParameterizedException(
            START_DATE_BEFORE_END_DATE, ErrorConstants.ERR_VALIDATION);
      }
    }
  }
}
