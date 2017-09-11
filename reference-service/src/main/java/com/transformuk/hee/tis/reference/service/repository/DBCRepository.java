package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.DBC;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the DBC entity.
 */
@SuppressWarnings("unused")
public interface DBCRepository extends JpaRepository<DBC, Long> {

  DBC findByDbc(String code);

  List<DBC> findByDbcIn(Set<String> dbcs);

}