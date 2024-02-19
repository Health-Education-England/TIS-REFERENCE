package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocalOfficeContact entity.
 */
@Repository
public interface LocalOfficeContactRepository extends JpaRepository<LocalOfficeContact, UUID>,
    JpaSpecificationExecutor<LocalOfficeContact> {

}
