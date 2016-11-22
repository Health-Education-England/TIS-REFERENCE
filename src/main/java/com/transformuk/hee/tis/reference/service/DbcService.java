package com.transformuk.hee.tis.reference.service;

import com.transformuk.hee.tis.reference.model.DBC;
import com.transformuk.hee.tis.reference.repository.DBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Service to fetch designated body codes
 */
@Service
public class DBCService {

	private DBCRepository dbcRepository;

	@Autowired
	public DBCService(DBCRepository dbcRepository) {
		this.dbcRepository = dbcRepository;
	}

	/**
	 * Returns all designated body codes
	 *
	 * @return List of {@link DBC}
	 */
	public List<DBC> getAllDBCs() {
		return dbcRepository.findAll();
	}

	/**
	 * Finds a designated body code entity
	 *
	 * @param dbc the designated body code - NOT NULL
	 * @return the {@link DBC} if found, null otherwise
	 */
	public DBC getDBCByCode(String dbc) {
		checkNotNull(dbc);
		return dbcRepository.findOne(dbc);
	}
}
