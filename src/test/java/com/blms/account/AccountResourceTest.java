package com.blms.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blms.customer.CustomerDAO;
import com.blms.testutils.TestUtils;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
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
class AccountResourceTest {

  private static final CustomerDAO CUSTOMER_DAO = mock(CustomerDAO.class);
  private static final AccountDAO ACCOUNT_DAO = mock(AccountDAO.class);
  public static final ResourceExtension RESOURCES =
      ResourceExtension.builder()
          .addResource(new AccountResource(ACCOUNT_DAO, CUSTOMER_DAO))
          .build();

  @AfterEach
  void tearDown() {
    reset(CUSTOMER_DAO);
    reset(ACCOUNT_DAO);
  }

  @Test
  void testCreateAccount() {
    AccountDto suppliedAccountDto = TestUtils.getAccountDto();
    suppliedAccountDto.setId(UUID.randomUUID().toString());
    Account account = Account.from(suppliedAccountDto);
    when(ACCOUNT_DAO.create(eq(account))).thenReturn(account);
    when(CUSTOMER_DAO.getById(account.getCustomer().getId()))
        .thenReturn(Optional.of(account.getCustomer()));
    when(ACCOUNT_DAO.getById(eq(account.getId()))).thenReturn(Optional.of(account));
    Response response =
        RESOURCES
            .target("/accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedAccountDto, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(Response.Status.OK, response.getStatusInfo());
    AccountDto actualAccountDto = response.readEntity(AccountDto.class);
    assertNotNull(actualAccountDto.getId());
    suppliedAccountDto.setId(actualAccountDto.getId());
    assertEquals(suppliedAccountDto, actualAccountDto);
    verify(ACCOUNT_DAO).create(eq(account));
    verify(CUSTOMER_DAO).getById(eq(account.getCustomer().getId()));
    verify(ACCOUNT_DAO).getById(eq(account.getId()));
  }

  @Test
  void testCreateAccountWhenDaoThrowsError() {
    AccountDto suppliedAccountDto = TestUtils.getAccountDto();
    suppliedAccountDto.setId(UUID.randomUUID().toString());
    Account account = Account.from(suppliedAccountDto);
    when(CUSTOMER_DAO.getById(account.getCustomer().getId()))
        .thenReturn(Optional.of(account.getCustomer()));
    doThrow(RuntimeException.class).when(ACCOUNT_DAO).create(eq(account));

    Response response =
        RESOURCES
            .target("/accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedAccountDto, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    verify(CUSTOMER_DAO).getById(eq(account.getCustomer().getId()));
  }

  @Test
  void testCreateAccountWhenCustomerAccountIsNotActive() {
    AccountDto suppliedAccountDto = TestUtils.getAccountDto();
    suppliedAccountDto.getHolder().setIsActive(false);
    suppliedAccountDto.setId(UUID.randomUUID().toString());

    Response response =
        RESOURCES
            .target("/accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedAccountDto, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Status.PRECONDITION_FAILED, response.getStatusInfo());
  }

  @Test
  void testCreateAccountWhenCustomerAccountIsBlacklisted() {
    AccountDto suppliedAccountDto = TestUtils.getAccountDto();
    suppliedAccountDto.getHolder().setIsBlacklisted(true);
    suppliedAccountDto.setId(UUID.randomUUID().toString());

    Response response =
        RESOURCES
            .target("/accounts")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedAccountDto, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Status.PRECONDITION_FAILED, response.getStatusInfo());
  }
}
