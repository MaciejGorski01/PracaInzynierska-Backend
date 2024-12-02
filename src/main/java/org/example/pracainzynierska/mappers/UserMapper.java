package org.example.pracainzynierska.mappers;

import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper implements RowMapper<List<User>> {
    JsonValidator jsonValidator = new JsonValidator("schemas/user_schema.json");

    public List<User> mapRow(ResultSet rs, int rowNum) throws SQLException {

        String json = rs.getString("users_json");
        List<User> users = new ArrayList<>();

        if (json != null) {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonValidator.validator(jsonObject);

                User user = new User();
                user.setId(jsonObject.optString("id"));
                user.setPassword(jsonObject.optString("password"));
                user.setEmail(jsonObject.optString("email"));
                user.setName(jsonObject.optString("name"));
                user.setSurname(jsonObject.optString("surname"));

                users.add(user);
            }
        }
        return users;

    }
}
