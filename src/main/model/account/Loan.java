package src.main.model.account;

public class Loan extends Account{

  private static final double DEBT_LIMIT = 10000.00;
  private static final double INTEREST_RATE = 0.02;

  public Loan(String id, String name, double balance) {
    super(id, name, balance);
  }
  
  public Loan(Loan source) {
    super(source);
  }

  @Override
  public Account clone() {
    Account loanAccount = new Loan(this);
    return loanAccount;
  }

  @Override
  public void deposit(double amount) {
    double newBalance = super.getBalance() - amount;
    super.setBalance(super.round(newBalance));
  }

  /**
   * Function name: withdraw
   * 
   * @param (double amount)
   * @return boolean
   * 
   * Withdrawal/loan requirements:
   * - A withdrawal can't made if the debt exceeds $10,000.
   * - Every withdrawal is charged a fixed interest rate of 2%.
   * 
   */

  @Override
  public boolean withdraw(double amount) {
    double currentBalance = super.getBalance();
    if (currentBalance + amount > DEBT_LIMIT) {
      return false;
    }
    super.setBalance(super.round(currentBalance + amount * (1 + INTEREST_RATE)));
    return true;
  }

}
