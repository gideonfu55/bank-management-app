package src.main.model.account;

import java.text.DecimalFormat;

// Bank Management 1: https://www.learnthepart.com/course/2dfda34d-6bbc-4bd5-8f45-d5999de2f514/20e0c341-e89b-479c-9c54-c9f5797dafaa

// Parent Class for Chequing, Savings and Loan Classes.
public abstract class Account {

  private String id;
  private String name;
  private double balance;

  public Account(String id, String name, double balance) {
    setId(id);
    setName(name);
    setBalance(balance);
  }

  public Account(Account source) {
    this.id = source.id;
    this.name = source.name;
    this.balance = source.balance;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Account Id cannot be null or blank. Please check again.");
    }
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Account Name cannot be null or blank. Please check again.");
    }
    this.name = name;
  }

  public double getBalance() {
    return this.balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return
      this.getClass().getSimpleName() + " Account Information:- " +
      "\tid: " + getId() + "" + 
      "\tname: " + getName() + "" +
      "\tbalance: $" + getBalance() + "";
  }

  // Account Methods:
  public abstract void deposit(double amount);
  public abstract boolean withdraw(double amount);

  protected double round(double amount) {
    DecimalFormat formatter = new DecimalFormat("#.##");
    return Double.parseDouble(formatter.format(amount));
  }

  // As you won't be able to create a new Account object, you need a method to clone:
  public abstract Account clone();

}
