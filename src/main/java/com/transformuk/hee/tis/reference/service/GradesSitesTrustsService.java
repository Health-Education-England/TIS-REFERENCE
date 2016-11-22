package com.transformuk.hee.tis.reference.service;

import com.transformuk.hee.tis.reference.model.Grade;
import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import com.transformuk.hee.tis.reference.repository.GradeRepository;
import com.transformuk.hee.tis.reference.repository.SiteRepository;
import com.transformuk.hee.tis.reference.repository.TrustRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Service to fetch reference data like grades, sites, trust
 */
@Service
public class GradesSitesTrustsService {
	private GradeRepository gradeRepository;
	private SiteRepository siteRepository;
	private TrustRepository trustRepository;

	private final int limit;

	@Autowired
	public GradesSitesTrustsService(GradeRepository gradeRepository, SiteRepository siteRepository,
	                                TrustRepository trustRepository, @Value("${search.result.limit:100}") int limit) {
		this.gradeRepository = gradeRepository;
		this.siteRepository = siteRepository;
		this.trustRepository = trustRepository;
		this.limit = limit;
	}

	/**
	 * Returns all grades
	 * @return List of {@link Grade}
	 */
	public List<Grade> getAllGrades() {
		return gradeRepository.findAll();
	}

	/**
	 * Finds a grade given it's code
	 * @param gradeCode the grade code - NOT NULL
	 * @return the grade if found, null otherwise
	 */
	public Grade getGradeByCode(String gradeCode) {
		checkNotNull(gradeCode);
		return gradeRepository.findOne(gradeCode);
	}

	/**
	 * Searches all sites who have data containing given searchString
	 * @param searchString search string to be searched for site data
	 * @return List of {@link Site} matching searchString
	 */
	public List<Site> searchSites(String searchString) {
		if(!isEmpty(searchString)) {
			return siteRepository.findBySearchString(searchString, new PageRequest(0, limit));
		} else {
			return siteRepository.findAll();
		}
	}

	/**
	 * Finds a grade given it's code
	 * @param siteCode the site code - NOT NULL
	 * @return the site if found, null otherwise
	 */
	public Site getSiteByCode(String siteCode) {
		checkNotNull(siteCode);
		return siteRepository.findOne(siteCode);
	}

	/**
	 *
	 * @param trustCode Trust code for a trust inside which the sites have to be searched
	 * @param searchString search string to be searched for site data
	 * @return List of {@link Site} matching searchString for given trustCode
	 */
	public List<Site> searchSitesWithinTrust(String trustCode, String searchString) {
		if(!isEmpty(searchString)) {
			return siteRepository.findBySearchStringAndTrustCode(trustCode, searchString, new PageRequest(0, limit));
		} else {
			return siteRepository.findByTrustCode(trustCode, new PageRequest(0, limit));
		}
	}

	/**
	 * Returns a trust with given code
	 * @param trustCode Code for a trust - NOT NULL
	 * @return {@link Trust} if found, null otherwise
	 */
	public Trust getTrustByCode(String trustCode) {
		checkNotNull(trustCode);
		return trustRepository.findOne(trustCode);
	}

	/**
	 * Searches all trusts who have data containing given searchString
	 * @param searchString search string to be searched for trust data
	 * @return List of {@link Trust} matching searchString
	 */
	public List<Trust> searchTrusts(String searchString) {
		if(!isEmpty(searchString)) {
			return trustRepository.findBySearchString(searchString, new PageRequest(0, limit));
		} else {
			return trustRepository.findAll();
		}
	}
}
