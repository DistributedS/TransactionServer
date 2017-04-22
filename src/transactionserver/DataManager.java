package transactionserver;

import static java.util.Arrays.fill;

public class DataManager {
    
    int accounts[];
    
    public DataManager(){
        accounts = new int[10];
        fill(accounts, 10);
    }
}
