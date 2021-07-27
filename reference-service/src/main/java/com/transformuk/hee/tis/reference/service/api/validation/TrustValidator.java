package com.transformuk.hee.tis.reference.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.exception.ErrorConstants;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrustValidator {

  private final TrustRepository repository;

  public TrustValidator(TrustRepository repository) {
    this.repository = repository;
  }

  public void validate(TrustDTO trustDto) {
    log.debug("Validating trust: {}", trustDto);

    if (trustDto.getStatus() == Status.CURRENT) {
      Long id = trustDto.getId();
      String code = trustDto.getCode();

      List<Trust> existingTrusts = repository.findByCodeAndStatus(code, Status.CURRENT);
      existingTrusts.removeIf(t -> t.getId().equals(id));

      if (!existingTrusts.isEmpty()) {
        log.warn("Trust failed validation due to non-unique code '{}'.", code);
        throw new CustomParameterizedException(code, ErrorConstants.ERR_VALIDATION);
      }
    }
  }
}
