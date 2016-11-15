package com.transformuk.hee.tis.reference.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An NHS trust overseeing several {@link Site}
 */
@ApiModel(description = "Trust data")
@Entity
@Table(name = "Trust")
public class Trust {
	@Id
	private String code;
	private String name;

	public Trust() {
	}

	public Trust(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Trust)) return false;
		Trust c = (Trust) o;
		return Objects.equals(code, c.getCode())
				&& Objects.equals(name, c.getName());
	}

	@Override
	public int hashCode() {
		checkNotNull(code);
		return code.hashCode();
	}
}
