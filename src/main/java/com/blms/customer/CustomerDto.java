package com.blms.customer;

import com.blms.account.loan.LoanAccountDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class CustomerDto {
  private String id;

  @NotEmpty private String firstName;

  @NotEmpty private String lastName;

  private String middleName;

  @NotEmpty private String primaryContactNumber;

  private String alternateContactNumber;

  @NotEmpty private String permanentAddress;

  private String temporaryAddress;

  private String linkToFolderWithDocuments;

  private Boolean isActive;

  private Boolean isBlacklisted;

  private List<LoanAccountDto> accounts;

  public static CustomerDto from(Customer customer) {
    return new CustomerDto(
        customer.getId().toString(),
        customer.getFirstName(),
        customer.getLastName(),
        customer.getMiddleName(),
        customer.getPrimaryContactNumber(),
        customer.getAlternateContactNumber(),
        customer.getPermanentAddress(),
        customer.getTemporaryAddress(),
        customer.getLinkToFolderWithDocuments(),
        customer.getIsActive(),
        customer.getIsBlacklisted(),
        customer.getLoanAccounts() == null
            ? new ArrayList<>()
            : customer.getLoanAccounts().stream()
                .map(LoanAccountDto::from)
                .collect(Collectors.toList()));
  }
}
