package com.blms.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerDtoTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  @Test
  void testJsonParsing() throws JsonProcessingException {
    String json =
        "{\"first_name\": \"Narendra\", \"last_name\": \"Modi\", \"middle_name\": \"Damodardas\","
            + " \"primary_contact_number\": \"+91 9876543210\", \"alternate_contact_number\": "
            + "\"+91 9876543201\", \"permanent_address\": \"Gujarat\", \"temporary_address\": "
            + "\"7, Lok Kalyan Marg\", \"link_to_folder_with_documents\": \"digi-locker\", "
            + "\"is_active\": true, \"is_blacklisted\": false}";
    CustomerDto customerDto = mapper.readValue(json, CustomerDto.class);

    assertEquals("Narendra", customerDto.getFirstName());
    assertEquals("Modi", customerDto.getLastName());
    assertEquals("Damodardas", customerDto.getMiddleName());
    assertEquals("+91 9876543210", customerDto.getPrimaryContactNumber());
    assertEquals("+91 9876543201", customerDto.getAlternateContactNumber());
    assertEquals("Gujarat", customerDto.getPermanentAddress());
    assertEquals("7, Lok Kalyan Marg", customerDto.getTemporaryAddress());
    assertEquals("digi-locker", customerDto.getLinkToFolderWithDocuments());
    assertTrue(customerDto.getIsActive());
    assertFalse(customerDto.getIsBlacklisted());
  }

  @Test
  void testMappingFromCustomer() throws JsonProcessingException {
    Customer customer =
        Customer.builder()
            .firstName("Narendra")
            .lastName("Modi")
            .primaryContactNumber("+91 9876543210")
            .permanentAddress("Gujarat")
            .build();
    String jsonStr = mapper.writeValueAsString(customer);
    assertEquals(
        "{\"firstName\":\"Narendra\",\"lastName\":\"Modi\",\"primaryContactNumber\":\"+91 9876543210\",\"permanentAddress\":\"Gujarat\"}",
        jsonStr);
  }
}
