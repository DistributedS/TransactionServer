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
    
    static final int serverPort = 80085;
    ServerSocket serverSocket;
    transactionserver.DataManager dataManager;
    transactionserver.LockManager lockManager;
    transactionserver.TransactionManager transactionManager;
    
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
    // ...

        try{
            while(true){
            System.out.println("Waiting for connections on Port");
            //Accept incomming connections.
            Socket connectionToServer  = serverSocket.accept();
            System.out.println("A connection is established!");
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
            
            // reading message
            try {
                message = (Message) readFromClient.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("[TransactionServer.run] Message could not be read from object stream.");
                e.printStackTrace();
                System.exit(1);
            }

            switch (message.getType()) {
                case OPEN_TRANS:
                    // Use transaction manager
                    transID = transactionManager.assignTransID();
                    System.out.println("Opened Transaction.");
                    
                    break;

                case CLOSE_TRANS:
                    // Use transaction manager
                    System.out.println("Transaction Finished");
                    break;
                    
                case READ:
                    try{
                        // Use data manager
                        transaction = (Transaction) message.getContent();
                        int accountID = transaction.getAccountID();
                        // TODO Call datamansger to get balance.
                        Object resultObject = null;
                        writeToClient.writeObject(resultObject);
                        
                    } catch(Exception e){
                        System.out.println("Error in read.");
                    }
                    break;
                    
                case WRITE:
                    try {
                        transaction = (Transaction) message.getContent();
                        int accountID = transaction.getAccountID();
                        int transferAmt = transaction.getAmount();
                        // TODO Call datamansger to do transaction.
                        Object resultObject = null;
                        writeToClient.writeObject(resultObject);
                        
                    } catch(Exception e){
                        System.out.println("Error in write.");
                    }

                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }
    
    public static void main(String[] args) {
        // start the application server
        TransactionServer server = new TransactionServer();

        server.run();
    }
}
