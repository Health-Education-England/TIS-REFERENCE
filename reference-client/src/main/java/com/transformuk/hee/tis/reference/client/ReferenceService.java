package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.client.ClientService;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.LimitedListResponse;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.Map;

public interface ReferenceService extends ClientService {

  ResponseEntity<DBCDTO> getDBCByCode(String code);
 // HttpStatus getTrustByCodeHttpStatus(String trustCode);
  //HttpStatus getSiteByCodeHttpStatus(String siteCode);
 // LimitedListResponse<SiteDTO> getSitesByTrustCode(String trustCode);
  //ResponseEntity<String> getTrustCodeBySiteCode (String code);

  HttpStatus siteTrustMatch (String siteCode, String trustCode);
  Map<Long,Boolean> gradeExists(List<Long> Ids);
  Map<Long,Boolean> trustExists(List<Long> Ids);
  Map<Long,Boolean> siteExists(List<Long> Ids);
  HttpStatus trustCodeExists(String Code);
  HttpStatus siteCodeExists(String Code);
}
