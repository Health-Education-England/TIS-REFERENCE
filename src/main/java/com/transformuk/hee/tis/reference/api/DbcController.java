package com.transformuk.hee.tis.reference.api;

import com.transformuk.hee.tis.reference.model.DBC;
import com.transformuk.hee.tis.reference.service.DbcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Offers Search and list on designated body codes
 */
@RestController
@Api(value = "/api", description = "Search and list on designated boy codes")
@RequestMapping("/api")
public class DbcController {

	private DbcService dbcService;

	public DbcController(DbcService dbcService) {
		this.dbcService = dbcService;
	}

	@ApiOperation(value = "getAllDBCs()",
			notes = "Returns a list of all designated body codes",
			response = List.class, responseContainer = "Designated body codes List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Designated body codes list", response = List.class)})
	@RequestMapping(method = GET, value = "/dbcs")
	public List<DBC> getAllDBCs() throws Exception {
		return dbcService.getAllDBCs();
	}

	@ApiOperation(value = "getDBCByCode()",
			notes = "Returns a designated body code entity given it's code",
			response = DBC.class, responseContainer = "Designated body Code")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Designated body Code", response = DBC.class)})
	@RequestMapping(method = GET, value = "/dbcs/{dbc}")
	public DBC getDBCByCode(@PathVariable(value = "dbc") String dbc) {
		return dbcService.getDBCByCode(dbc);
	}
}
