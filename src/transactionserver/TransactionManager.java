package transactionserver;

public class TransactionManager {
    
    private int transID;
    public TransactionManager(){
    
    }
    
    public synchronized int assignTransID(){
        return transID++;
    }
    
    // Keep track of transactions? Array...?
    
    
}
