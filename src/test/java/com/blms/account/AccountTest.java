package com.blms.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.blms.customer.Customer;
import com.blms.testutils.TestUtils;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AccountTest {

  private AccountDto accountDto;
  private Account account;

  @AfterEach
  void tearDown() {
    accountDto = null;
    account = null;
  }

  @Test
  void testAccountFromDto() {
    accountDto = TestUtils.getAccountDto();
    account = Account.from(accountDto);
    assertNotNull(account.getId());
    assertEquals(accountDto.getNumber(), account.getNumber());
    assertEquals(accountDto.getCustomerId(), account.getCustomerId().toString());
  }

  @Test
  void testAccountFromDtoWhenIdIsProvided() {
    String id = UUID.randomUUID().toString();
    accountDto = TestUtils.getAccountDto();
    accountDto.setId(id);
    account = Account.from(accountDto);
    assertEquals(id, account.getId().toString());
  }
}
