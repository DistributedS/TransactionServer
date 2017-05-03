package transactionserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Message;
import static utils.MessageTypes.CLOSE_TRANS;
import static utils.MessageTypes.OPEN_TRANS;
import static utils.MessageTypes.READ;
import static utils.MessageTypes.WRITE;
import utils.Transaction;

public class TransactionServer {
    
    static final int serverPort = 8008;
    ServerSocket serverSocket;
    public static transactionserver.DataManager dataManager;
    public static transactionserver.TransactionManager transactionManager;
    public static transactionserver.LockManager lockManager;
    
    public TransactionServer(){
        
        // Create instance of Managers
        dataManager = new DataManager();
        lockManager = new LockManager();
        transactionManager = new TransactionManager();
        
        // Create server socket
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException ex) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void run() {
    // start serving clients in server loop ...

        try{
            while(true){
            System.out.println("[Transaction Server] Waiting for connections on port "+serverPort);
            //Accept incomming connections.
            Socket connectionToServer  = serverSocket.accept();
            System.out.println("[Transaction Server] A connection is established.");
            //Spin off new thread.
            (new ServerThread(connectionToServer)).start();
          }        

        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    // objects of this helper class communicate with clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectOutputStream writeToClient = null;
        ObjectInputStream readFromClient = null;
        
        Message message = null;
        Transaction transaction;
        int transID;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                // setting up object streams
                // ...
                readFromClient = new ObjectInputStream(client.getInputStream());
                writeToClient = new ObjectOutputStream(client.getOutputStream());
                
                
            } catch (IOException ex) {
                Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            loop: while(true){
                
                // reading message
                try {
                    message = (Message) readFromClient.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("[Transaction Server]: Message could not be read from object stream.");
                    e.printStackTrace();
                    System.exit(1);
                }

                switch (message.getType()) {
                    case OPEN_TRANS:
                        // Use transaction manager
                        transID = transactionManager.assignTransID();
                        
                        try {
                            Object resultObject = transID;
                            
                            writeToClient.writeObject(resultObject);
                            
                            System.out.println("[Transaction Server][Transaction "+transID+"]: Opened Transaction.");

                        } catch(Exception e){
                            System.out.println("[Transaction Server][Transaction "+transID+"]: Error in Open Transaction.");
                        }
                        break;

                    case CLOSE_TRANS:
                        // Use transaction manager
                        dataManager.printAccounts();
                        System.out.println("[Transaction Server][Transaction "+transID+"]: Closed Transaction");
                        break loop;

                    case READ:
                        System.out.println("[Transaction Server][Transaction "+transID+"]: Got Read Instruction.");
                        try{
                            transaction = (Transaction) message.getContent();
                            int accountID = transaction.getAccountID();
                            System.out.println("[Transaction Server][Transaction "+transID+"]: Got TID.");
                     
                            Object resultObject = dataManager.getAccountBalance(accountID, transID);
                            //Object resultObject = transID;
                            
                            writeToClient.writeObject(resultObject);
                            

                        } catch(Exception e){
                            System.out.println("[Transaction Server][Transaction "+transID+"]: Error in read.");
                        }
                        break;

                    case WRITE:
                        System.out.println("[Transaction Server][Transaction "+transID+"]: Got Write Instruction.");
                        try {
                            transaction = (Transaction) message.getContent();
                            //System.out.println("HERE");
                            int accountID = transaction.getAccountID();
                            int transferAmt = transaction.getAmount();

                            //Object resultObject = dataManager.setAccountBalance(accountID, transID, transferAmt);
                            
                            Object resultObject = transID;
                            
                            writeToClient.writeObject(resultObject);

                        } catch(Exception e){
                            System.out.println("[Transaction Server][Transaction "+transID+"]: Error in write.");
                        }

                        break;

                    default:
                        System.err.println("[Transaction Server][Transaction "+transID+"]: Warning: Message type not implemented");
                }
            } // End while loop
        }
    }
    
    public static void main(String[] args) {
        // start the application server
        TransactionServer server = new TransactionServer();

        server.run();
    }
}
