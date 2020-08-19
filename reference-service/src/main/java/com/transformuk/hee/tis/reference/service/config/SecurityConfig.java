package com.transformuk.hee.tis.reference.service.config;

import com.transformuk.hee.tis.security.UserPermissionEvaluator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

  @Autowired
  MutableAclService mutableAclService;

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(permissionEvaluator());
    expressionHandler
        .setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(mutableAclService));
    return expressionHandler;
  }

  @Bean
  public PermissionEvaluator permissionEvaluator() {
    List<PermissionEvaluator> evaluators = new ArrayList<>();
    evaluators.add(new AclPermissionEvaluator(mutableAclService));
    evaluators.add(new UserPermissionEvaluator());

    return new EvaluatorsAggregator(evaluators);
  }
}
