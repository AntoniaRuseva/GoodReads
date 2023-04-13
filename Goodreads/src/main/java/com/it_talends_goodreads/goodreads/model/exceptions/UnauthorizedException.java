package com.it_talends_goodreads.goodreads.model.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String msg){
        super (msg);
    }
}
