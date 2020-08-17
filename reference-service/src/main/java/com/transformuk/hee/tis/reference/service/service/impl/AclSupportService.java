package com.transformuk.hee.tis.reference.service.service.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.AuditableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;

@Service
public class AclSupportService {

  private final MutableAclService aclService;

//  private ConsoleAuditLogger aclAuditLogger = new ConsoleAuditLogger();

  public AclSupportService(MutableAclService aclService) {
    this.aclService = aclService;
  }

  public AuditableAcl grantPermissionsToUser(String type, Long id, Set<String> principals,
      Set<Permission> permissions) {
    Set<Sid> sids = principals.stream().map(p -> new GrantedAuthoritySid(p))
        .collect(Collectors.toSet());
    return grantPermissions(type, id, sids, permissions);
  }

  private AuditableAcl grantPermissions(String type, Long id, Set<Sid> sids,
      Set<Permission> permissions) {
    AuditableAcl acl = getOrCreate(type, id);
//    acl.setOwner(new GrantedAuthoritySid("Machine User"));
    Set<Integer> indices = new HashSet<>();
    for (Permission permission : permissions) {
      for (Sid sid : sids) {
        int index = acl.getEntries().size();
        boolean granting = true;
        acl.setOwner(sid);
        acl.insertAce(index, permission, sid, granting);
        indices.add(index);
      }
    }

    for (Integer index : indices) {
      acl.updateAuditing(index, true, true);
    }

    acl = (AuditableAcl) aclService.updateAcl(acl);

    return acl;
  }

  private AuditableAcl getOrCreate(String type, Long id) {
    AuditableAcl acl = get(type, id);
    if (acl == null) {
      acl = create(type, id);
    }
    return acl;
  }

  private AuditableAcl create(String type, Long id) {
    AuditableAcl acl = (AuditableAcl) aclService.createAcl(new ObjectIdentityImpl(type, id));
    return acl;
  }

  private AuditableAcl get(String type, Serializable id) {
    try {
      return (AuditableAcl) aclService.readAclById(new ObjectIdentityImpl(type, id));
    } catch (NotFoundException exception) {
      return null;
    }
  }
}