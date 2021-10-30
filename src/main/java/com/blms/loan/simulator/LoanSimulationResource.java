package com.blms.loan.simulator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/loan-simulation")
@Produces(MediaType.APPLICATION_JSON)
public class LoanSimulationResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoanSimulationResource.class);
  private static final DecimalFormat DF = new DecimalFormat("0.00");

  @POST
  public LoanSimulationResponseDto getLoanPreview(
      @Valid LoanSimulationRequestDto loanSimulationRequest) {
    Long principal = loanSimulationRequest.getAmount();
    Integer emiCount = loanSimulationRequest.getDurationInMonths();
    Float roi = loanSimulationRequest.getRateOfInterest() / 100;

    double tempEmi =
        (principal * roi * Math.pow(1 + roi, emiCount)) / (Math.pow(1 + roi, emiCount) - 1);
    long emi = Math.round(tempEmi);
    LOGGER.info(
        "EMI for a principal of {} with roi {} and duration {} months = {}",
        principal,
        roi,
        emiCount,
        emi);

    return prepareLoanSimulatorResponse(principal, emiCount, roi, emi);
  }

  private LoanSimulationResponseDto prepareLoanSimulatorResponse(
      Long principal, Integer emiCount, Float roi, Long emi) {

    List<LoanMonthlySimulation> monthlySimulations = new ArrayList<>(emiCount);
    Double openingBalance = principal.doubleValue();
    Double closingBalance;
    for (int i = 1; i <= emiCount; i++) {
      Double currentMonthInterest = Double.valueOf(DF.format(openingBalance * roi));
      closingBalance = Double.valueOf(DF.format(openingBalance + currentMonthInterest - emi));
      LoanMonthlySimulation monthlySimulation =
          LoanMonthlySimulation.builder()
              .monthNumber(i)
              .openingBalance(openingBalance)
              .currentMonthInterest(currentMonthInterest)
              .emi(emi)
              .closingBalance(closingBalance)
              .build();
      LOGGER.info("Monthly installment {}", monthlySimulation.toString());
      monthlySimulations.add(monthlySimulation);
      openingBalance = closingBalance;
    }
    return LoanSimulationResponseDto.builder()
        .amount(principal)
        .durationInMonths(emiCount)
        .rateOfInterest(roi)
        .emi(emi)
        .loanMonthlySimulations(monthlySimulations)
        .build();
  }
}
