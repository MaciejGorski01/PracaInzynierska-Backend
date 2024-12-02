package org.example.pracainzynierska.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.example.pracainzynierska.exceptions.ValidationException;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class JsonValidator {

    private final JsonSchema jsonSchema;

    public JsonValidator(String path){
        try {
            InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(path);
            if (schemaStream == null) {
                throw new IllegalStateException("Brak schematu");
            }

//            String schemaString = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);
//
//            JSONObject rawSchema = new JSONObject(schemaString);
//            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
//            this.jsonSchema = schemaFactory.getSchema(rawSchema.toString());

            this.jsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaStream);

        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas ładowania JSON Schema", e);
        }
    }

    public void validator(JSONObject jsonObject){
        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode jsonNode = om.readTree(jsonObject.toString());
            Set<ValidationMessage> validationMessages = jsonSchema.validate(jsonNode);

            if (!validationMessages.isEmpty()) {
                for (ValidationMessage message : validationMessages) {
                    System.out.println("Błąd walidacji: " + message.getMessage());
                }
                throw new ValidationException("JSON nie spełnia wymagań schematu. ");
            }

        } catch (Exception e){
            throw new ValidationException("JSON nie spełnia wymagań schematu.", e.getCause());
        }
    }
}
