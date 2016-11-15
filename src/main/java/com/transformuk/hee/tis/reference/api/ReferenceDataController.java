package com.transformuk.hee.tis.reference.api;

import com.transformuk.hee.tis.reference.ReferenceDataService;
import com.transformuk.hee.tis.reference.model.Grade;
import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Offers the basic operations on the concerns table, including listing
 */
@RestController
@Api(value = "/api/cd", description = "Basic operations on the concerns list, add, modify and list")
@RequestMapping("/api/cd")
public class ReferenceDataController {
	private ReferenceDataService referenceDataService;

	public ReferenceDataController(ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

	@ApiOperation(value = "getAllGrades()",
			notes = "Returns a list of grades",
			response = List.class, responseContainer = "Grades List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grades list", response = List.class)})
	@CrossOrigin
	@RequestMapping(method = GET, value = "/grades")
	public List<Grade> getAllGrades() throws Exception {
		return referenceDataService.getAllGrades();
	}

	@ApiOperation(value = "searchSites()",
			notes = "Returns a list of sites matching given search string",
			response = List.class, responseContainer = "Sites List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sites list", response = List.class)})
	@CrossOrigin
	@RequestMapping(method = GET, value = "/sites")
	public List<Site> searchSites(@RequestParam(value = "searchString", required = false) String searchString) throws Exception {
		return referenceDataService.searchSites(searchString);
	}

	@ApiOperation(value = "searchSitesWithinATrustCode()",
			notes = "Returns a list of sites with a given trust code and given search string",
			response = List.class, responseContainer = "Sites List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sites list", response = List.class)})
	@CrossOrigin
	@RequestMapping(method = GET, value = "/sites/{trustCode}")
	public List<Site> searchSitesWithinATrustCode(@PathVariable(value = "trustCode") String trustCode,
												  @RequestParam(value = "searchString", required = false)
														  String searchString) throws Exception {
		return referenceDataService.searchSitesWithinTrust(trustCode, searchString);
	}

	@ApiOperation(value = "getTrust()",
			notes = "Returns trust with a given trust code",
			response = Trust.class, responseContainer = "Trust")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Trust", response = Trust.class)})
	@CrossOrigin
	@RequestMapping(method = GET, value = "/trusts/{trustCode}")
	public Trust getTrust(@PathVariable(value = "trustCode") String trustCode) throws Exception {
		return referenceDataService.getTrustWithCode(trustCode);
	}

	@ApiOperation(value = "searchTrusts()",
			notes = "Returns a list of trusts matching given search string",
			response = List.class, responseContainer = "Trusts List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Trusts list", response = List.class)})
	@CrossOrigin
	@RequestMapping(method = GET, value = "/trusts")
	public List<Trust> searchTrusts(@RequestParam(value = "searchString") String searchString) throws Exception {
		return referenceDataService.searchTrusts(searchString);
	}
}
