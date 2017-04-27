package transactionserver;


import java.util.HashMap;
import utils.LockTypes;

public class LockManager {
    
    private HashMap theLocks;
    
    //Object is an account
    public void setLock(int accountID, int transID, LockTypes lockType){
        //Lock foundLock = null;
        synchronized(this){
        // find the lock associated with object
        // if there isn’t one, create it and add it to the hashtable
        }
        foundLock.acquire(trans, lockType);
    }
    // synchronize this one because we want to remove all entries
    public synchronized void unLock(int transID) {
        Enumeration e = theLocks.elements();
        while(e.hasMoreElements()){
            Lock aLock = (Lock)(e.nextElement());
            if(/* trans is a holder of this lock*/ ) aLock.release(trans);
        }
    }
    
    
    public synchronized boolean isConflict(){
        
    }
}