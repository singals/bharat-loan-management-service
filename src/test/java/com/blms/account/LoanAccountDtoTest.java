package com.blms.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blms.account.loan.LoanAccountDto;
import com.blms.customer.Customer;
import com.blms.testutils.TestUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanAccountDtoTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  @Test
  void testJsonParsing() throws JsonProcessingException {
    String json = "{\"customer_id\":\"aa7cf47a-7b46-4aff-816e-0f9c43d563ef\"}";
    LoanAccountDto loanAccountDto = mapper.readValue(json, LoanAccountDto.class);

    assertEquals("aa7cf47a-7b46-4aff-816e-0f9c43d563ef", loanAccountDto.getCustomerId());
  }

  @Test
  void testMappingFromAccount() throws JsonProcessingException {
    Customer customer = TestUtils.getCustomer();
    customer.setId(null);
    LoanAccountDto account = LoanAccountDto.builder().number(7l).build();


    String json = mapper.writeValueAsString(account);
    assertEquals(
        "{\"number\":7}",
        json);
  }
}
