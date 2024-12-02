package org.example.pracainzynierska.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entityName){
        super(entityName + " not found");
    }
}
