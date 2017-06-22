package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.springframework.http.ResponseEntity;

public interface ReferenceService extends ClientService {

	ResponseEntity<DBCDTO> getDBCByCode(String code);
}
