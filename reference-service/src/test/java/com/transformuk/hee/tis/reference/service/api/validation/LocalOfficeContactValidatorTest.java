package com.transformuk.hee.tis.reference.service.api.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactTypeDto;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactRepository;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class LocalOfficeContactValidatorTest {

  private LocalOfficeContactValidator validator;
  private LocalOfficeContactRepository repository;

  @BeforeEach
  void setUp() {
    repository = mock(LocalOfficeContactRepository.class);
    validator = new LocalOfficeContactValidator(repository);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {
      "valid@tis.nhs.uk",
      "https://valid.tis.nhs.uk",
      "https://valid.tis.nhs.uk/sub"
  })
  void shouldBeValidWhenAnEmailOrUrlAndUnique(String contact) {
    LocalOfficeContactDto dto = new LocalOfficeContactDto();
    dto.setContact(contact);
    dto.setContactType(new LocalOfficeContactTypeDto());
    dto.setLocalOffice(new LocalOfficeDTO());

    when(repository.findByContactTypeIdAndLocalOfficeUuid(any(), any())).thenReturn(
        Collections.emptyList());

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
    dto.setContactType(new LocalOfficeContactTypeDto());
    dto.setLocalOffice(new LocalOfficeDTO());

    CustomParameterizedException exception = assertThrows(CustomParameterizedException.class,
        () -> validator.validate(dto));

    assertThat("Unexpected message.", exception.getMessage(), is(
        String.format("Contact '%s' was not a valid Email or HTTPS URL.", contact)));
  }

  @Test
  void shouldNotBeValidWhenDuplicateIndexOnCreate() {
    LocalOfficeContactTypeDto contactType = new LocalOfficeContactTypeDto();
    UUID contactTypeId = UUID.randomUUID();
    contactType.setId(contactTypeId);
    contactType.setLabel("TYPE_LABEL");

    LocalOfficeDTO localOffice = new LocalOfficeDTO();
    UUID localOfficeUuid = UUID.randomUUID();
    localOffice.setUuid(localOfficeUuid);
    localOffice.setName("LO_NAME");

    LocalOfficeContactDto dto = new LocalOfficeContactDto();
    dto.setContact("duplicate@tis.nhs.uk");
    dto.setContactType(contactType);
    dto.setLocalOffice(localOffice);

    when(repository.findByContactTypeIdAndLocalOfficeUuid(any(), any())).thenReturn(
        Collections.singletonList(new LocalOfficeContact()));

    CustomParameterizedException exception = assertThrows(CustomParameterizedException.class,
        () -> validator.validate(dto));

    assertThat("Unexpected message.", exception.getMessage(),
        is("A 'TYPE_LABEL' contact already exists for the 'LO_NAME' Local Office."));
    verify(repository).findByContactTypeIdAndLocalOfficeUuid(contactTypeId, localOfficeUuid);
  }

  @Test
  void shouldBeValidWhenDuplicateIndexOnUpdate() {
    LocalOfficeContactTypeDto contactType = new LocalOfficeContactTypeDto();
    UUID contactTypeId = UUID.randomUUID();
    contactType.setId(contactTypeId);
    contactType.setLabel("TYPE_LABEL");

    LocalOfficeDTO localOffice = new LocalOfficeDTO();
    UUID localOfficeUuid = UUID.randomUUID();
    localOffice.setUuid(localOfficeUuid);
    localOffice.setName("LO_NAME");

    LocalOfficeContactDto dto = new LocalOfficeContactDto();
    dto.setId(UUID.randomUUID());
    dto.setContact("duplicate@tis.nhs.uk");
    dto.setContactType(contactType);
    dto.setLocalOffice(localOffice);

    when(repository.findByContactTypeIdAndLocalOfficeUuid(any(), any())).thenReturn(
        Collections.singletonList(new LocalOfficeContact()));

    assertDoesNotThrow(() -> validator.validate(dto));
  }
}
