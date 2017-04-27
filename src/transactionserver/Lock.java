package transactionserver;

import java.util.Vector;
import utils.LockTypes;

public class Lock {
    private Object object;
    // the object being protected by the lock
    private Vector holders;
    // the TIDs of current holders
    private LockTypes lockType; // the current type
    
    public synchronized void acquire(TransID trans, LockTypes aLockType ){
        //Check if something is conflict, checks for cases where you it is not a conflict -> rest is conflicts.
        //boolean isConfict
        // no conflict if:
        // 1) lock holder empty -> all other holds lock holders.
        // 2) you are sole lock holder.
        // 3) currentlock type is read and newlocktype is also read
        // else lock is not conflict.
        while(/*another transaction holds the lock in conflicting mode*/) {
        try {
        wait();
        }catch ( InterruptedException e){/*...*/ }
        }
        if (holders.isEmpty()) { // no TIDs hold lock
        holders.addElement(trans);
        lockType = aLockType;
        } else if (/*another transaction holds the lock, share it*/ ) ){
            if (/* this transaction not a holder*/) holders.addElement(trans);
        } else if (/* this transaction is a holder but needs a more exclusive lock*/)
            lockType.promote();
    }
    }

    public synchronized void release(TransID trans ){
    holders.removeElement(trans); // remove this holder
    // set locktype to none
    notifyAll();
    }
    
    
}
