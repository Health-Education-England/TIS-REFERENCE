package com.transformuk.hee.tis.reference.service.api.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
@Transactional
class TrustValidatorTest {

  private TrustValidator validator;

  @Autowired
  private TrustRepository repository;

  @BeforeEach
  void setUp() {
    validator = new TrustValidator(repository);
  }

  @Test
  void shouldPassValidationWhenExistingTrustIsUpdatedWithSameCode() {
    Trust trust = new Trust();
    trust.setStatus(Status.CURRENT);
    trust.setCode("existingCode");
    trust.setTrustName("Some name");
    trust = repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setId(trust.getId());
    trustDto.setCode("existingCode");
    trustDto.setTrustName("Some other name");
    trustDto.setStatus(Status.CURRENT);

    assertDoesNotThrow(() -> validator.validate(trustDto));
  }

  @Test
  void shouldPassValidationWhenExistingCodeIsMadeInactive() {
    Trust trust = new Trust();
    trust.setStatus(Status.CURRENT);
    trust.setCode("existingCode");
    repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setId(trust.getId());
    trustDto.setCode("existingCode");
    trustDto.setStatus(Status.INACTIVE);

    assertDoesNotThrow(() -> validator.validate(trustDto));
  }

  @Test
  void shouldPassValidationWhenCodeIsNull() {
    Trust trust = new Trust();
    trust.setStatus(Status.CURRENT);
    repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setStatus(Status.CURRENT);

    assertDoesNotThrow(() -> validator.validate(trustDto));
  }

  @Test
  void shouldFailValidationWhenCurrentCodeExistsAndNewTrustCurrent() {
    Trust trust = new Trust();
    trust.setStatus(Status.CURRENT);
    trust.setCode("existingCode");
    repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setStatus(Status.CURRENT);
    trustDto.setCode("existingCode");

    assertThrows(RuntimeException.class, () -> validator.validate(trustDto));
  }

  @Test
  void shouldPassValidationWhenCurrentCodeExistsAndNewTrustInactive() {
    Trust trust = new Trust();
    trust.setStatus(Status.CURRENT);
    trust.setCode("existingCode");
    repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setStatus(Status.INACTIVE);
    trustDto.setCode("existingCode");

    assertDoesNotThrow(() -> validator.validate(trustDto));
  }

  @ParameterizedTest(
      name = "Should pass validation when existing code is inactive and new status is {0}")
  @EnumSource(Status.class)
  void shouldPassValidationWhenInactiveCodeExists(Status status) {
    Trust trust = new Trust();
    trust.setStatus(Status.INACTIVE);
    trust.setCode("existingCode");
    repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setStatus(status);
    trustDto.setCode("existingCode");

    assertDoesNotThrow(() -> validator.validate(trustDto));
  }

  @ParameterizedTest(name = "Should pass validation when code does not exist and new status is {0}")
  @EnumSource(Status.class)
  void shouldPassValidationWhenCodeNotExists(Status status) {
    Trust trust = new Trust();
    trust.setStatus(Status.INACTIVE);
    trust.setCode("existingCode");
    repository.save(trust);

    TrustDTO trustDto = new TrustDTO();
    trustDto.setStatus(status);
    trustDto.setCode("newCode");

    assertDoesNotThrow(() -> validator.validate(trustDto));
  }
}
