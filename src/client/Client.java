package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import utils.Message;
import static utils.MessageTypes.CLOSE_TRANS;
import static utils.MessageTypes.OPEN_TRANS;
import static utils.MessageTypes.READ;
import static utils.MessageTypes.WRITE;
import utils.Transaction;


public class Client {

    String host = "127.0.0.1";
    static final int port = 80085;
    Random random = new Random();
    
    public void run() {
        try { 
            // connect to application server
            Socket server = new Socket(host, port);
            
            //Setup Object Streams
            ObjectOutputStream writeToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream readFromServer = new ObjectInputStream(server.getInputStream());
            
            Message message;
            //Open Transaction
            message = new Message(OPEN_TRANS, null);
            Transaction readObject;
            writeToServer.writeObject(message);
            
            // Read account balance 1
            int accountID = getRandomInt(0, 9);
            readObject = new Transaction(accountID, 0);
            message = new Message(READ, readObject);
            writeToServer.writeObject(message);
            
            // Recieve account balance 1 and print output
            int balance = (Integer) readFromServer.readObject();
            System.out.println("Account "+accountID+" has balance: "+balance);
            
            // Withdraw account balance 1
            int transferAmount = getRandomInt(0, 10);
            Transaction transObject = new Transaction(accountID, -transferAmount);
            message = new Message(WRITE, transObject);
            System.out.println("Withdrawing from account "+accountID+" with amount "+transferAmount);
            writeToServer.writeObject(message);
            
            // Recieve account balance 1 and print output
            int postBalance = (Integer) readFromServer.readObject();
            System.out.println("Account "+accountID+" has new balance after withdraw: "+postBalance);
            
            // Read balance of account to recieve funds
            int depositAccountID = getRandomInt(0, 9);
            readObject = new Transaction(depositAccountID, 0);
            message = new Message(READ, readObject);
            writeToServer.writeObject(message);
            
            // Recieve account balance 2 and print output
            balance = (Integer) readFromServer.readObject();
            System.out.println("Account "+depositAccountID+" has balance "+balance+" before deposit.");
            
            // Withdraw account balance 1
            transObject = new Transaction(depositAccountID, transferAmount);
            message = new Message(WRITE, transObject);
            System.out.println("Depositing in account "+depositAccountID+" with amount "+transferAmount);
            writeToServer.writeObject(message);
            
            // Close Transaction
            message = new Message(CLOSE_TRANS, null);
            writeToServer.writeObject(message);
            

        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("[Client.run] Error occurred");
        }
    }
    
    public int getRandomInt(int min, int max){
        return random.nextInt(max +1 - min) + min;
    }
    
    
    public static void main(String[] args) {
        
        Client client = new Client();
        
        client.run();
    } 
}


