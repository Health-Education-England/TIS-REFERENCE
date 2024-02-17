package com.transformuk.hee.tis.reference.service.api.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class LocalOfficeContactValidatorTest {

  private LocalOfficeContactValidator validator;

  @BeforeEach
  void setUp() {
    validator = new LocalOfficeContactValidator();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {
      "valid@tis.nhs.uk",
      "https://valid.tis.nhs.uk",
      "https://valid.tis.nhs.uk/sub"
  })
  void shouldBeValidWhenAnEmailOrUrl(String contact) {
    LocalOfficeContactDto dto = new LocalOfficeContactDto();
    dto.setContact(contact);

    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "invalid@",
      "@invalid",
      "http://invalid.tis.nhs.uk",
      "ftp://invalid.tis.nhs.uk",
      "invalid"
  })
  void shouldNotBeValidWhenNotAnEmailOrUrl(String contact) {
    LocalOfficeContactDto dto = new LocalOfficeContactDto();
    dto.setContact(contact);
    assertThrows(CustomParameterizedException.class, () -> validator.validate(dto));
  }
}
