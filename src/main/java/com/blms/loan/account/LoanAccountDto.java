package com.blms.loan.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public class LoanAccountDto {
  private String id;
  private Long number;
  @NotEmpty private String customerId;
  @Positive private Long initialAmount;
  @Positive private Integer initialDurationInMonths;
  private String createdAt;

  public static LoanAccountDto from(LoanAccount loanAccount) {
    return LoanAccountDto.builder()
        .id(loanAccount.getId().toString())
        .number(loanAccount.getNumber())
        .customerId(loanAccount.getCustomerId().toString())
        .initialAmount(loanAccount.getInitialAmount())
        .initialDurationInMonths(loanAccount.getInitialDurationInMonths())
        .createdAt(
            loanAccount
                .getCreatedAt()
                .toLocalDateTime()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        .build();
  }
}
