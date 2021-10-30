package com.blms.loan.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanSimulationRequestDtoTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  @Test
  void testJsonParsing() throws JsonProcessingException {
    String json = "{\"amount\": 100000, \"rate_of_interest\": 2, \"duration_in_months\": 12}";
    LoanSimulationRequestDto loanSimulationRequestDto =
        mapper.readValue(json, LoanSimulationRequestDto.class);
    assertEquals(100000, loanSimulationRequestDto.getAmount());
    assertEquals(2, loanSimulationRequestDto.getRateOfInterest());
    assertEquals(12, loanSimulationRequestDto.getDurationInMonths());
  }
}
