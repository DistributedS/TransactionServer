package transactionserver;

import java.util.HashMap;
import utils.Account;
import utils.LockTypes;

public class LockManager implements LockTypes {
    
    private HashMap<Integer, Account> theLocks;
    public int waitType;
    
    
    public LockManager(){
        theLocks = new HashMap<>();
    }
    
    //Object is an account
    public synchronized void setLock(Account objAccount, int transID, int lockType){
 
        //Check hashmap for current accountID
        if(theLocks.containsKey(objAccount.getAccountID())){ //Don't have to insert this account to hashmap
            
            while(isConflict(objAccount, transID, lockType)) {
                try {
                    System.out.println("[Transaction "+transID+"] Waiting on account "+objAccount.getAccountID()+".");
                    wait();
                }catch ( InterruptedException e){/*...*/ }
            }
            
            //Out of waiting loop, can set locks now
            objAccount.setLockType(lockType);
            System.out.println("ALREADY ADDED [Lock Manager][Transaction "+transID+"]: Set type "+lockType+" on account "+objAccount.getAccountID()+".");
             
        // If there is not lock on the account and not in hash map.
        }else{
            objAccount.setLockType(lockType);
            theLocks.put(objAccount.getAccountID(), objAccount);
            System.out.println("ADDED [Lock Manager][Transaction "+transID+"]: Set type "+lockType+" on account "+objAccount.getAccountID()+".");
        }
    }
    
    // synchronize this one because we want to remove all entries
    public synchronized void unLock(Account objAccount, int transID) {
        objAccount.removeLock();
        System.out.println("[Lock Manager][Transaction "+transID+"]: Removed lock on account "+objAccount.getAccountID()+".");
        notifyAll(); 
    }
    
    //Check if something is conflict, checks for cases where you it is not a conflict -> rest is conflicts.
    //boolean isConfict
    // no conflict if:
    // 1) lock holder empty -> all other holds lock holders.
    // 2) you are sole lock holder.
    // 3) currentlock type is read and newlocktype is also read
    // else lock is not conflict.
    public boolean isConflict(Account objAccount, int transID, int lockType){
        // Nobody else holds the lock
        if(objAccount.getLockType() == NONE) {
            return false;
        }
        // Current lock is read on Account and new lock is also read
        if (lockType == READ && objAccount.getLockType() == READ) {
            return false;
        }
        
        return true;
    }
}