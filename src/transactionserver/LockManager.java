package transactionserver;

import java.util.HashMap;
import utils.Account;
import utils.LockTypes;

public class LockManager implements LockTypes {
    
    private HashMap<Integer, Account> theLocks;
    public int waitType;
    
    
    public LockManager(){
        theLocks = new HashMap<>();
        waitType = NONE;
    }
    
    //Object is an account
    public synchronized void setLock(int accountID, Account objAccount, int transID, int lockType){
        
        //Check if something is conflict, checks for cases where you it is not a conflict -> rest is conflicts.
        //boolean isConfict
        // no conflict if:
        // 1) lock holder empty -> all other holds lock holders.
        // 2) you are sole lock holder.
        // 3) currentlock type is read and newlocktype is also read
        // else lock is not conflict.
            
        //Check hashmap for current accountID
        if(theLocks.containsKey(accountID)){ //Don't have to insert this account to hashmap
            
            if(lockType == READ && objAccount.getLockType() == READ) {
                // Share a read lock
                objAccount.setLockType(lockType);
                System.out.println("[Lock Manager][Transaction "+transID+"] Sharing read lock on account "+accountID+".");
                
            } else if(objAccount.getLockType() == NONE) {
                objAccount.setLockType(lockType);
                System.out.println("[Lock Manager][Transaction "+transID+"] Set type "+lockType+" on account "+accountID+".");
                
            } else {
            
            // set Write with read -> wait
            // set Read with a write -> wait
            // set Write with write -> wait
            // set either with none
            
            //if(lockType == WRITE){
            //    waitType = NONE;
            //}
            if(lockType == READ){
                waitType = READ;
                //System.out.println("Wait type is READ for account "+accountID);
            }
            System.out.println("Wait type is "+waitType+" lock type on account "+accountID+" is "+objAccount.getLockType());
            while(objAccount.getLockType() != 3 || objAccount.getLockType() != waitType) {
                try {
                    System.out.println("waiting");
                    wait();
                }catch ( InterruptedException e){/*...*/ }
            }
            waitType = NONE;
            
            //Out of waiting loop, can set locks now
            objAccount.setLockType(lockType);
            System.out.println("[Lock Manager][Transaction "+transID+"] Set type "+lockType+" on account "+accountID+".");
            }
            
        }
        // If there is not lock on the account and not in hash map.
        else{
            objAccount.setLockType(lockType);
            theLocks.put(accountID, objAccount);
            System.out.println("[Lock Manager][Transaction "+transID+"] Set type "+lockType+" on account "+accountID+".");
            
        }   
    }
    // synchronize this one because we want to remove all entries
    public synchronized void unLock(int accountID, Account objAccount, int transID) {
        objAccount.removeLock();
        System.out.println("[Lock Manager][Transaction "+transID+"] Removed lock on account "+accountID+".");
    
    }
    
}