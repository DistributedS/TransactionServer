package utils;

import java.io.Serializable;

public class Transaction implements Serializable{
    
    int accountID;
    int amount;
    
    public Transaction(int accountID, int amount) {
        this.accountID = accountID;
        this.amount = amount;
    }
    
    public int getAccountID() {
        return accountID;
    }
    
    public int getAmount() {
        return amount;
    }
}