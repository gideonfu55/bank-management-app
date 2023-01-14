package src.main.model.account;

public class Savings extends Account {

  private static final double WITHDRAWAL_FEE = 5.00;

  public Savings(String id, String name, double balance) {
    super(id, name, balance);
  }

  public Savings(Savings source) {
    super(source);
  }

  @Override
  public Account clone() {
    Account savingsAccount = new Savings(this);
    return savingsAccount;
  }

  @Override
  public void deposit(double amount) {
    double newBalance = super.getBalance() + amount;
    super.setBalance(super.round(newBalance));
  }

  /**
   * Function name: withdraw
   * @param (double amount)
   * @return boolean
   * 
   * Withdrawal requirement:
   * - The savings account charges a $5.00 fee for every withdrawal.
   * 
   */

  @Override
  public boolean withdraw(double amount) {
    if (super.getBalance() - amount > 0) {
      super.setBalance(super.round(super.getBalance() - amount - WITHDRAWAL_FEE));
      return true;
    } else {
      System.out.println("Following transaction is not approved - insufficient balance: ");
      return false;
    }
  }

}
