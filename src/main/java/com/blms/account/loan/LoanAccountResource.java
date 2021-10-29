package com.blms.account.loan;

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
import javax.ws.rs.core.Response.Status;

@Path("/loan-accounts")
public class LoanAccountResource {

  private final LoanAccountDAO loanAccountDAO;
  private final CustomerDAO customerDAO;

  public LoanAccountResource(LoanAccountDAO loanAccountDAO, CustomerDAO customerDAO) {
    this.loanAccountDAO = loanAccountDAO;
    this.customerDAO = customerDAO;
  }

  @POST
  @UnitOfWork
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoanAccountDto createAccount(@Valid LoanAccountDto loanAccountDto) {
    Customer customer =
        customerDAO
            .getById(UUID.fromString(loanAccountDto.getCustomerId()))
            .orElseThrow(() -> new WebApplicationException(Status.PRECONDITION_FAILED));
    if (!customer.getIsActive() || customer.getIsBlacklisted()) {
      throw new WebApplicationException(Status.FORBIDDEN);
    }
    LoanAccount loanAccount = LoanAccount.from(loanAccountDto);
    loanAccountDAO.create(loanAccount);
    loanAccount = loanAccountDAO.getById(loanAccount.getId()).get();
    return LoanAccountDto.from(loanAccount);
  }

  @GET
  @UnitOfWork
  @Produces(MediaType.APPLICATION_JSON)
  public List<LoanAccountDto> getAllAccounts() {
    List<LoanAccount> loanAccounts = loanAccountDAO.getAll();
    return loanAccounts.stream().map(LoanAccountDto::from).collect(Collectors.toList());
  }
}
