package com.transformuk.hee.tis.reference.service.api.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactTypeDto;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.dto.TraineeTypeDto;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactRepository;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class LocalOfficeContactValidatorTest {

  private static final UUID CONTACT_TYPE_ID = UUID.randomUUID();
  private static final UUID LOCAL_OFFICE_UUID = UUID.randomUUID();
  private static final UUID TRAINEE_TYPE_ID = UUID.randomUUID();

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
    LocalOfficeContactDto dto = createContactDto(TRAINEE_TYPE_ID);
    dto.setContact(contact);

    when(repository.findByContactTypeIdAndLocalOfficeUuidAndTraineeTypeId(any(), any(), any()))
        .thenReturn(Collections.emptyList());

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
    LocalOfficeContactDto dto = createContactDto(TRAINEE_TYPE_ID);
    dto.setContact(contact);

    CustomParameterizedException exception = assertThrows(CustomParameterizedException.class,
        () -> validator.validate(dto));

    assertThat("Unexpected message.", exception.getMessage(), is(
        String.format("Contact '%s' was not a valid Email or HTTPS URL.", contact)));
  }

  @ParameterizedTest
  @MethodSource("missingTraineeTypeIds")
  void shouldNotBeValidWhenTraineeTypeIdMissing(UUID contactId, TraineeTypeDto traineeType) {
    LocalOfficeContactDto dto = createContactDto(TRAINEE_TYPE_ID);
    dto.setId(contactId);
    dto.setTraineeType(traineeType);

    CustomParameterizedException exception = assertThrows(CustomParameterizedException.class,
        () -> validator.validate(dto));

    assertThat("Unexpected message.", exception.getMessage(), is("Trainee Type ID is required."));
  }

  private static Stream<Arguments> missingTraineeTypeIds() {
    TraineeTypeDto traineeTypeWithoutId = new TraineeTypeDto();
    traineeTypeWithoutId.setCode("FOUNDATION");
    traineeTypeWithoutId.setLabel("Foundation");

    return Stream.of(
        Arguments.of(null, null),
        Arguments.of(null, new TraineeTypeDto()),
        Arguments.of(null, traineeTypeWithoutId),
        Arguments.of(UUID.randomUUID(), null),
        Arguments.of(UUID.randomUUID(), new TraineeTypeDto()),
        Arguments.of(UUID.randomUUID(), traineeTypeWithoutId)
    );
  }

  @Test
  void shouldNotBeValidWhenDuplicateIndexOnCreate() {
    LocalOfficeContactDto dto = createContactDto(TRAINEE_TYPE_ID);

    when(repository.findByContactTypeIdAndLocalOfficeUuidAndTraineeTypeId(CONTACT_TYPE_ID,
        LOCAL_OFFICE_UUID, TRAINEE_TYPE_ID)).thenReturn(
        Collections.singletonList(new LocalOfficeContact()));

    CustomParameterizedException exception = assertThrows(CustomParameterizedException.class,
        () -> validator.validate(dto));

    assertThat("Unexpected message.", exception.getMessage(), is(
        "A 'TYPE_LABEL' contact already exists for the 'LO_NAME' Local Office and 'TT_LABEL'"
            + " trainee type."));
  }

  @Test
  void shouldBeValidWhenSameContactTypeAndLocalOfficeExistForDifferentTraineeTypeOnCreate() {
    UUID otherTraineeTypeId = UUID.randomUUID();
    LocalOfficeContactDto dto = createContactDto(otherTraineeTypeId);

    when(repository.findByContactTypeIdAndLocalOfficeUuidAndTraineeTypeId(CONTACT_TYPE_ID,
        LOCAL_OFFICE_UUID, TRAINEE_TYPE_ID)).thenReturn(
        Collections.singletonList(new LocalOfficeContact()));
    when(repository.findByContactTypeIdAndLocalOfficeUuidAndTraineeTypeId(CONTACT_TYPE_ID,
        LOCAL_OFFICE_UUID, otherTraineeTypeId)).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldBeValidWhenDuplicateIndexOnUpdate() {
    LocalOfficeContactDto dto = createContactDto(TRAINEE_TYPE_ID);
    dto.setId(UUID.randomUUID());

    when(repository.findByContactTypeIdAndLocalOfficeUuidAndTraineeTypeId(any(), any(), any()))
        .thenReturn(Collections.singletonList(new LocalOfficeContact()));

    assertDoesNotThrow(() -> validator.validate(dto));
  }

  private static LocalOfficeContactDto createContactDto(UUID traineeTypeId) {
    LocalOfficeContactTypeDto contactType = new LocalOfficeContactTypeDto();
    contactType.setId(CONTACT_TYPE_ID);
    contactType.setLabel("TYPE_LABEL");

    LocalOfficeDTO localOffice = new LocalOfficeDTO();
    localOffice.setUuid(LOCAL_OFFICE_UUID);
    localOffice.setName("LO_NAME");

    TraineeTypeDto traineeType = new TraineeTypeDto();
    traineeType.setId(traineeTypeId);
    traineeType.setLabel("TT_LABEL");

    LocalOfficeContactDto dto = new LocalOfficeContactDto();
    dto.setContact("duplicate@tis.nhs.uk");
    dto.setContactType(contactType);
    dto.setLocalOffice(localOffice);
    dto.setTraineeType(traineeType);
    return dto;
  }
}
