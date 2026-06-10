package com.transformuk.hee.tis.reference.service.config;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.ErrorCode;
import org.flywaydb.core.api.ErrorDetails;
import org.flywaydb.core.api.output.ValidateOutput;
import org.flywaydb.core.api.output.ValidateResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlywayConfigTest {

  @Mock
  Flyway flyway;

  @Test
  void repairThenMigrateStrategy_shouldRepairWhenValidationFailed() {
    ValidateResult stubResult = getStubInvalidResult();
    when(flyway.validateWithResult()).thenReturn(stubResult);

    new FlywayConfig().repairThenMigrateStrategy(true)
        .migrate(flyway);

    verify(flyway).repair();
    verify(flyway).migrate();
  }

  @Test
  void repairThenMigrateStrategy_shouldNotRepairWhenValidated() {
    ValidateResult stubResult = new ValidateResult(null, null, null, true, 0, null,
        Collections.emptyList(), null);
    when(flyway.validateWithResult()).thenReturn(stubResult);

    new FlywayConfig().repairThenMigrateStrategy(true)
        .migrate(flyway);

    verify(flyway).migrate();
    verifyNoMoreInteractions(flyway);
  }

  @Test
  void repairThenMigrateStrategy_shouldNotRepairWhenInactive() {
    new FlywayConfig().repairThenMigrateStrategy(false)
        .migrate(flyway);

    verify(flyway).migrate();
    verifyNoMoreInteractions(flyway);
  }

  private static ValidateResult getStubInvalidResult() {
    List<ValidateOutput> invalidMigrations = List
        .of(new ValidateOutput("VX", "good_vintage", "tenant/david.sql",
            new ErrorDetails(ErrorCode.CHECKSUM_MISMATCH, "No Doctor's fixing this!")));
    return new ValidateResult(null, null, null, false, 0, invalidMigrations,
        Collections.emptyList(), null);
  }
}