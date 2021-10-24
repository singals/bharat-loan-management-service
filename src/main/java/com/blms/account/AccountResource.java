package com.blms.account;

import com.blms.customer.Customer;
import com.blms.customer.CustomerDAO;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class AccountResource {

  private final AccountDAO accountDAO;
  private final CustomerDAO customerDAO;

  public AccountResource(AccountDAO accountDAO, CustomerDAO customerDAO) {
    this.accountDAO = accountDAO;
    this.customerDAO = customerDAO;
  }

  @POST
  @UnitOfWork
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public AccountDto createAccount(@Valid AccountDto accountDto) {
    Customer customer =
        customerDAO
            .getById(UUID.fromString(accountDto.getHolder().getId()))
            .orElseThrow(() -> new WebApplicationException(Response.status(412).build()));
    if (!customer.getIsActive() || customer.getIsBlacklisted()) {
      throw new RuntimeException("Can not create account for an inactive/blacklisted customer");
    }
    Account account = Account.from(accountDto);
    accountDAO.create(account);
    account = accountDAO.getById(account.getId()).get();
    account.setCustomer(customer);
    return AccountDto.from(account);
  }
}
