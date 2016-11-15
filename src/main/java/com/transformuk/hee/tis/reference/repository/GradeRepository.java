package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Grades
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, String> {
}
