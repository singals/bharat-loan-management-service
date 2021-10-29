package com.blms.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blms.account.loan.LoanAccount;
import com.blms.account.loan.LoanAccountDAO;
import com.blms.account.loan.LoanAccountDto;
import com.blms.account.loan.LoanAccountResource;
import com.blms.customer.Customer;
import com.blms.customer.CustomerDAO;
import com.blms.testutils.TestUtils;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class LoanAccountResourceTest {

  private static final CustomerDAO CUSTOMER_DAO = mock(CustomerDAO.class);
  private static final LoanAccountDAO LOAN_ACCOUNT_DAO = mock(LoanAccountDAO.class);
  public static final ResourceExtension RESOURCES =
      ResourceExtension.builder()
          .addResource(new LoanAccountResource(LOAN_ACCOUNT_DAO, CUSTOMER_DAO))
          .build();

  @AfterEach
  void tearDown() {
    reset(CUSTOMER_DAO);
    reset(LOAN_ACCOUNT_DAO);
  }

  @Test
  void testCreateAccount() {
    Customer customer = TestUtils.getCustomer();
    LoanAccountDto suppliedLoanAccountDto = TestUtils.getAccountDto();
    suppliedLoanAccountDto.setId(UUID.randomUUID().toString());
    suppliedLoanAccountDto.setCustomerId(customer.getId().toString());
    suppliedLoanAccountDto.setCreatedAt(
        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    LoanAccount loanAccount = LoanAccount.from(suppliedLoanAccountDto);
    when(LOAN_ACCOUNT_DAO.create(eq(loanAccount))).thenReturn(loanAccount);
    when(CUSTOMER_DAO.getById(loanAccount.getCustomerId())).thenReturn(Optional.of(customer));
    when(LOAN_ACCOUNT_DAO.getById(eq(loanAccount.getId()))).thenReturn(Optional.of(loanAccount));
    Response response =
        RESOURCES
            .target("/loan-accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedLoanAccountDto, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(Response.Status.OK, response.getStatusInfo());
    LoanAccountDto actualLoanAccountDto = response.readEntity(LoanAccountDto.class);
    assertNotNull(actualLoanAccountDto.getId());
    suppliedLoanAccountDto.setId(actualLoanAccountDto.getId());
    suppliedLoanAccountDto.setCreatedAt(actualLoanAccountDto.getCreatedAt());
    assertEquals(suppliedLoanAccountDto, actualLoanAccountDto);
    verify(LOAN_ACCOUNT_DAO).create(eq(loanAccount));
    verify(CUSTOMER_DAO).getById(eq(loanAccount.getCustomerId()));
    verify(LOAN_ACCOUNT_DAO).getById(eq(loanAccount.getId()));
  }

  @Test
  void testCreateAccountWhenDaoThrowsError() {
    Customer customer = TestUtils.getCustomer();
    LoanAccountDto suppliedLoanAccountDto = TestUtils.getAccountDto();
    suppliedLoanAccountDto.setId(UUID.randomUUID().toString());
    suppliedLoanAccountDto.setCustomerId(customer.getId().toString());
    LoanAccount loanAccount = LoanAccount.from(suppliedLoanAccountDto);
    when(CUSTOMER_DAO.getById(loanAccount.getCustomerId())).thenReturn(Optional.of(customer));
    doThrow(RuntimeException.class).when(LOAN_ACCOUNT_DAO).create(eq(loanAccount));

    Response response =
        RESOURCES
            .target("/loan-accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedLoanAccountDto, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    verify(CUSTOMER_DAO).getById(eq(loanAccount.getCustomerId()));
  }

  @Test
  void testCreateAccountWhenCustomerAccountIsNotActive() {
    Customer customer = TestUtils.getCustomer();
    customer.setIsActive(false);
    LoanAccountDto suppliedLoanAccountDto = TestUtils.getAccountDto();
    suppliedLoanAccountDto.setId(UUID.randomUUID().toString());
    suppliedLoanAccountDto.setCustomerId(customer.getId().toString());
    LoanAccount loanAccount = LoanAccount.from(suppliedLoanAccountDto);
    when(CUSTOMER_DAO.getById(loanAccount.getCustomerId())).thenReturn(Optional.of(customer));

    Response response =
        RESOURCES
            .target("/loan-accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedLoanAccountDto, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Status.PRECONDITION_FAILED, response.getStatusInfo());
  }

  @Test
  void testCreateAccountWhenCustomerAccountIsBlacklisted() {
    Customer customer = TestUtils.getCustomer();
    customer.setIsBlacklisted(true);
    LoanAccountDto suppliedLoanAccountDto = TestUtils.getAccountDto();
    suppliedLoanAccountDto.setId(UUID.randomUUID().toString());
    suppliedLoanAccountDto.setCustomerId(customer.getId().toString());
    LoanAccount loanAccount = LoanAccount.from(suppliedLoanAccountDto);
    when(CUSTOMER_DAO.getById(loanAccount.getCustomerId())).thenReturn(Optional.of(customer));

    Response response =
        RESOURCES
            .target("/loan-accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedLoanAccountDto, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Status.PRECONDITION_FAILED, response.getStatusInfo());
  }
}
