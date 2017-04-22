package transactionserver;

public class TransactionManager {
    
    private int transID;
    public TransactionManager(){
    
    }
    
    public int assignTransID(){
        return transID++;
    }
    
    
}
