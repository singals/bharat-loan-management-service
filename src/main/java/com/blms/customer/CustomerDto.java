package com.blms.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
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
        customer.getIsBlacklisted());
  }
}
