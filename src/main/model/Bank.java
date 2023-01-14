package src.main.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import src.main.model.Transaction.Type;
import src.main.model.account.Account;
import src.main.model.account.Chequing;
import src.main.model.account.impl.Taxable;

public class Bank {

  private ArrayList<Account> accounts;
  private ArrayList<Transaction> transactions;

  public Bank() {
    this.accounts = new ArrayList<Account>();
    this.transactions = new ArrayList<Transaction>();
  }

  // Methods for Bank Class:

  /**
   * Name: addAccount
   * 
   * @param account (Account)
   * 
   * Inside the function:
   * 1. adds an account to the accounts ArrayList
   * 
   * - The reason a clone is required is because Account is abstract and you cannot construct an Account by its constructors.
   */

  public void addAccount(Account account) {
    this.accounts.add(account.clone());
  }

  /**
   * Name: addTransaction
   * 
   * @param transaction
   * 
   * Inside the function:
   * 1. adds a new transaction object to the array list.
   * 
   * Note - this is to be private as the caller should be forbidden from adding transactions. It is the bank who decides to add 
   * successful transactions and ignore the ones that fail.
   */

  private void addTransaction(Transaction transaction) {
    this.transactions.add(new Transaction(transaction));
  }

  /**
   * Name: getTransactions
   * 
   * @param accountId (String)
   * @return (Transaction[])
   * 
   * 1. returns an array of transactions whose id (provided in the form of accountIds) matches the accountId.
   */

  public Transaction[] getTransactions(String accountId) {
    List<Transaction> transactionList = this.transactions
    .stream()
    .filter(transaction -> transaction.getId().equals(accountId))
    .collect(Collectors.toList());

    return transactionList.toArray(new Transaction[transactionList.size()]);
  }

  /**
   * Name: getAccount()
   * 
   * @param transactionId (String)
   * @return (Account)
   * 
   * 1. returns an account whose account id matches a transaction id (which are provided as accountIds in the list of
   *    transactions).
   */

  public Account getAccount(String transactionId) {
    return this.accounts
    .stream()
    .filter(account -> account.getId().equals(transactionId))
    .findFirst()
    .orElse(null);
  }

  private void depositTransaction(Transaction transaction) {
    Account accountToTransact = getAccount(transaction.getId());
    if (accountToTransact != null) {
      accountToTransact.deposit(transaction.getAmount());
      addTransaction(transaction);
    }
  }

  private void withdrawTransaction(Transaction transaction) {
    Account accountToTransact = getAccount(transaction.getId());
    if (accountToTransact != null && accountToTransact.withdraw(transaction.getAmount())) {
      addTransaction(transaction);
    } else {
      // Personal preference - this line follows an error statement within the respective account for an unsuccessful transaction:
      System.out.println("Account holder: " + accountToTransact.getName() + "\tAccount type: " + accountToTransact.getClass().getSimpleName() + "\t" + transaction + "\n");
    }
  }

  /**
     * Name: executeTransaction
     * @param transaction
     * 
     * Inside the function:
     *  1. calls withdrawTransaction if transaction type is WITHDRAW
     *  2. calls depositTransaction if transaction type is DEPOSIT
     * 
     */

  public void executeTransaction(Transaction transaction) {
    Type transactionType = transaction.getType();
    switch(transactionType) {

      case DEPOSIT: 
      depositTransaction(transaction);
      break;

      case WITHDRAW:
      withdrawTransaction(transaction);

      default:
        break;
    }
  }

  /**
   * Name: getIncome
   * 
   * @param account (Taxable)
   * @return double
   * 
   *  Inside the function:
   *   1. Gets every transaction that matches the account's id.
   *   2. Maps every transaction to a double => .mapToDouble(lambda expression)
   *      - Transactions of type WITHDRAW are mapped to negative numbers.
   *      - Transactions of type DEPOSIT are mapped to positive numbers.
   *   3. Takes the sum of every number and returns the income.
   * 
   */

  private double getIncome(Taxable account) {
    Transaction[] transactions = getTransactions(((Chequing)account).getId());
    return Arrays.stream(transactions)
      .mapToDouble(transaction -> {
        switch (transaction.getType()) {
          case WITHDRAW: return -transaction.getAmount();
          case DEPOSIT: return transaction.getAmount();
          default: return 0;
        }
      }).sum();
  }

  /**
   * Name: deductTaxes
   * 
   * Inside the function:
   * 1. Loops through every account in bank object (ArrayList<Account> accounts) and check if it is taxable (all taxable accounts
   * implements the Taxable interface). In this case, only the Chequing account does.
   * 2. For a taxable account, typecase it and apply its tax() method with the calculated income.
   * 
   */

  public void deductTaxes() {
    for (Account account : accounts) {
      if (Taxable.class.isAssignableFrom(account.getClass())) {
        Taxable taxableAccount = (Taxable)account;
        taxableAccount.tax(getIncome(taxableAccount));
      }
    }
  }

}
