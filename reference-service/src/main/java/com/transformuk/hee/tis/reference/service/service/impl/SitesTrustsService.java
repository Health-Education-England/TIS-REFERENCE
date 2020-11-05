package com.transformuk.hee.tis.reference.service.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;
import static org.springframework.util.StringUtils.isEmpty;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to fetch reference data like sites and trusts
 */
@Service
public class SitesTrustsService {

  private final int limit;
  private SiteRepository siteRepository;
  private TrustRepository trustRepository;
  private LocalOfficeRepository localOfficeRepository;

  @Autowired
  public SitesTrustsService(SiteRepository siteRepository, TrustRepository trustRepository,
      LocalOfficeRepository localOfficeRepository, @Value("${search.result.limit:100}") int limit) {
    this.siteRepository = siteRepository;
    this.trustRepository = trustRepository;
    this.localOfficeRepository = localOfficeRepository;
    this.limit = limit;
  }

  /**
   * Searches all sites who have data containing given searchString
   *
   * @param searchString search string to be searched for site data
   * @param pageable pageable defining the page and size
   * @return List of {@link Site} matching searchString
   */
  public Page<Site> searchSites(String searchString, Pageable pageable) {
    return siteRepository.findBySearchString(searchString, pageable);
  }

  /**
   * Searches all sites who have data containing given searchString
   *
   * @param searchString search string to be searched for site data
   * @return List of {@link Site} matching searchString
   */
  public List<Site> searchSites(String searchString) {
    if (!isEmpty(searchString)) {
      Page<Site> sites = searchSites(searchString, PageRequest.of(0, limit));
      return sites.getContent();
    } else {
      return siteRepository.findAll();
    }
  }

  /**
   * Finds a grade given it's code
   *
   * @param siteCode the site code - NOT NULL
   * @return the site if found, null otherwise
   */
  public Site getSiteByCode(String siteCode) {
    checkNotNull(siteCode);
    return siteRepository.findBySiteCode(siteCode);
  }

  /**
   * @param trustCode Trust code for a trust inside which the sites have to be searched
   * @param searchString search string to be searched for site data
   * @return List of {@link Site} matching searchString for given trustCode
   */
  public List<Site> searchSitesWithinTrust(String trustCode, String searchString) {
    if (!isEmpty(searchString)) {
      return siteRepository.findBySearchStringAndTrustCode(trustCode, searchString,
          PageRequest.of(0, limit));
    } else {
      return siteRepository.findByTrustCode(trustCode, PageRequest.of(0, limit));
    }
  }

  /**
   * Returns a trust with given code
   *
   * @param trustCode Code for a trust - NOT NULL
   * @return {@link Trust} if found, null otherwise
   */
  public Trust getTrustByCode(String trustCode) {
    checkNotNull(trustCode);
    return trustRepository.findByCode(trustCode);
  }

  /**
   * Searches all trusts who have data containing given searchString
   *
   * @param searchString search string to be searched for trust data
   * @return List of {@link Trust} matching searchString
   */
  public List<Trust> searchTrusts(String searchString) {
    if (!isEmpty(searchString)) {
      return trustRepository.findBySearchString(searchString, PageRequest.of(0, limit))
          .getContent();
    } else {
      return trustRepository.findAll();
    }
  }

  /**
   * Searches all trusts who have data containing given searchString
   *
   * @param searchString search string to be searched for trust data
   * @param pageable pageable defining the page and size
   * @return Page of {@link Trust} matching searchString
   */
  public Page<Trust> searchTrusts(String searchString, Pageable pageable) {
    return trustRepository.findBySearchString(searchString, pageable);
  }

  /**
   * Searches all current trusts who have data containing given searchString
   *
   * @param searchString search string to be searched for trust data
   * @param pageable pageable defining the page and size
   * @return Page of {@link Trust} matching searchString
   */
  public Page<Trust> searchCurrentTrusts(String searchString, Pageable pageable) {
    return trustRepository.findByStatusAndSearchString(Status.CURRENT, searchString, pageable);
  }

  /**
   * Returns a localOffice with given code
   *
   * @param abbreviation Code for a LocalOffice - NOT NULL
   * @return {@link LocalOffice} if found, null otherwise
   */
  public LocalOffice getLocalOfficeByAbbreviation(String abbreviation) {
    checkNotNull(abbreviation);
    return localOfficeRepository.findByAbbreviation(abbreviation);
  }


  @Transactional(readOnly = true)
  public Page<Site> advanceSearchSite(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<Site>> specs = new ArrayList<>();
    // add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("siteCode", searchString))
          .or(containsLike("trustCode", searchString)).or(containsLike("siteName", searchString))
          .or(containsLike("postCode", searchString)).or(containsLike("address", searchString))
          .or(containsLike("siteKnownAs", searchString)));
    }
    // add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<Site> result;
    if (!specs.isEmpty()) {
      Specification<Site> fullSpec = Specification.where(specs.get(0));
      // add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = siteRepository.findAll(fullSpec, pageable);
    } else {
      result = siteRepository.findAll(pageable);
    }

    return result;
  }

  @Transactional(readOnly = true)
  public Page<Trust> advanceSearchTrust(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<Trust>> specs = new ArrayList<>();
    // add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("trustName", searchString))
          .or(containsLike("trustKnownAs", searchString)).or(containsLike("code", searchString))
          .or(containsLike("postCode", searchString)).or(containsLike("address", searchString)));
    }
    // add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<Trust> result;
    if (!specs.isEmpty()) {
      Specification<Trust> fullSpec = Specification.where(specs.get(0));
      // add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = trustRepository.findAll(fullSpec, pageable);
    } else {
      result = trustRepository.findAll(pageable);
    }
    return result;
  }

  @Transactional(readOnly = true)
  public Page<LocalOffice> advanceSearchLocalOffice(String searchString,
      List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<LocalOffice>> specs = new ArrayList<>();
    // add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("abbreviation", searchString))
          .or(containsLike("name", searchString)));
    }
    // add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<LocalOffice> result;
    if (!specs.isEmpty()) {
      Specification<LocalOffice> fullSpec = Specification.where(specs.get(0));
      // add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = localOfficeRepository.findAll(fullSpec, pageable);
    } else {
      result = localOfficeRepository.findAll(pageable);
    }

    return result;
  }

  /**
   * Searches current localOffices who have data containing given searchString
   *
   * @param searchString search string to be searched for localOffice data
   * @param pageable pageable defining the page and size
   * @return Page of {@link LocalOffice} matching searchString
   */
  public Page<LocalOffice> searchCurrentLocalOffices(String searchString, Pageable pageable) {
    return localOfficeRepository.findByStatusAndSearchString(Status.CURRENT, searchString,
        pageable);
  }

  /**
   * Searches all localOffices who have data containing given searchString
   *
   * @param searchString search string to be searched for trust data
   * @return List of {@link LocalOffice} matching searchString
   */
  public List<LocalOffice> searchLocalOffices(String searchString) {
    if (!isEmpty(searchString)) {
      return localOfficeRepository.findBySearchString(searchString, PageRequest.of(0, limit))
          .getContent();
    } else {
      return localOfficeRepository.findAll();
    }
  }

}
