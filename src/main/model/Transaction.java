package src.main.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
  
  public enum Type {DEPOSIT, WITHDRAW};
  private Type type;
  private long timestamp;
  private String id;
  private double amount;

  public Transaction(Type type, long timestamp, String id, double amount) {
    setType(type);
    setTimestamp(timestamp);
    setId(id);
    setAmount(amount);
  }
    
  public Transaction(Transaction source) {
    this.type = source.type;
    this.timestamp = source.timestamp;
    this.id = source.id;
    this.amount = source.amount;
  }

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  } 

  public long getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("ID cannot be null or blank. Please recheck the ID.");
    }
    this.id = id;
  }

  public double getAmount() {
    return this.amount;
  }

  public void setAmount(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Transaction amount cannot be negative. Please recheck the amount.");
    }
    this.amount = amount;
  }

  public String returnDate() {
    Date date = new Date(this.timestamp * 1000);
    return new SimpleDateFormat("dd-MM-yyyy").format(date);
  }

  // Override equals() method:
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Transaction)) {
      return false;
    }
    
    Transaction transaction = (Transaction)obj;

    return this.type.equals(transaction.type) &&
    this.timestamp == transaction.getTimestamp() &&
    this.id.equals(transaction.id) &&
    this.amount == transaction.getAmount();

  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, id, amount);
  }

  // For making Transaction objects "sortable" from lowest to highest by - timestamp:
  // - 1st object, if larger than the 2nd, will yield a positive result.
  @Override
  public int compareTo(Transaction specifiedObject) {
    return Double.compare(this.timestamp, specifiedObject.timestamp);
  }

  // Overriding toString() method:
  @Override
  public String toString() {
    return 
    this.getClass().getSimpleName() + ": " +
    "\t" + this.returnDate() + "" +
    "\t" + this.getId() + "" +
    "\t" + this.getType() + " " +
    "\t$" + this.getAmount() + "";
  }

}
