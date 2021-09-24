package com.transformuk.hee.tis.reference.service.aop.auditing;

import static com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType.createEvent;
import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.flipkart.zjsonpatch.JsonDiff;
import com.transformuk.hee.tis.audit.enumeration.GenericAuditEventType;
import com.transformuk.hee.tis.reference.service.model.JsonPatch;
import com.transformuk.hee.tis.reference.service.repository.JsonPatchRepository;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.http.ResponseEntity;
import uk.nhs.tis.ObjectMapperUtil;

/**
 * Aspect for auditing execution of rest calls Spring components.
 */
@Aspect
public class AuditingAspect {

  private final static String REFERENCE_PREFIX = "reference_";
  private final static String GET_PREFIX = "get";
  private final static String DTO_POSTFIX = "DTO";
  private final static String ETL_USERNAME = "consolidated_etl";
  private final static String ID_KEY = "id";
  private final static Logger LOG = LoggerFactory.getLogger(AuditingAspect.class);

  private final AuditEventRepository auditEventRepository;

  @Autowired
  private JsonPatchRepository rebaseTypeRepository;
  private ConcurrentHashMap<String, String> classToPrimaryKeyMap = new ConcurrentHashMap<>();
  private Map<String, Class> classToIdClass = new ConcurrentHashMap<>();
  private ObjectMapper mapper = ObjectMapperUtil.createMapper();

  public AuditingAspect(AuditEventRepository auditEventRepository) {
    this.auditEventRepository = auditEventRepository;
  }

  @PostConstruct
  public void initClassToPrimaryKeyMap() {
    classToPrimaryKeyMap.put("TrustDTO", "id");
    classToPrimaryKeyMap.put("SiteDTO", "id");
    classToPrimaryKeyMap.put("GradeDTO", "id");
  }

  @PostConstruct
  public void initiClassToIdClass() {
    classToIdClass.put("TrustDTO", Long.class);
    classToIdClass.put("SiteDTO", Long.class);
    classToIdClass.put("GradeDTO", Long.class);
  }

  /**
   * Pointcut that matches all rest call for create method.
   */
  @Pointcut("execution(* com.transformuk.hee.tis.reference.service.api.*.create*(..))")
  public void auditingCreatePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Pointcut that matches all rest call for update method.
   */
  @Pointcut("execution(* com.transformuk.hee.tis.reference.service.api.*.update*(..))")
  public void auditingUpdatePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Pointcut that matches all rest call for delete method.
   */
  @Pointcut("execution(* com.transformuk.hee.tis.reference.service.api.*.delete*(..))")
  public void auditingDeletePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Advice that Audit method before execution check if any modification then its creating a
   * jsonPatch and stored into JsonPatch table
   */
  @Before("execution(* com.transformuk.hee.tis.reference.service.api.*.update*(..))")
  public void auditUpdateBeforeExecution(JoinPoint joinPoint) throws Throwable {
    // TODO: review implementation, accessibility update via reflection flagged as potential issue.
    // store old value to map, wait until the update process
    try {
      UserProfile userPofile = getProfileFromContext();
      if (!userPofile.getUserName().equalsIgnoreCase(ETL_USERNAME)) {
        final Object newValue = joinPoint.getArgs()[0];
        String className = newValue.getClass().getSimpleName();
        String fieldName = classToPrimaryKeyMap.get(className);
        if (StringUtils.isEmpty(fieldName)) {
          fieldName = ID_KEY;
        }
        String entityName = StringUtils
            .left(className, StringUtils.length(className) - StringUtils.length(DTO_POSTFIX));
        if (StringUtils.isNoneEmpty(fieldName)) {
          final Field idField = newValue.getClass().getDeclaredField(fieldName);
          idField.setAccessible(true);
          final Object idFieldValue = idField.get(newValue);
          Object oldValue = null;
          JsonNode newJsonNode = mapper.convertValue(newValue, JsonNode.class);
          JsonNode oldJsonNode = NullNode.getInstance();
          // if the idFieldValue is null means it's new record so don't fetch old value from db
          if (idFieldValue != null) {

            final Method method;
            if (classToIdClass.containsKey(className)) {
              Class idClass = classToIdClass.get(className);
              method = joinPoint.getTarget().getClass()
                  .getDeclaredMethod(GET_PREFIX + entityName, idClass);
            } else {
              method = joinPoint.getTarget().getClass()
                  .getDeclaredMethod(GET_PREFIX + entityName, new Class[]{Long.class});
            }
            final Object responseEntity = method
                .invoke(joinPoint.getTarget(), idField.get(newValue));
            oldValue = ((ResponseEntity) responseEntity).getBody();
            oldJsonNode = mapper.convertValue(oldValue, JsonNode.class);
          }

          JsonNode patch = JsonDiff.asJson(oldJsonNode, newJsonNode);
          JsonPatch rebaseJson = new JsonPatch();
          rebaseJson.setTableDtoName(className);
          rebaseJson.setPatchId(String.valueOf(idFieldValue));
          rebaseJson.setPatch(patch.toString());
          rebaseTypeRepository.save(rebaseJson);
        }
      }
    } catch (Throwable t) {
      LOG.error("An exception was thrown during an AOP method, rethrowing", t);
      throw t;
    }
  }

  /**
   * Advice that Audit methods returning for all create method.
   */
  @AfterReturning(pointcut = "auditingCreatePointcut()")
  public void auditCreateReturning(JoinPoint joinPoint) throws Throwable {
    setAuditEvent(GenericAuditEventType.add, joinPoint);
  }

  /**
   * Advice that Audit methods returning for all update method.
   */
  @AfterReturning(pointcut = "auditingUpdatePointcut()")
  public void auditUpdateReturning(JoinPoint joinPoint) throws Throwable {
    setAuditEvent(GenericAuditEventType.modify, joinPoint);
  }

  /**
   * Advice that Audit methods returning for all delete method.
   */
  @AfterReturning(pointcut = "auditingDeletePointcut()")
  public void auditDeleteReturning(JoinPoint joinPoint) throws Throwable {
    setAuditEvent(GenericAuditEventType.delete, joinPoint);
  }

  private void setAuditEvent(GenericAuditEventType auditEventType, JoinPoint joinPoint)
      throws Throwable {
    try {
      UserProfile userPofile = getProfileFromContext();
      // Audit event
      AuditEvent auditEvent = createEvent(userPofile.getUserName(), REFERENCE_PREFIX,
          joinPoint.getSignature().getName()
          , auditEventType, joinPoint.getArgs());
      auditEventRepository.add(auditEvent);
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

}
