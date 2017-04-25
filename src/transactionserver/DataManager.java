package transactionserver;

import static java.util.Arrays.fill;

public class DataManager {
    
    int accounts[];
    
    public DataManager(){
        accounts = new int[10];
        fill(accounts, 10);
    }
    
    public int getAccountBalance(int accountID){
        return accounts[accountID];
    }
    
    public int doAccountTrans(int accountID, int transferAmt){
        accounts[accountID] += transferAmt;
        return accounts[accountID];
    }
}
