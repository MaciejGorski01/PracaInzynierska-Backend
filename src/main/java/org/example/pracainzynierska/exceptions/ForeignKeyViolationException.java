package org.example.pracainzynierska.exceptions;

public class ForeignKeyViolationException extends RuntimeException{
    public ForeignKeyViolationException (String message){
        super(message);
    }
}
