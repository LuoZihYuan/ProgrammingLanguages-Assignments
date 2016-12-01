
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

class BankingException extends Exception {
    BankingException () { super(); }
    BankingException (String s) { super(s); }
} 

interface BasicAccount {
    String name();
    double balance();	
}

interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;	
}

interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;	
}

interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;	
}

interface FullFunctionalAccount extends WithdrawableAccount,
                                        DepositableAccount,
                                        InterestableAccount {
}

public abstract class Account {
	
    // protected variables to store commom attributes for every bank accounts	
    protected String accountName;
    protected double accountBalance;
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;
    
    // public methods for every bank accounts
    public String name() {
    	return(accountName);	
    }	
    
    public double balance() {
        return(accountBalance);
    }

    abstract double deposit(double amount, Date depositDate) throws BankingException;
    
    public double deposit(double amount) throws BankingException {
        Date depositDate = new Date();
        return(deposit(amount, depositDate));
    } 
    
    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;
    
    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return(withdraw(amount, withdrawDate));
    }
    
    abstract double computeInterest(Date interestDate) throws BankingException;
    
    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return(computeInterest(interestDate));
    }
}

/*
 *  Derived class: CheckingAccount
 *
 *  Description:
 *      Interest is computed daily; there's no fee for
 *      withdraw; there is a minimum balance of $1000.
 */
                          
class CheckingAccount extends Account implements FullFunctionalAccount {

    CheckingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;	
    }
    
    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;	
    }	

    public double deposit(double amount, Date depositDate) throws BankingException {
        accountBalance += amount;
        return accountBalance;
    }
    
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
    // minimum balance is 1000, raise exception if violated
        if ((accountBalance  - amount) < 1000) {
            throw new BankingException ("Underfraft from checking account name:" +
                                         accountName);
        } else {
            accountBalance -= amount;	
            return(accountBalance); 	
        }                                        	
    }
    
    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name:" +
                                        accountName);                            	
        }
        
        int numberOfDays = (int) ((interestDate.getTime() 
                                   - lastInterestDate.getTime())
                                   / 86400000.0);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *
                                      accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }  	
}           

/*
 *  Derived Class: SavingAccount
 *
 *  Description:
 *      Monthly interest; fee of $1 for every transaction, except
 *      the first three per month are free; no minimum balance.
 */

class SavingAccount extends Account implements FullFunctionalAccount {

    // protected variables to keep track of free transactions per month of SavingAccount
    protected Date lastTransactionDate;
    protected int freeTransactions;

    SavingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;
        lastTransactionDate = openDate;
        freeTransactions = 3;
    }

    SavingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;
        lastTransactionDate = openDate;
        freeTransactions = 3;
    }

    public double deposit(double amount, Date depositDate) throws BankingException {
        // every transaction cost a $1 fee, except the first three per month are free
        if (depositDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to deposit for account name:" +
                                        accountName);
        } else if ((depositDate.getTime() - lastTransactionDate.getTime()) > 2592000000.0) {
            freeTransactions = 2;
            accountBalance += amount;
            lastTransactionDate = depositDate;
        } else if (freeTransactions > 0) {
            freeTransactions -= 1;
            accountBalance += amount;
            lastTransactionDate = depositDate;
        } else {
            accountBalance += amount;
            accountBalance -= 1.0;
            lastTransactionDate = depositDate;
        }
        return(accountBalance);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // every transaction cost a $1 fee, except the first three per month are free
        if (withdrawDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to withdraw for account name:" +
                                        accountName);
        } else if ((accountBalance - amount) < 0) {
            throw new BankingException("Underfraft from saving account name:" +
                                        accountName);
        } else if ((withdrawDate.getTime() - lastTransactionDate.getTime()) > 2592000000.0) {
            freeTransactions = 2;
            accountBalance -= amount;
            lastTransactionDate = withdrawDate;
        } else if (freeTransactions > 0) {
            freeTransactions -= 1;
            accountBalance -= amount;
            lastTransactionDate = withdrawDate;
        } else {
            accountBalance -= amount;
            accountBalance -= 1.0;
            lastTransactionDate = withdrawDate;
        }
        return(accountBalance);
    }

    public double computeInterest(Date interestDate) throws BankingException {
        // compute interest of SavingAccount monthly
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name:" +
                                        accountName);
        }

        int numberOfMonths = (int) ((interestDate.getTime()
                                     - lastInterestDate.getTime())
                                     / 2592000000.0);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12.0 *
                                    accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);
    }
}

/*
 *  Derived Class: CDAccount
 *
 *  Description:
 *      Monthly interest; fixed amount and duration (e.g., you can open
 *      1 12-month CD for $5000; for the next 12 months you can't deposit
 *      anything and withdrawals cost a  $250 fee); at the end of the 
 *      duration the interest payments stop and you can withdraw w/o fee.
 */

class CDAccount extends Account implements FullFunctionalAccount {
    
    // protected variable to store fixed duration of CDAccount
    protected int accountDuration;

    CDAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;
        accountDuration = 12;
    }

    CDAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;
        accountDuration = 12;
    }

    public double deposit(double amount, Date depositDate) throws BankingException {
        // prohibit to deposit money into CDAccount after first deposit
        throw new BankingException("Invalid to deposit from CDAccount for account name:" +
                                    accountName);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // withdrawals within fixed duration cost a $250 fee
        if (withdrawDate.before(openDate)) {
            throw new BankingException("Invalid date to withdraw for account name:" +
                                        accountName);
        }

        int numberOfMonths = (int) ((withdrawDate.getTime()
                                     - openDate.getTime())
                                     / 2592000000.0);
        double extraFee = (numberOfMonths >= accountDuration) 
                            ? 0.0: 250.0;
        if ((accountBalance - amount - extraFee) < 0) {
            throw new BankingException("Underfraft from CD account name:" +
                                        accountName);
        } else {
            accountBalance -= (amount + extraFee);
            return(accountBalance);
        }
    }

    public double computeInterest(Date interestDate) throws BankingException {
        // compute interest of CDAccount monthly
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name:" +
                                        accountName);
        }

        int numberOfMonths = (int) ((interestDate.getTime()
                                     - lastInterestDate.getTime())
                                     / 2592000000.0);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        int pastMonths = (int) ((lastInterestDate.getTime()
                                 - openDate.getTime())
                                 / 2592000000.0);
        if (numberOfMonths + pastMonths > accountDuration) {
            numberOfMonths = (accountDuration - pastMonths);
            numberOfMonths = (numberOfMonths < 0)? 0: numberOfMonths;
        }
        double interestEarned = (double) numberOfMonths / 12.0 *
                                    accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);
    }

}

/*
 *  Derived Class: LoanAccount
 *
 *  Description:
 *      Like a saving account, but the balance is "negative" (you owe
 *      the bank money, so a deposit will reduce the amount of the loan);
 *      you can't withdraw (i.e., loan more money) but of course you can 
 *      deposit (i.e., pay off part of the loan).
 */

class LoanAccount extends Account implements FullFunctionalAccount {

    LoanAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = new Date();
    }
    
    LoanAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        openDate = firstDate;
    }   

    public double deposit(double amount, Date depositDate) throws BankingException {
        accountBalance += amount;
        return accountBalance;
    }
    
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // prohibit to withdraw money from LoanAccount
        throw new BankingException("Invalid to withdraw from LoanAccount for account name:" +
                                    accountName);
    }
    
    public double computeInterest(Date interestDate) throws BankingException {
        // prohibit to compute interest from LoanAccount
        throw new BankingException("Invalid to compute interest from LoanAccount for account name:" +
                                    accountName);
    }   
}