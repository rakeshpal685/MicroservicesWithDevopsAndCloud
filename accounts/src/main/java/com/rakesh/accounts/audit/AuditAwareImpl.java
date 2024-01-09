package com.rakesh.accounts.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/* This class is created so that spring framework can use this class to add the created by field
value in the entity*/
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("ACCOUNTS_MS");
  }
}
