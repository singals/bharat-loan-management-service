package com.blms.customer;

import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/customers")
public class CustomerResource {

  private final CustomerDAO customerDAO;

  public CustomerResource(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
  }

  @POST
  @UnitOfWork
  @Produces(MediaType.APPLICATION_JSON)
  public CustomerDto createCustomer(@Valid CustomerDto customerDto) {
    Customer customer = Customer.from(customerDto);
    customerDAO.create(customer);
    return CustomerDto.from(customer);
  }
}
