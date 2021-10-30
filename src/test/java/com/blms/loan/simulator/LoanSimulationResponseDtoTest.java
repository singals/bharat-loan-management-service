package com.blms.loan.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanSimulationResponseDtoTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  @Test
  void testMappingFromLoanSimulatorResponse() throws JsonProcessingException {
    LoanMonthlySimulation loanMonthlySimulation =
        LoanMonthlySimulation.builder()
            .monthNumber(1)
            .openingBalance(100.0)
            .currentMonthInterest(10.0)
            .emi(110L)
            .closingBalance(0D)
            .build();
    LoanSimulationResponseDto loanSimulationResponse =
        LoanSimulationResponseDto.builder()
            .amount(100L)
            .durationInMonths(1)
            .rateOfInterest(10F)
            .emi(110L)
            .loanMonthlySimulations(Arrays.asList(loanMonthlySimulation))
            .build();
    String jsonResponse = mapper.writeValueAsString(loanSimulationResponse);
    String expectedJson =
        "{\"amount\":100,\"rate_of_interest\":10.0,\"duration_in_months\":1,\"emi\":110,\"loan_monthly_simulations\":[{\"month_number\":1,\"opening_balance\":100.0,\"current_month_interest\":10.0,\"emi\":110,\"closing_balance\":0.0}]}";
    assertEquals(expectedJson, jsonResponse);
  }
}
