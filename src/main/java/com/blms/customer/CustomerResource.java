package com.blms.customer;

import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
  @Consumes(MediaType.APPLICATION_JSON)
  public CustomerDto createCustomer(@Valid CustomerDto customerDto) {
    Customer customer = Customer.from(customerDto);
    customerDAO.create(customer);
    return CustomerDto.from(customer);
  }

  @GET
  @UnitOfWork
  @Produces(MediaType.APPLICATION_JSON)
  public List<CustomerDto> getAllCustomers() {
    List<Customer> customers = customerDAO.getAll();
    return customers.stream().map(CustomerDto::from).collect(Collectors.toList());
  }
}
