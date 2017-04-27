package transactionserver;

public class TransactionManager {
    
    private int transID;
    public TransactionManager(){
    
    }
    
    public int assignTransID(){
        return transID++;
    }
    
    // Keep track of transactions? Array...?
    
    
}
