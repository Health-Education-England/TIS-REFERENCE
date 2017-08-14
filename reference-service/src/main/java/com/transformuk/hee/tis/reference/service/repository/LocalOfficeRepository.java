package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the LocalOffice entity.
 */
@SuppressWarnings("unused")
public interface LocalOfficeRepository extends JpaRepository<LocalOffice, Long> {

	List<LocalOffice> findByNameIn(Set<String> localOffices);

}
