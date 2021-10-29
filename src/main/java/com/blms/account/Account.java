package com.blms.account;

import com.blms.BlmsBaseEntity;
import com.blms.customer.Customer;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "accounts")
@NamedQueries({@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")})
public class Account implements BlmsBaseEntity {
  @Id private UUID id;

  @Column(name = "number")
  @Generated(GenerationTime.INSERT)
  private Long number;

  @Column(name = "customer_id")
  private UUID customerId;

  public static Account from(AccountDto account) {
    return Account.builder()
        .id(account.getId() == null ? UUID.randomUUID() : UUID.fromString(account.getId()))
        .number(account.getNumber())
        .customerId(UUID.fromString(account.getCustomerId()))
        .build();
  }
}
