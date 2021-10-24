package com.blms.customer;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class CustomerDAO extends AbstractDAO<Customer> {

  public CustomerDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public Customer create(Customer customer) {
    return persist(customer);
  }

  public List<Customer> getAll() {
    return list(namedTypedQuery("Customer.findAll"));
  }
}
