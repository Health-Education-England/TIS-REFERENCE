package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.DBC;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the DBC entity.
 */
@SuppressWarnings("unused")
public interface DBCRepository extends JpaRepository<DBC, Long>, JpaSpecificationExecutor {

  DBC findByDbc(String code);

  List<DBC> findByDbcIn(Set<String> dbcs);

}