package com.blms.loan.simulator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public class LoanMonthlySimulation {
  Integer monthNumber;
  Double openingBalance;
  Double currentMonthInterest;
  Long emi;
  Double closingBalance;
}
