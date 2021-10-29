package com.blms.account.loan;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.SessionFactory;

public class LoanAccountDAO extends AbstractDAO<LoanAccount> {

  public LoanAccountDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public LoanAccount create(LoanAccount loanAccount) {
    return persist(loanAccount);
  }

  public Optional<LoanAccount> getById(UUID id) {
    return Optional.ofNullable(get(id));
  }

  public List<LoanAccount> getAll() {
    return list(namedTypedQuery("LoanAccount.findAll"));
  }
}
