package com.blms.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blms.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CustomerTest {

  private CustomerDto customerDto;
  private Customer customer;

  @AfterEach
  void tearDown() {
    customerDto = null;
    customer = null;
  }

  @Test
  void testCustomerFromDto() {
    customerDto = TestUtils.getCustomerDto();
    customer = Customer.from(customerDto);

    assertEquals(customerDto.getFirstName(), customer.getFirstName());
    assertEquals(customerDto.getLastName(), customer.getLastName());
    assertEquals(customerDto.getMiddleName(), customer.getMiddleName());
    assertEquals(customerDto.getPrimaryContactNumber(), customer.getPrimaryContactNumber());
    assertEquals(customerDto.getAlternateContactNumber(), customer.getAlternateContactNumber());
    assertEquals(customerDto.getPermanentAddress(), customer.getPermanentAddress());
    assertEquals(customerDto.getTemporaryAddress(), customer.getTemporaryAddress());
    assertEquals(
        customerDto.getLinkToFolderWithDocuments(), customer.getLinkToFolderWithDocuments());
    assertEquals(customerDto.getIsActive(), customer.getIsActive());
    assertEquals(customerDto.getIsActive(), customer.getIsActive());
    assertEquals(customerDto.getIsBlacklisted(), customer.getIsBlacklisted());
  }

  @Test
  void testCustomerFromDtoIsActiveIsSet() {
    customerDto = CustomerDto.builder().isActive(false).build();
    customer = Customer.from(customerDto);
    assertFalse(customer.getIsActive());

    customerDto = CustomerDto.builder().isActive(true).build();
    customer = Customer.from(customerDto);
    assertTrue(customer.getIsActive());

    customerDto = CustomerDto.builder().build();
    customer = Customer.from(customerDto);
    assertTrue(customer.getIsActive());
  }

  @Test
  void testCustomerFromDtoIsBlacklistedIsSet() {
    customerDto = CustomerDto.builder().isBlacklisted(true).build();
    customer = Customer.from(customerDto);
    assertTrue(customer.getIsBlacklisted());

    customerDto = CustomerDto.builder().isBlacklisted(false).build();
    customer = Customer.from(customerDto);
    assertFalse(customer.getIsBlacklisted());

    customerDto = CustomerDto.builder().build();
    customer = Customer.from(customerDto);
    assertFalse(customer.getIsBlacklisted());
  }
}
