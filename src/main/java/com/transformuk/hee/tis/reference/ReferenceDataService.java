package com.transformuk.hee.tis.reference;

import com.transformuk.hee.tis.reference.model.Grade;
import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import com.transformuk.hee.tis.reference.repository.GradeRepository;
import com.transformuk.hee.tis.reference.repository.SiteRepository;
import com.transformuk.hee.tis.reference.repository.TrustRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Service to fetch reference data like grades, sites, trust
 */
@Service
public class ReferenceDataService {
	private GradeRepository gradeRepository;
	private SiteRepository siteRepository;
	private TrustRepository trustRepository;

	@Autowired
	public ReferenceDataService(GradeRepository gradeRepository, SiteRepository siteRepository, TrustRepository trustRepository) {
		this.gradeRepository = gradeRepository;
		this.siteRepository = siteRepository;
		this.trustRepository = trustRepository;
	}

	/**
	 * Returns all grades
	 * @return List of {@link Grade}
	 */
	public List<Grade> getAllGrades() {
		return gradeRepository.findAll();
	}

	/**
	 * Searches all sites who have data containing given searchString
	 * @param searchString search string to be searched for site data
	 * @return List of {@link Site} matching searchString
	 */
	public List<Site> searchSites(String searchString) {
		if(!isEmpty(searchString)) {
			return siteRepository.findBySearchString(searchString);
		} else {
			return siteRepository.findAll();
		}
	}

	/**
	 *
	 * @param trustCode Trust code for a trust inside which the sites have to be searched
	 * @param searchString search string to be searched for site data
	 * @return List of {@link Site} matching searchString for given trustCode
	 */
	public List<Site> searchSitesWithinTrust(String trustCode, String searchString) {
		if(!isEmpty(searchString)) {
			return siteRepository.findBySearchStringAndTrustCode(trustCode, searchString);
		} else {
			return siteRepository.findByTrustCode(trustCode);
		}
	}

	/**
	 * Returns a trust with given code
	 * @param trustCode Code for a trust
	 * @return {@link Trust}
	 */
	public Trust getTrustWithCode(String trustCode) {
		return trustRepository.findOne(trustCode);
	}

	/**
	 * Searches all trusts who have data containing given searchString
	 * @param searchString search string to be searched for trust data
	 * @return List of {@link Trust} matching searchString
	 */
	public List<Trust> searchTrusts(String searchString) {
		if(!isEmpty(searchString)) {
			return trustRepository.findBySearchString(searchString);
		} else {
			return trustRepository.findAll();
		}
	}
}
