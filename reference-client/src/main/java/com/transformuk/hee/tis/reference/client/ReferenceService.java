package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDto;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * An interface for the client reference service.
 */
public interface ReferenceService extends ClientService {

  /**
   * Find the sites given the codes. This does one API call and one SQL call to retrieve all the
   * sites.
   *
   * @param codes the site codes to look for
   * @return the found sites or an empty list
   */
  List<SiteDTO> findSitesIn(Set<String> codes);

  /**
   * Find the sites given the ids. This does one API call and one SQL call to retrieve all the
   * sites.
   *
   * @param ids the site ids to look for
   * @return the found sites or an empty list
   */
  List<SiteDTO> findSitesIdIn(Set<Long> ids);

  /**
   * Find the sites given the name. This does one API call and one SQL call to retrieve all the
   * sites.
   *
   * @param name the site name to look for
   * @return the found sites or an empty list
   */
  List<SiteDTO> findSitesByName(String name);

  /**
   * Find the grades given the codes. This does one API call and one SQL call to retrieve all the
   * grades.
   *
   * @param codes the grade codes to look for
   * @return the found grades or an empty list
   */
  List<GradeDTO> findGradesIn(Set<String> codes);

  /**
   * Find the roles given the codes. This does one API call and one SQL call to retrieve all the
   * roles.
   *
   * @param codes the role codes to look for
   * @return the found roles or an empty list
   */
  List<RoleDTO> findRolesIn(String codes);

  /**
   * Find the grades given the Ids. This does one API call and one SQL call to retrieve all the
   * grades.
   *
   * @param ids the grade ids to look for
   * @return the found grades or an empty list
   */
  List<GradeDTO> findGradesIdIn(Set<Long> ids);

  /**
   * Find the grades given the name. This does one API call and one SQL call to retrieve all the
   * grades.
   *
   * @param name the grade name to look for
   * @return the found grades or an empty list
   */
  List<GradeDTO> findGradesByName(String name);

  /**
   * Find the grades with status=CURRENT and placementGrade=true and trainingGrade=true.
   * This does one API call and one SQL call to retrieve all the grades.
   *
   * @return the found grades or an empty list
   */
  List<GradeDTO> findGradesCurrentPlacementAndTrainingGrades();

  /**
   * Find the trusts given the trustKnownAs. This does one API call and one SQL call to retrieve all
   * the trusts.
   *
   * @param trustKnownAs The trustKnownAs to look for.
   * @return The found trusts or an empty list.
   */
  List<TrustDTO> findTrustByTrustKnownAs(String trustKnownAs);

  /**
   * Find the trust given the id.
   *
   * @param id The id of the requested trust.
   * @return The found trust.
   */
  TrustDTO findTrustById(Long id);

  /**
   * Find all AssessmentTypes.
   *
   * @return the list of AssessmentTypes
   */
  List<AssessmentTypeDto> findAllAssessmentTypes();

  List<LocalOfficeDTO> findLocalOfficesByName(String localOfficeName);

  List<TrustDTO> findCurrentTrustsByTrustKnownAsIn(Set<String> allTrustKnownAs);

  List<FundingTypeDTO> findCurrentFundingTypesByLabelIn(Set<String> labels);

  ResponseEntity<DBCDTO> getDBCByCode(String code);

  HttpStatus siteTrustMatch(String siteCode, String trustCode);

  Map<String, Boolean> gradeExists(List<String> Ids);

  Map<Long, Boolean> gradeIdsExists(List<Long> ids);

  Map<String, Boolean> rolesExist(List<String> codes, boolean currentOnly);

  Map<String, String> rolesMatch(List<String> codes, boolean currentOnly);

  Map<String, Boolean> trustExists(List<String> Ids);

  Map<Long, Boolean> trustIdsExists(List<Long> ids);

  Map<String, Boolean> siteExists(List<String> Ids);

  Map<Long, Boolean> siteIdExists(List<Long> Ids);

  HttpStatus trustCodeExists(String Code);

  HttpStatus siteCodeExists(String Code);

  Map<String, Boolean> medicalSchoolsExists(List<String> values);

  Map<String, Boolean> countryExists(List<String> values);

  Map<String, Boolean> rotationExists(List<String> values);

  Map<String, Boolean> placementTypeExists(List<String> values);

  Boolean isValueExists(Class dtoClass, String value);

  Boolean isValueExists(Class dtoClass, String value, boolean currentOnly);

  List<LocalOfficeDTO> findAllLocalOffice();

  Collection<RoleDTO> getRolesByCategory(final Long categoryId);

  Set<DBCDTO> getAllDBCs();

  Map<String, Boolean> programmeMembershipTypesExist(List<String> codes, boolean currentOnly);

  Map<String, String> leavingReasonsMatch(List<String> codes, boolean currentOnly);
}
