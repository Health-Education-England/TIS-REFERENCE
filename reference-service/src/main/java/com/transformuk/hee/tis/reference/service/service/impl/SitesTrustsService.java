package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.StringUtils.isEmpty;

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
  public SitesTrustsService(SiteRepository siteRepository, TrustRepository trustRepository, LocalOfficeRepository localOfficeRepository,
                            @Value("${search.result.limit:100}") int limit) {
    this.siteRepository = siteRepository;
    this.trustRepository = trustRepository;
    this.localOfficeRepository = localOfficeRepository;
    this.limit = limit;
  }

  /**
   * Searches all sites who have data containing given searchString
   *
   * @param searchString search string to be searched for site data
   * @param pageable     pageable defining the page and size
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
      Page<Site> sites = searchSites(searchString, new PageRequest(0, limit));
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
   * @param trustCode    Trust code for a trust inside which the sites have to be searched
   * @param searchString search string to be searched for site data
   * @return List of {@link Site} matching searchString for given trustCode
   */
  public List<Site> searchSitesWithinTrust(String trustCode, String searchString) {
    if (!isEmpty(searchString)) {
      return siteRepository.findBySearchStringAndTrustCode(trustCode, searchString, new PageRequest(0, limit));
    } else {
      return siteRepository.findByTrustCode(trustCode, new PageRequest(0, limit));
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
      return trustRepository.findBySearchString(searchString, new PageRequest(0, limit)).getContent();
    } else {
      return trustRepository.findAll();
    }
  }

  /**
   * Searches all trusts who have data containing given searchString
   *
   * @param searchString search string to be searched for trust data
   * @param pageable     pageable defining the page and size
   * @return Page of {@link Trust} matching searchString
   */
  public Page<Trust> searchTrusts(String searchString, Pageable pageable) {
    return trustRepository.findBySearchString(searchString, pageable);
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

  /**
   * Searches all localOffices who have data containing given searchString
   *
   * @param searchString search string to be searched for localOffice data
   * @param pageable     pageable defining the page and size
   * @return Page of {@link LocalOffice} matching searchString
   */
  public Page<LocalOffice> searchLocalOffices(String searchString, Pageable pageable) {
    return localOfficeRepository.findBySearchString(searchString, pageable);
  }

  /**
   * Searches all localOffices who have data containing given searchString
   *
   * @param searchString search string to be searched for trust data
   * @return List of {@link LocalOffice} matching searchString
   */
  public List<LocalOffice> searchLocalOffices(String searchString) {
    if (!isEmpty(searchString)) {
      return localOfficeRepository.findBySearchString(searchString, new PageRequest(0, limit)).getContent();
    } else {
      return localOfficeRepository.findAll();
    }
  }

}
