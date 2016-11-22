package com.transformuk.hee.tis.reference.api;

import com.transformuk.hee.tis.reference.service.GradesSitesTrustsService;
import com.transformuk.hee.tis.reference.model.Grade;
import com.transformuk.hee.tis.reference.model.LimitedListResponse;
import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Offers Search and list on grades, sites and trusts
 */
@RestController
@Api(value = "/api", description = "Search and list on grades, sites and trusts")
@RequestMapping("/api")
public class GradesSitesTrustsController {
	private GradesSitesTrustsService gradesSitesTrustsService;

	private final int limit;

	public GradesSitesTrustsController(GradesSitesTrustsService gradesSitesTrustsService,
	                                   @Value("${search.result.limit:100}") int limit) {
		this.gradesSitesTrustsService = gradesSitesTrustsService;
		this.limit = limit;
	}

	@ApiOperation(value = "getAllGrades()",
			notes = "Returns a list of grades",
			response = List.class, responseContainer = "Grades List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grades list", response = List.class)})
	@RequestMapping(method = GET, value = "/grades")
	public List<Grade> getAllGrades() throws Exception {
		return gradesSitesTrustsService.getAllGrades();
	}

	@ApiOperation(value = "getGradeByCode()",
			notes = "Returns a grade given it's code",
			response = Grade.class, responseContainer = "Grade")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grade", response = Grade.class)})
	@RequestMapping(method = GET, value = "/grades/{gradeCode}")
	public Grade getGradeByCode(@PathVariable(value = "gradeCode") String gradeCode) {
		return gradesSitesTrustsService.getGradeByCode(gradeCode);
	}

	@ApiOperation(value = "searchSites()",
			notes = "Returns a list of sites matching given search string",
			response = List.class, responseContainer = "Sites List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sites list", response = LimitedListResponse.class)})
	@RequestMapping(method = GET, value = "/sites")
	public LimitedListResponse<Site> searchSites(
			@RequestParam(value = "searchString", required = false) String searchString) throws Exception {
		return new LimitedListResponse<>(gradesSitesTrustsService.searchSites(searchString), limit);
	}

	@ApiOperation(value = "getSiteByCode()",
			notes = "Returns a site given it's code",
			response = Grade.class, responseContainer = "Site")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Site", response = Site.class)})
	@RequestMapping(method = GET, value = "/sites/{siteCode}")
	public Site getSiteByCode(@PathVariable(value = "siteCode") String siteCode) {
		return gradesSitesTrustsService.getSiteByCode(siteCode);
	}

	@ApiOperation(value = "searchSitesWithinATrustCode()",
			notes = "Returns a list of sites with a given trust code and given search string",
			response = List.class, responseContainer = "Sites List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sites list", response = LimitedListResponse.class)})
	@RequestMapping(method = GET, value = "/sites/search-by-trust/{trustCode}")
	public LimitedListResponse<Site> searchSitesWithinATrustCode(@PathVariable(value = "trustCode") String trustCode,
												  @RequestParam(value = "searchString", required = false)
														  String searchString) throws Exception {
		return new LimitedListResponse<> (
				gradesSitesTrustsService.searchSitesWithinTrust(trustCode, searchString), limit);
	}

	@ApiOperation(value = "getTrustByCode()",
			notes = "Returns trust with a given trust code",
			response = Trust.class, responseContainer = "Trust")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Trust", response = Trust.class)})
	@RequestMapping(method = GET, value = "/trusts/{trustCode}")
	public Trust getTrustByCode(@PathVariable(value = "trustCode") String trustCode) throws Exception {
		return gradesSitesTrustsService.getTrustByCode(trustCode);
	}

	@ApiOperation(value = "searchTrusts()",
			notes = "Returns a list of trusts matching given search string",
			response = List.class, responseContainer = "Trusts List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Trusts list", response = LimitedListResponse.class)})
	@RequestMapping(method = GET, value = "/trusts")
	public LimitedListResponse<Trust> searchTrusts(@RequestParam(value = "searchString") String searchString)
			throws Exception {
		return new LimitedListResponse<>(gradesSitesTrustsService.searchTrusts(searchString), limit);
	}

}
