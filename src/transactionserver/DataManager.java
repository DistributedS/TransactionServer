package transactionserver;

import static java.util.Arrays.fill;
import utils.Account;
import utils.LockTypes;

public class DataManager implements LockTypes {
    
    Account accounts[];
    
    
    public DataManager(){
        accounts = new Account[10];
        //fill(accounts, new Account(10));
        
        for(int i=0; i<accounts.length; i++){
            accounts[i] = new Account(10);
        }
    }
    
    public int getAccountBalance(int accountID, int transID){
        TransactionServer.lockManager.setLock(accountID, accounts[accountID], transID, READ);
        int balance = accounts[accountID].getBalance();
        TransactionServer.lockManager.unLock(accountID, accounts[accountID], transID);
        //this.printAccounts();
        
        return balance;
    }
    
    public int setAccountBalance(int accountID, int transID, int transferAmt){
        TransactionServer.lockManager.setLock(accountID, accounts[accountID], transID, WRITE);
        accounts[accountID].addToBalance(transferAmt);
        int balance = accounts[accountID].getBalance();
        TransactionServer.lockManager.unLock(accountID, accounts[accountID], transID);
        //this.printAccounts();

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
