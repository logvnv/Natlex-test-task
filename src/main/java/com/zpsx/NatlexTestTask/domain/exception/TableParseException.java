package com.zpsx.NatlexTestTask.domain.exception;

public class TableParseException extends RuntimeException{
    public TableParseException(String messageBase, int r, int c){
        super(String.format("%s [%d, %d].", messageBase, r, c));
    }
}
