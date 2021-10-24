package com.blms.account;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.SessionFactory;

public class AccountDAO extends AbstractDAO<Account> {

  public AccountDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public Account create(Account account) {
    return persist(account);
  }

  public Optional<Account> getById(UUID id) {
    return Optional.ofNullable(get(id));
  }
}
