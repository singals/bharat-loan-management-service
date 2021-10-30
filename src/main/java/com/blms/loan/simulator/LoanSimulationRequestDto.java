package com.blms.loan.simulator;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class LoanSimulationRequestDto {
  @Positive private Long amount;
  @Positive private Float rateOfInterest;
  @Positive private Integer durationInMonths;
}
