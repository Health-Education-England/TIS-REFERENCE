package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ReferenceService extends ClientService {

  ResponseEntity<DBCDTO> getDBCByCode(String code);

  HttpStatus siteTrustMatch(String siteCode, String trustCode);

  Map<Long, Boolean> gradeExists(List<Long> Ids);

  Map<Long, Boolean> trustExists(List<Long> Ids);

  Map<Long, Boolean> siteExists(List<Long> Ids);

  HttpStatus trustCodeExists(String Code);

  HttpStatus siteCodeExists(String Code);

  Map<String, Boolean> medicalSchoolsExists(List<String> values);

  Map<String, Boolean> countryExists(List<String> values);

  Map<String, Boolean> rotationExists(List<String> values);
}
