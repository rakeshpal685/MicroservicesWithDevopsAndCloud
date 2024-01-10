package com.rakesh.cards.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/* This class is created so that spring framework can use this class to add the created by field
value in the entity*/
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

  /**
   * Returns the current auditor of the application.
   *
   * @return the current auditor.
   */
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("CARDS_MS");
  }
}
