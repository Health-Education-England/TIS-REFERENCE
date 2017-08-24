package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ReferenceService extends ClientService {

  ResponseEntity<DBCDTO> getDBCByCode(String code);
  HttpStatus getTrustByCodeHttpStatus(String trustCode);
  HttpStatus getSiteByCodeHttpStatus(String siteCode);
}

