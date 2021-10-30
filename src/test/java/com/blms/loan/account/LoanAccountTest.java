package com.blms.loan.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.blms.testutils.TestUtils;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class LoanAccountTest {

  private LoanAccountDto loanAccountDto;
  private LoanAccount loanAccount;

  @AfterEach
  void tearDown() {
    loanAccountDto = null;
    loanAccount = null;
  }

  @Test
  void testLoanAccountFromDto() {
    loanAccountDto = TestUtils.getAccountDto();
    loanAccount = LoanAccount.from(loanAccountDto);
    assertNotNull(loanAccount.getId());
    assertEquals(loanAccountDto.getNumber(), loanAccount.getNumber());
    assertEquals(loanAccountDto.getCustomerId(), loanAccount.getCustomerId().toString());
  }

  @Test
  void testLoanAccountFromDtoWhenIdIsProvided() {
    String id = UUID.randomUUID().toString();
    loanAccountDto = TestUtils.getAccountDto();
    loanAccountDto.setId(id);
    loanAccount = LoanAccount.from(loanAccountDto);
    assertEquals(id, loanAccount.getId().toString());
  }
}
