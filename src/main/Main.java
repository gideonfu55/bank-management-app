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
     * Inside the function
     * 1. Print: \t\t\t\t TRANSACTION HISTORY\n\t
     * 2. Print every transaction that corresponds to the id. (Waits 300 milliseconds before printing the next one)
     *    - Use this format "\t"+transaction+"\n"
     * 3. Print: \n\t\t\t\t\tAFTER TAX\n
     * 4. Print: "\t" + account that corresponds to id +"\n\n\n\n"
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

        // // 1. Checking for instantiating new account types:

        // Chequing chequing = new Chequing("f84c43f4-a634-4c57-a644-7602f8840870", "Michael Scott", 1524.51);
        // Savings savings = new Savings("ce07d7b3-9038-43db-83ae-77fd9c0450c9", "Saul Goodman", 2241.60);
        // Loan loan = new Loan("4991bf71-ae8f-4df9-81c1-9c79cff280a5", "Phoebe Buffay", 2537.31);

        // // Tests:
        // System.out.println(chequing);
        // System.out.println(savings);
        // System.out.println(loan);

        // // 2. Checking for instantiating new transactions:

        // Transaction transaction = new Transaction(Transaction.Type.WITHDRAWAL, 1546905600, "6b8dd258-aba3-4b19-b238-45d15edd4b48", 624.99);
        // Transaction transaction2 = new Transaction(Transaction.Type.WITHDRAWAL, 1546905605, "6b8dd258-aba3-4b19-b238-45d15edd4b49", 1000.00);

        // // Tests:
        // System.out.println("\n");
        // System.out.println(transaction);
        // System.out.println(transaction2);

        // // 3. Checking for clone() method:

        // Chequing chequing2 = new Chequing("f84c43f4-a634-4c57-a644-7602f8840870", "Michael Scott", 1524.51);
        // Account chequingCopy = chequing.clone();

        // Savings savings2 = new Savings("ce07d7b3-9038-43db-83ae-77fd9c0450c9", "Saul Goodman", 2241.60);
        // Account savingsCopy = savings.clone();

        // // Tests for clone() method in each account type:
        // System.out.println("\n");
        // System.out.println(chequing2);
        // System.out.println(chequingCopy);
        // System.out.println(savings2);
        // System.out.println(savingsCopy);

        // // 4. Checking for new Bank object, accounts and transactions:

        // Bank bank = new Bank();

        // Account[] accounts = new Account[] {
        // new Chequing("f84c43f4-a634-4c57-a644-7602f8840870", "Michael Scott",
        // 1524.51),
        // new Savings("ce07d7b3-9038-43db-83ae-77fd9c0450c9", "Saul Goodman", 2241.60)
        // };

        // for (Account account : accounts) {
        // bank.addAccount(account);
        // }

        // Transaction[] transactions = new Transaction[] {
        // new Transaction(Transaction.Type.WITHDRAW, 1546905600, "f84c43f4-a634-4c57-a644-7602f8840870", 624.99),
        // new Transaction(Transaction.Type.DEPOSIT, 1578700800, "f84c43f4-a634-4c57-a644-7602f8840870", 441.93),
        // new Transaction(Transaction.Type.WITHDRAW, 1547078400, "f84c43f4-a634-4c57-a644-7602f8840870", 546.72),
        // new Transaction(Transaction.Type.WITHDRAW, 1546732800, "f84c43f4-a634-4c57-a644-7602f8840870", 546.72),
        // new Transaction(Transaction.Type.DEPOSIT, 1578355200, "f84c43f4-a634-4c57-a644-7602f8840870", 635.95),
        // new Transaction(Transaction.Type.WITHDRAW, 1547078400, "ce07d7b3-9038-43db-83ae-77fd9c0450c9", 875.64),
        // new Transaction(Transaction.Type.WITHDRAW, 1578614400, "ce07d7b3-9038-43db-83ae-77fd9c0450c9", 912.45),
        // new Transaction(Transaction.Type.WITHDRAW, 1577836800, "ce07d7b3-9038-43db-83ae-77fd9c0450c9", 695.09),
        // new Transaction(Transaction.Type.WITHDRAW, 1609459200, "ce07d7b3-9038-43db-83ae-77fd9c0450c9", 917.21),
        // new Transaction(Transaction.Type.WITHDRAW, 1578096000, "ce07d7b3-9038-43db-83ae-77fd9c0450c9", 127.94),
        // new Transaction(Transaction.Type.WITHDRAW, 1546819200, "ce07d7b3-9038-43db-83ae-77fd9c0450c9", 612.52)
        // };

        // for (Transaction transaction : transactions) {
        //     bank.executeTransaction(transaction);
        // }

        // Transaction[] filteredTransactions =
        // bank.getTransactions("f84c43f4-a634-4c57-a644-7602f8840870");

        // // Print all transactions in filteredTransactions - there should be 5 for the provided accountID:
        // Arrays.stream(filteredTransactions)
        // .forEach(transaction -> System.out.println(transaction));

        // // Check for getAccount():
        // Account account = bank.getAccount("ce07d7b3-9038-43db-83ae-77fd9c0450c9");

        // System.out.println("\nChecking getAccount() method for Bank class:");
        // System.out.println(account);

        // // 5. Check method works for createObject():

        // String[] values = new String[] { "Chequing", "f84c43f4-a634-4c57-a644-7602f8840870", "Michael Scott", "1524.51"};

        // System.out.println(createObject(values));