package transactionserver;

import utils.Account;
import utils.LockTypes;

public class DataManager implements LockTypes {
    
    Account accounts[];
    int USE_LOCKS = 1;
    int NUM_ACCOUNTS = 4;
    
    
    public DataManager(){
        accounts = new Account[NUM_ACCOUNTS];
        
        for(int i=0; i<accounts.length; i++){
            accounts[i] = new Account(10, i);
        }
    }
    
    public int getAccountBalance(int accountID, int transID){
        if (USE_LOCKS == 1) TransactionServer.lockManager.setLock(accounts[accountID], transID, READ);
        System.out.println("[Data Manager][Transaction "+transID+"]: Got Account Balance on account "+accountID+".");
        int balance = accounts[accountID].getBalance();
        if (USE_LOCKS == 1) TransactionServer.lockManager.unLock(accounts[accountID], transID);
        
        //return balance;
        return balance;
    }
    
    public int setAccountBalance(int accountID, int transID, int transferAmt){
        if (USE_LOCKS == 1) TransactionServer.lockManager.setLock(accounts[accountID], transID, WRITE);
        accounts[accountID].addToBalance(transferAmt);
        int balance = accounts[accountID].getBalance();
        if (USE_LOCKS == 1) TransactionServer.lockManager.unLock(accounts[accountID], transID);


        return balance;
    }
    
    public void printAccounts(){
        int totalBalance = 0;
        System.out.println("--------------Balance for all accounts:---------------");
        for(int i = 0; i<accounts.length; i++){
            int accountBalance = accounts[i].getBalance();
            totalBalance += accountBalance;
            System.out.print(" ["+accountBalance+"] ");
        }
        System.out.print("\n");
        System.out.println("Total accounts balance: "+totalBalance);
        System.out.println("------------------------------------------------------");
    }
}
