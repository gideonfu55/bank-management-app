package src.main.model.account;

import src.main.model.account.impl.Taxable;

public class Chequing extends Account implements Taxable {

  private static final double OVERDRAFT_FEE = 5.50;
  private static final double OVERDRAFT_LIMIT = -200.00;
  private static final double TAXABLE_INCOME = 3000.00;
  private static final double TAX_RATE = 0.15;

  public Chequing(String id, String name, double balance) {
    super(id, name, balance);
  }

  public Chequing(Chequing source) {
    super(source);
  }

  @Override
  public Account clone() {
    Account chequingAccount = new Chequing(this);
    return chequingAccount;
  }

  @Override
  public void deposit(double amount) {
    double newBalance = super.getBalance() + amount;
    super.setBalance(super.round(newBalance));
  }

  @Override
  public boolean withdraw(double amount) {
    if (super.getBalance() - amount < OVERDRAFT_LIMIT) {
      System.out.println("Following transaction not approved - above overdraft limit: ");
      return false;
    }

    super.setBalance(super.round(super.getBalance() - amount));

    if (super.getBalance() < 0) {
      super.setBalance(super.round(super.getBalance() - OVERDRAFT_FEE));
    }

    return true;
  }

  @Override
  public void tax(double income) {
    double taxAmount = Math.max(0, income - TAXABLE_INCOME) * TAX_RATE;
    super.setBalance(super.round(super.getBalance() - taxAmount));
  }

}
