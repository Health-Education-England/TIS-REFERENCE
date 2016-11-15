package com.transformuk.hee.tis.reference.api;


import com.transformuk.hee.tis.security.model.AuthenticatedUser;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestUtils {

	private static final String DBC = "1-85KJU0";

	public static void mockUserprofile(String userName, String designatedBodyCode) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserName(userName);
		userProfile.setDesignatedBodyCode(designatedBodyCode);
		AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile, null);
		UsernamePasswordAuthenticationToken authenticationToken = new
				UsernamePasswordAuthenticationToken(authenticatedUser, null);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	public static UserProfile mockWithPermissions(String userName,  String...permissions) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserName(userName);
		userProfile.setDesignatedBodyCode(DBC);
		Set<String> permSet = new HashSet<>(Arrays.asList(permissions));
		userProfile.setPermissions(permSet);
		return userProfile;
	}

}
