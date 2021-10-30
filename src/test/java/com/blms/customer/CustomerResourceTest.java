package com.blms.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blms.loan.account.LoanAccount;
import com.blms.loan.account.LoanAccountDto;
import com.blms.testutils.TestUtils;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class CustomerResourceTest {
  private static final CustomerDAO CUSTOMER_DAO = mock(CustomerDAO.class);
  public static final ResourceExtension RESOURCES =
      ResourceExtension.builder().addResource(new CustomerResource(CUSTOMER_DAO)).build();

  @AfterEach
  void tearDown() {
    reset(CUSTOMER_DAO);
  }

  @Test
  void testCreateCustomer() {
    CustomerDto suppliedCustomerDto = TestUtils.getCustomerDto();
    suppliedCustomerDto.setId(UUID.randomUUID().toString());
    Customer customer = Customer.from(suppliedCustomerDto);
    when(CUSTOMER_DAO.create(eq(customer))).thenReturn(customer);
    Response response =
        RESOURCES
            .target("/customers")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedCustomerDto, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(Response.Status.OK, response.getStatusInfo());
    CustomerDto actualCustomerDto = response.readEntity(CustomerDto.class);
    assertNotNull(actualCustomerDto.getId());
    suppliedCustomerDto.setId(actualCustomerDto.getId());
    assertEquals(suppliedCustomerDto, actualCustomerDto);
    verify(CUSTOMER_DAO).create(eq(customer));
  }

  @Test
  void testCreateCustomerWhenErrorFromDAO() {
    CustomerDto suppliedCustomerDto = TestUtils.getCustomerDto();
    suppliedCustomerDto.setId(UUID.randomUUID().toString());
    Customer customer = Customer.from(suppliedCustomerDto);
    doThrow(RuntimeException.class).when(CUSTOMER_DAO).create(eq(customer));
    Response response =
        RESOURCES
            .target("/customers")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(suppliedCustomerDto, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    verify(CUSTOMER_DAO).create(eq(customer));
  }

  @Test
  void testGetAllCustomers() {
    Customer customer = TestUtils.getCustomer();
    LoanAccountDto loanAccountDto = TestUtils.getAccountDto();
    customer.setLoanAccounts(Arrays.asList(LoanAccount.from(loanAccountDto)));
    Customer blacklistedCustomer = TestUtils.getBlacklistedCustomer();
    when(CUSTOMER_DAO.getAll()).thenReturn(Arrays.asList(customer, blacklistedCustomer));
    Response response =
        RESOURCES.target("/customers").request(MediaType.APPLICATION_JSON_TYPE).get();
    assertEquals(Response.Status.OK, response.getStatusInfo());
    List<CustomerDto> customerDtos = response.readEntity(new GenericType<>() {});
    assertEquals(2, customerDtos.size());
    assertEquals(CustomerDto.from(customer), customerDtos.get(0));
    assertEquals(CustomerDto.from(blacklistedCustomer), customerDtos.get(1));
    verify(CUSTOMER_DAO).getAll();
  }

  @Test
  void testGetAllCustomersWhenErrorFromDAO() {
    doThrow(RuntimeException.class).when(CUSTOMER_DAO).getAll();
    Response response =
        RESOURCES.target("/customers").request(MediaType.APPLICATION_JSON_TYPE).get();
    assertEquals(Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    verify(CUSTOMER_DAO).getAll();
  }
}
