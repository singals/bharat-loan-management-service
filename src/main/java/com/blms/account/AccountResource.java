package com.blms.account;

import com.blms.customer.Customer;
import com.blms.customer.CustomerDAO;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
            .getById(UUID.fromString(accountDto.getCustomerId()))
            .orElseThrow(() -> new WebApplicationException(Response.status(412).build()));
    if (!customer.getIsActive() || customer.getIsBlacklisted()) {
      throw new WebApplicationException(Response.status(412).build());
    }
    Account account = Account.from(accountDto);
    accountDAO.create(account);
    account = accountDAO.getById(account.getId()).get();
    return AccountDto.from(account);
  }

  @GET
  @UnitOfWork
  @Produces(MediaType.APPLICATION_JSON)
  public List<AccountDto> getAllAccounts() {
    List<Account> accounts = accountDAO.getAll();
    return accounts.stream().map(AccountDto::from).collect(Collectors.toList());
  }
}
