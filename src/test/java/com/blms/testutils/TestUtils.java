package com.blms.testutils;

import com.blms.customer.Customer;
import com.blms.customer.CustomerDto;
import java.util.UUID;

public class TestUtils {

  public static CustomerDto getCustomerDto() {
    return CustomerDto.builder()
        .firstName("firstName")
        .lastName("lastName")
        .middleName("middleName")
        .primaryContactNumber("1234567890")
        .alternateContactNumber("1234567890")
        .permanentAddress("test address, tester")
        .temporaryAddress("test address, tester")
        .linkToFolderWithDocuments("link-to-folder-in-s3-bucket")
        .isActive(true)
        .isBlacklisted(false)
        .build();
  }

  public static Customer getCustomer() {
    CustomerDto customerDto = getCustomerDto();
    customerDto.setId(UUID.randomUUID().toString());
    return Customer.from(customerDto);
  }

  public static Customer getBlacklistedCustomer() {
    CustomerDto customerDto = getCustomerDto();
    customerDto.setId(UUID.randomUUID().toString());
    customerDto.setIsBlacklisted(true);
    customerDto.setIsActive(false);
    return Customer.from(customerDto);
  }
}
