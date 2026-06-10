package com.transformuk.hee.tis.reference.service.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.output.ValidateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Flyway database migrations.
 */
@Slf4j
@Configuration
public class FlywayConfig {

  /**
   * Override the default Flyway migration strategy, optionally repairing migrations.
   *
   * @param repairEnabled whether to validate and repair before migration
   * @return a FlywayMigrationStrategy that optionally repairs failed migrations
   */
  @Bean
  public FlywayMigrationStrategy repairThenMigrateStrategy(
      @Value("${application.flyway.repair}") boolean repairEnabled) {
    return flyway -> {
      if (repairEnabled) {
        log.warn("Flyway repair is enabled.  Please ensure this is still necessary!");
        ValidateResult result = flyway.validateWithResult();
        if (!result.validationSuccessful) {
          log.warn("Flyway validation failed, repairing before migration.  Validation errors: {}",
              result.getAllErrorMessages());
          flyway.repair();
        }
      }
      flyway.migrate();
    };
  }
}