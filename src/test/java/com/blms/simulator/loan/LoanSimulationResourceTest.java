package com.blms.simulator.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class LoanSimulationResourceTest {

  public static final ResourceExtension RESOURCES =
      ResourceExtension.builder().addResource(new LoanSimulationResource()).build();

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  @Test
  void testSimpleLoanSimulation() {
    LoanSimulationRequestDto request =
        LoanSimulationRequestDto.builder()
            .amount(100_000l)
            .durationInMonths(1)
            .rateOfInterest(10F)
            .build();

    Response response =
        RESOURCES
            .target("/loan-simulation")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(Response.Status.OK, response.getStatusInfo());
    LoanSimulationResponseDto loanSimulationResponse =
        response.readEntity(LoanSimulationResponseDto.class);
    assertEquals(100_000, loanSimulationResponse.getAmount());
    assertEquals(110_000, loanSimulationResponse.getEmi());
    assertEquals(1, loanSimulationResponse.getDurationInMonths());
    List<LoanMonthlySimulation> loanMonthlySimulations =
        loanSimulationResponse.getLoanMonthlySimulations();
    assertEquals(1, loanMonthlySimulations.size());
    LoanMonthlySimulation loanMonthlySimulation = loanMonthlySimulations.get(0);
    assertEquals(1, loanMonthlySimulation.getMonthNumber());
    assertEquals(100_000, loanMonthlySimulation.getOpeningBalance());
    assertEquals(10_000, loanMonthlySimulation.getCurrentMonthInterest());
    assertEquals(110_000, loanMonthlySimulation.getEmi());
    assertEquals(0, loanMonthlySimulation.getClosingBalance());
  }

  @Test
  void testLoanSimulationWithMultipleEmis() {
    LoanSimulationRequestDto request =
        LoanSimulationRequestDto.builder()
            .amount(100_000l)
            .durationInMonths(3)
            .rateOfInterest(10F)
            .build();

    Response response =
        RESOURCES
            .target("/loan-simulation")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(Response.Status.OK, response.getStatusInfo());
    LoanSimulationResponseDto loanSimulationResponse =
        response.readEntity(LoanSimulationResponseDto.class);
    assertEquals(100_000, loanSimulationResponse.getAmount());
    assertEquals(40_211, loanSimulationResponse.getEmi());
    assertEquals(3, loanSimulationResponse.getDurationInMonths());
    List<LoanMonthlySimulation> loanMonthlySimulations =
        loanSimulationResponse.getLoanMonthlySimulations();
    assertEquals(3, loanMonthlySimulations.size());
    LoanMonthlySimulation firstMonthSimulation = loanMonthlySimulations.get(0);
    assertEquals(1, firstMonthSimulation.getMonthNumber());
    assertEquals(100_000, firstMonthSimulation.getOpeningBalance());
    assertEquals(10_000, firstMonthSimulation.getCurrentMonthInterest());
    assertEquals(40_211, firstMonthSimulation.getEmi());
    assertEquals(69_789, firstMonthSimulation.getClosingBalance());
    LoanMonthlySimulation secondMonthSimulation = loanMonthlySimulations.get(1);
    assertEquals(2, secondMonthSimulation.getMonthNumber());
    assertEquals(69_789, secondMonthSimulation.getOpeningBalance());
    assertEquals(6_978.9, secondMonthSimulation.getCurrentMonthInterest());
    assertEquals(40_211, secondMonthSimulation.getEmi());
    assertEquals(36_556.9, secondMonthSimulation.getClosingBalance());
    LoanMonthlySimulation thirdMonthSimulation = loanMonthlySimulations.get(2);
    assertEquals(3, thirdMonthSimulation.getMonthNumber());
    assertEquals(36_556.9, thirdMonthSimulation.getOpeningBalance());
    assertEquals(3_655.69, thirdMonthSimulation.getCurrentMonthInterest());
    assertEquals(40_211, thirdMonthSimulation.getEmi());
    assertEquals(1.59, thirdMonthSimulation.getClosingBalance());
  }
}
