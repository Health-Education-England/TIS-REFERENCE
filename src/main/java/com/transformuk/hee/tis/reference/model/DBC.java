package com.transformuk.hee.tis.reference.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A designated body code
 */
@ApiModel(description = "Designated body code data")
@Entity
@Table(name = "DBC")
public class DBC {

	@Id
	private String dbc;
	private String name;
	private String abbr;

	public String getDbc() {
		return dbc;
	}

	public void setDbc(String dbc) {
		this.dbc = dbc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
}
