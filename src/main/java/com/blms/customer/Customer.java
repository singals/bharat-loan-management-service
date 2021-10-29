package com.blms.customer;

import com.blms.BlmsBaseEntity;
import com.blms.account.loan.LoanAccount;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@NamedQueries({@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")})
public class Customer implements BlmsBaseEntity {
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

  @OneToMany
  @JoinColumn(name = "customer_id")
  private List<LoanAccount> loanAccounts;

  public static Customer from(CustomerDto customerDto) {
    return new Customer(
        customerDto.getId() == null ? UUID.randomUUID() : UUID.fromString(customerDto.getId()),
        customerDto.getFirstName(),
        customerDto.getLastName(),
        customerDto.getMiddleName(),
        customerDto.getPrimaryContactNumber(),
        customerDto.getAlternateContactNumber(),
        customerDto.getPermanentAddress(),
        customerDto.getTemporaryAddress(),
        customerDto.getLinkToFolderWithDocuments(),
        customerDto.getIsActive() == null || customerDto.getIsActive(),
        customerDto.getIsBlacklisted() != null && customerDto.getIsBlacklisted(),
        customerDto.getAccounts() == null
            ? null
            : customerDto.getAccounts().stream().map(LoanAccount::from).collect(Collectors.toList()));
  }
}
