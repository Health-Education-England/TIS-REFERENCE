package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Class that contains utility methods that work on the Principle of the logged in user
 */
@Service
public class PermissionService {

  protected static final String HEE_ENTITY = "HEE";
  protected static final String NI_ENTITY = "NI";

  public Set<String> getUserEntities() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> roles = loggedInUserProfile.getRoles();
    return roles.stream()
        .filter(r -> StringUtils.equals(r, HEE_ENTITY) || StringUtils.equals(r, NI_ENTITY)).collect(
            Collectors.toSet());
  }
}
