package com.blms.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blms.customer.Customer;
import com.blms.testutils.TestUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountDtoTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  @Test
  void testJsonParsing() throws JsonProcessingException {
    String json = "{\"holder\":{\"id\":\"aa7cf47a-7b46-4aff-816e-0f9c43d563ef\"}}";
    AccountDto accountDto = mapper.readValue(json, AccountDto.class);

    assertEquals("aa7cf47a-7b46-4aff-816e-0f9c43d563ef", accountDto.getHolder().getId());
  }

  @Test
  void testMappingFromAccount() throws JsonProcessingException {
    Customer customer = TestUtils.getCustomer();
    customer.setId(null);
    Account account = Account.builder().number(7l).customer(customer).build();

    String json = mapper.writeValueAsString(account);
    assertEquals(
        "{\"number\":7,\"customer\":{\"firstName\":\"firstName\",\"lastName\":\"lastName\",\"middleName\":\"middleName\",\"primaryContactNumber\":\"1234567890\",\"alternateContactNumber\":\"1234567890\",\"permanentAddress\":\"test address, tester\",\"temporaryAddress\":\"test address, tester\",\"linkToFolderWithDocuments\":\"link-to-folder-in-s3-bucket\",\"isActive\":true,\"isBlacklisted\":false}}",
        json);
  }
}
