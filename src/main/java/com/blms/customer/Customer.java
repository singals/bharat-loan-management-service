package com.blms.customer;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
  @Id private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "primary_contact_no")
  private String primaryContactNumber;

  @Column(name = "alternate_contact_no")
  private String alternateContactNumber;

  @Column(name = "permanent_address")
  private String permanentAddress;

  @Column(name = "temporary_address")
  private String temporaryAddress;

  @Column(name = "link_to_folder_with_docs")
  private String linkToFolderWithDocuments;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "is_blacklisted")
  private Boolean isBlacklisted;

  public static Customer from(CustomerDto customerDto) {
    return new Customer(
        UUID.randomUUID(),
        customerDto.getFirstName(),
        customerDto.getLastName(),
        customerDto.getMiddleName(),
        customerDto.getPrimaryContactNumber(),
        customerDto.getAlternateContactNumber(),
        customerDto.getPermanentAddress(),
        customerDto.getTemporaryAddress(),
        customerDto.getLinkToFolderWithDocuments(),
        customerDto.getIsActive(),
        customerDto.getIsBlacklisted());
  }
}
