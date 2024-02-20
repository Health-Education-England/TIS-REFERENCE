package com.transformuk.hee.tis.reference.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.exception.ErrorConstants;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.annotationfactory.AnnotationDescriptor;
import org.hibernate.annotations.common.annotationfactory.AnnotationFactory;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.stereotype.Component;

/**
 * Validates that the a given LocalOfficeContact is valid.
 */
@Slf4j
@Component
public class LocalOfficeContactValidator {

  private final LocalOfficeContactRepository repository;

  private final EmailValidator emailValidator;
  private final URLValidator urlValidator;

  public LocalOfficeContactValidator(LocalOfficeContactRepository repository) {
    this.repository = repository;
    emailValidator = new EmailValidator();

    urlValidator = new URLValidator();
    AnnotationDescriptor descriptor = new AnnotationDescriptor(URL.class);
    descriptor.setValue("protocol", "https");
    URL url = AnnotationFactory.create(descriptor);
    urlValidator.initialize(url);
  }

  /**
   * Validate that the given Local Office contact is valid.
   *
   * @param dto The LocalOfficeContact to validate.
   */
  public void validate(LocalOfficeContactDto dto) {
    log.debug("Validating local office contact: {}", dto);

    // Validate that the contact is a valid email address or HTTPS based URL.
    String contact = dto.getContact();
    boolean isValid = emailValidator.isValid(contact, null) || urlValidator.isValid(contact, null);

    if (!isValid) {
      String message = String.format("Contact '%s' was not a valid Email or HTTPS URL.", contact);
      log.warn(message);
      throw new CustomParameterizedException(message, ErrorConstants.ERR_VALIDATION);
    }

    // Do not allow inserting a duplicate contact.
    if (dto.getId() == null) {
      List<LocalOfficeContact> existingContacts = repository.findByContactTypeIdAndLocalOfficeUuid(
          dto.getContactType().getId(), dto.getLocalOffice().getUuid());

      if (!existingContacts.isEmpty()) {
        String message = String.format("A '%s' contact already exists for the '%s' Local Office.",
            dto.getContactType().getLabel(), dto.getLocalOffice().getName());
        log.warn(message);
        throw new CustomParameterizedException(message, ErrorConstants.ERR_VALIDATION);
      }
    }
  }
}
