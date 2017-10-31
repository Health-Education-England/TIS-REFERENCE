package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReferenceService extends ClientService {

  /**
   * Find the sites given the codes. This does one API call and one SQL call to retrieve
   * all the sites.
   *
   * @param codes the site codes to look for
   * @return the found sites or an empty list
   */
  List<SiteDTO> findSitesIn(Set<String> codes);

  /**
   * Find the grades given the codes. This does one API call and one SQL call to retrieve
   * all the grades.
   *
   * @param codes the grade codes to look for
   * @return the found grades or an empty list
   */
  List<GradeDTO> findGradesIn(Set<String> codes);

  ResponseEntity<DBCDTO> getDBCByCode(String code);

  HttpStatus siteTrustMatch(String siteCode, String trustCode);

  Map<Long, Boolean> gradeExists(List<Long> Ids);

  Map<Long, Boolean> trustExists(List<Long> Ids);

  Map<Long, Boolean> siteExists(List<Long> Ids);

  HttpStatus trustCodeExists(String Code);

  HttpStatus siteCodeExists(String Code);

  Map<String, Boolean> medicalSchoolsExists(List<String> values);

  Map<String, Boolean> countryExists(List<String> values);

  Map<String, Boolean> rotationExists(List<String> values);

  Map<Long, Boolean> placementTypeExists(List<Long> values);
}
