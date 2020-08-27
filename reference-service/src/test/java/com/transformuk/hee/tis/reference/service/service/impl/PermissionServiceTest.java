package com.transformuk.hee.tis.reference.service.service.impl;

import static org.junit.Assert.assertEquals;

import com.transformuk.hee.tis.reference.service.api.TestUtil;
import java.util.Set;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

  private static final String TEST_USER = "test user";

  @InjectMocks
  private PermissionService permissionService;

  @Test
  public void canGetUserEntities() {
    Set<String> roleSet = Sets.newLinkedHashSet("HEE", "NI");
    TestUtil.mockUserProfileWithRole(TEST_USER, roleSet);
    Set<String> entities = permissionService.getUserEntities();
    assertEquals(roleSet, entities);
  }
}
