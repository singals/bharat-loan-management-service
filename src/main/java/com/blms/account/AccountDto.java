package com.blms.account;

import com.blms.customer.CustomerDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class AccountDto {
  private String id;
  private Long number;
  private CustomerDto holder;

  public static AccountDto from(Account account) {
    return AccountDto.builder()
        .id(account.getId().toString())
        .number(account.getNumber())
        .holder(CustomerDto.from(account.getCustomer()))
        .build();
  }
}
