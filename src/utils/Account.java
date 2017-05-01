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
    
    
    public Account(int balance){
        lockType = LockTypes.NONE;
        numReadLocks = 0;
        this.balance = balance;
    }
    
    public int getLockType(){
        return lockType;
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
        if(numReadLocks < 0){
            numReadLocks = 0;
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
