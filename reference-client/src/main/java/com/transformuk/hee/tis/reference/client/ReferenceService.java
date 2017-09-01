package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ReferenceService extends ClientService {

  ResponseEntity<DBCDTO> getDBCByCode(String code);

  Map<Long,Boolean> gradeExists(List<Long> Ids);

  Map<Long,Boolean> siteExists(List<Long> Ids);
}
