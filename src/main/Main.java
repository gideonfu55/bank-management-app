package src.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import src.main.model.Bank;
import src.main.model.Transaction;
import src.main.model.account.Account;

public class Main {

   static String ACCOUNTS_FILE = "src/main/data/accounts.txt";
   static String TRANSACTIONS_FILE = "src/main/data/transactions.txt";

   static Bank bank = new Bank();

    public static void main(String[] args) {
        try {
            // Creating and adding every account in ACCOUNT_FILE to the bank object:
            ArrayList<Account> accounts = returnAccounts();
            loadAccounts(accounts);

            // Adding every transaction in TRANSACTIONS_FILE to the bank object & executing all the transactions:
            ArrayList<Transaction> transactions = returnTransactions();
            runTransactions(transactions);

            // Applying taxation to applicable accounts and income(s):
            bank.deductTaxes();

            // Print transaction history for each account:
            for (Account account : accounts) {
                System.out.println("\n\t\t\t\t\t ACCOUNT\n\n\t" + account + "\n\n");
                transactionHistory(account.getId());
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Function name: wait
     * @param milliseconds
     * 
     * Inside the function:
     *  1. Makes the code sleep for X milliseconds.
     */

     public static void wait(int milliseconds) {
         try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
         } catch (InterruptedException e) {
             System.out.println(e.getMessage());
         }
     }

     /**
      * Name: createObject
      * 
      * @param values (String[] values)
      * @return Account
      * 
      * Inside the function:
      * 1. Dynamically creates a Chequing, Loan, or Savings object based on the values array.
      *
      */

    public static Account createObject(String[] values) {
        try {
            Account accountNew = (Account)Class.forName("src.main.model.account." + values[0])
                .getConstructor(String.class, String.class, double.class)
                .newInstance(values[1], values[2], Double.parseDouble(values[3]));

            return accountNew;

        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    /**
     * Name: returnAccounts()
     * 
     * @return ArrayList<Account>
     * @throws FileNotFoundException
     * 
     * Inside the function:
     * 1. Creates a Scanner object and reads the data from accounts.txt.
     * 2. Creates a new Account object for every line in accounts.txt.
     * 3. Returns an ArrayList of all Account objects.
     * 
     */

    public static ArrayList<Account> returnAccounts() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(ACCOUNTS_FILE);
        Scanner scan = new Scanner(fis);

        ArrayList<Account> accounts = new ArrayList<Account>();

        while (scan.hasNextLine()) {
            accounts.add(createObject(scan.nextLine().split(",")));
        }
        
        scan.close();
        return accounts;
    }

    /**
     * Name: loadAccounts
     * 
     * @param accounts (ArrayList<Account>)
     * 
     * Inside the function:
     * 1. Adds every account into the Bank object.
     * 
     */

    public static void loadAccounts(ArrayList<Account> accounts) {
        for(Account account : accounts) {
            bank.addAccount(account);
        }
    }

    /**
     * Name: returnTransactions()
     * 
     * @return ArrayList<Transaction>
     * @throws FileNotFoundException
     * 
     * Inside the function:
     * 1. Creates a Scanner object and reads the data from transactions.txt.
     * 2. Populates an ArrayList with transaction objects created from (each line of) transactions.txt.
     * 3. Sorts the ArrayList.
     * 
     */

    public static ArrayList<Transaction> returnTransactions() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(TRANSACTIONS_FILE);
        Scanner scan = new Scanner(fis);

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        while (scan.hasNextLine()) {
            String[] values = scan.nextLine().split(",");
            transactions.add(new Transaction(Transaction.Type.valueOf(values[1]), Long.valueOf(values[0]), values[2], Double.parseDouble(values[3])));
        }

        scan.close();
        Collections.sort(transactions);
        return transactions;
    }

    /**
     * Name: runTransactions
     * 
     * @param transactions ArrayList<Transaction>
     * 
     * Inside the function:
     * 1. Executes every transaction in the ArrayList with the executeTransaction() function.
     * 
     */

    public static void runTransactions(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            bank.executeTransaction(transaction);
        }
    }

    /**
     * Name: transactionHistory
     * 
     * @param id (String)
     * 
     * Inside the function:
     * 1. Prints header for transaction history.
     * 2. Print every transaction that corresponds to the id. (Waits 300 milliseconds before printing the next one)
     * 3. Prints header for account summary:
     * 4. Prints account information after executing applicable transactions and tax deductions.
     * 
     */

    public static void transactionHistory(String id) {
        Transaction[] transactions = bank.getTransactions(id);
        System.out.println("\t\t\t\t   TRANSACTION HISTORY\n\t");
        for (Transaction transaction : transactions) {
            wait(300);
            System.out.println("\t" + transaction + "\n");
        }
        System.out.println("\n\t\t\t\t\tAFTER TAX\n");
        System.out.println("\t" + bank.getAccount(id) + "\n\n\n\n");
    }

}