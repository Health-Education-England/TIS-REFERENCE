package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.util.collections.Sets.newSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transformuk.hee.tis.security.model.AuthenticatedUser;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for testing REST controllers.
 */
public class TestUtil {

  public static void mockUserProfile(String userName, String... designatedBodyCodes) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setDesignatedBodyCodes(newSet(designatedBodyCodes));
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile,
        null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert
   * @return the JSON byte array
   * @throws IOException
   */
  public static byte[] convertObjectToJsonBytes(Object object)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);

    return mapper.writeValueAsBytes(object);
  }

  /**
   * Convert an object to JSON string
   *
   * @param object
   * @return the JSON String
   * @throws IOException
   */
  public static String convertObjectToJson(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }

  /**
   * Create a byte array with a specific size filled with specified data.
   *
   * @param size the size of the byte array
   * @param data the data to put in the byte array
   * @return the JSON byte array
   */
  public static byte[] createByteArray(int size, String data) {
    byte[] byteArray = new byte[size];
    for (int i = 0; i < size; i++) {
      byteArray[i] = Byte.parseByte(data, 2);
    }
    return byteArray;
  }

  /**
   * Creates a matcher that matches when the examined string reprensents the same instant as the
   * reference datetime
   *
   * @param date the reference datetime against which the examined string is checked
   */
  public static ZonedDateTimeMatcher sameInstant(ZonedDateTime date) {
    return new ZonedDateTimeMatcher(date);
  }

  /**
   * Verifies the equals/hashcode contract on the model object.
   */
  public static void equalsVerifier(Class clazz) throws Exception {
    Object domainObject1 = clazz.getConstructor().newInstance();
    assertThat(domainObject1.toString()).isNotNull();
    assertThat(domainObject1).isEqualTo(domainObject1);
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
    // Test with an instance of another class
    Object testOtherObject = new Object();
    assertThat(domainObject1).isNotEqualTo(testOtherObject);
    // Test with an instance of the same class
    Object domainObject2 = clazz.getConstructor().newInstance();
    assertThat(domainObject1).isNotEqualTo(domainObject2);
    // HashCodes are equals because the objects are not persisted yet
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode());
  }

  /**
   * A matcher that tests that the examined string represents the same instant as the reference
   * datetime.
   */
  public static class ZonedDateTimeMatcher extends TypeSafeDiagnosingMatcher<String> {

    private final ZonedDateTime date;

    public ZonedDateTimeMatcher(ZonedDateTime date) {
      this.date = date;
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
      try {
        if (!date.isEqual(ZonedDateTime.parse(item))) {
          mismatchDescription.appendText("was ").appendValue(item);
          return false;
        }
        return true;
      } catch (DateTimeParseException e) {
        mismatchDescription.appendText("was ").appendValue(item)
            .appendText(", which could not be parsed as a ZonedDateTime");
        return false;
      }

    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a String representing the same Instant as ").appendValue(date);
    }
  }
}
