/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;


import static utils.LockTypes.NONE;

/**
 *
 * @author peter
 */
public class Account implements LockTypes {
    
    private int lockType;
    private int numReadLocks;
    private int balance;
    private int accountID;
    
    
    public Account(int balance, int accountID){
        lockType = LockTypes.NONE;
        numReadLocks = 0;
        this.balance = balance;
        this.accountID = accountID;
    }
    
    public int getLockType(){
        return lockType;
    }
    
    public int getAccountID(){
        return accountID;
    }
    
    public void setLockType(int type){
        lockType = type;
        if(type == READ){
            this.addReadLock();
        }
    }
    
    public void addReadLock(){
        numReadLocks += 1;
    }
    
    public int totalReadLocks(){
        return numReadLocks;
    }
    
    public void removeReadLock(){
        numReadLocks -= 1;
        if(numReadLocks <= 0){
            this.setLockType(NONE);
        }
    }
    
    public void removeLock(){
        if(this.getLockType() == READ){
            this.removeReadLock();
        }
        else{
            this.setLockType(NONE);
        }
    }
    
    public void addToBalance(int money){
        balance += money;
    }
    
    public int getBalance(){
        return balance;
    }
    
    
    
}
