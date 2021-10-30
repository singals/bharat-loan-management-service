package com.blms.loan.account;

import com.blms.BlmsBaseEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_accounts")
@NamedQueries({@NamedQuery(name = "LoanAccount.findAll", query = "SELECT a FROM LoanAccount a")})
public class LoanAccount implements BlmsBaseEntity {
  @Id private UUID id;

  @Column(name = "number")
  @Generated(GenerationTime.INSERT)
  private Long number;

  @Column(name = "customer_id")
  private UUID customerId;

  @Column(name = "initial_amount")
  private Long initialAmount;

  @Column(name = "initial_duration_months")
  private Integer initialDurationInMonths;

  @Column(name = "created_at")
  private Timestamp createdAt;

  public static LoanAccount from(LoanAccountDto account) {
    return LoanAccount.builder()
        .id(account.getId() == null ? UUID.randomUUID() : UUID.fromString(account.getId()))
        .number(account.getNumber())
        .customerId(UUID.fromString(account.getCustomerId()))
        .initialAmount(account.getInitialAmount())
        .initialDurationInMonths(account.getInitialDurationInMonths())
        .createdAt(
            account.getCreatedAt() == null
                ? new Timestamp(System.currentTimeMillis())
                : new Timestamp(
                    LocalDateTime.parse(
                            account.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .getNano()))
        .build();
  }
}
