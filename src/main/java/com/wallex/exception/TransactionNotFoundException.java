package com.wallex.exception; 

public class TransactionNotFoundException extends RuntimeException{


     public TransactionNotFoundException(Long id){
        super ("The transaction not found: " + id);
     }
}