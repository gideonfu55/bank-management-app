package src.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import src.main.model.Transaction;

public class TransactionTests {
  
  Transaction transaction;
  Transaction transaction2;
  Transaction transaction3;

  @Before
  public void setup() {
    transaction = new Transaction(Transaction.Type.WITHDRAW, 1546905600, "6b8dd258-aba3-4b19-b238-45d15edd4b48", 624.99);
    transaction2 = new Transaction(Transaction.Type.WITHDRAW, 1546905605, "6b8dd258-aba3-4b19-b238-45d15edd4b49", 1000.00);
    transaction3 = new Transaction(transaction);
  }

  // Test Case 1 - Correct Date:
  @Test
  public void correctDateTest() {
    assertEquals("08-01-2019", transaction.returnDate());
  }

  // Self-test Case - compareTo() override:
  @Test
  public void compareToTest() {
    assertTrue(transaction2.compareTo(transaction) > 0);
  }

  // Self-test Case - Equality Check:
  @Test
  public void transactionEquals() {
    assertTrue(transaction.equals(transaction3));
  }
  
}
