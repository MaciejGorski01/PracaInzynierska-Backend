package org.example.pracainzynierska.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException (String message){
        super(message);
    }
}
