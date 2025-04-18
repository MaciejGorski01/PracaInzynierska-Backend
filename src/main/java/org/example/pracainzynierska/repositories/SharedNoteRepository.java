package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.dtos.SharedNoteWithDetailsDto;
import org.example.pracainzynierska.mappers.SharedNoteMapper;
import org.example.pracainzynierska.mappers.SharedNoteWithDetailsMapper;
import org.example.pracainzynierska.models.SharedNote;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SharedNoteRepository {

    private final JdbcTemplate jdbcTemplate;

    public SharedNoteRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<SharedNote> findSharedNote(String id){
        String sql = "SELECT json_agg(row_to_json(\"SharedNote\")) AS sharedNotes_json FROM \"SharedNote\" WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, new SharedNoteMapper(), id);
    }

    public List<SharedNoteWithDetailsDto> findAllUserSharedNote(String email){
        String sql = "SELECT json_agg(json_build_object( " +
                "'note_id', sn.note_id, " +
                "'shared_with_user_email', sn.shared_with_user_email, " +
                "'title', n.title, " +
                "'tag', n.tag, " +
                "'favourite', n.favourite, " +
                "'content', n.content, " +
                "'color', n.color, " +
                "'fileUrl', n.\"fileUrl\", " +
                "'note_owner_id', n.note_owner_id" +
                ")) AS sharedNotesWithDetails_json " +
                "FROM \"SharedNote\" sn " +
                "JOIN \"Note\" n ON sn.note_id = n.id " +
                "WHERE sn.shared_with_user_email LIKE ?";
        return jdbcTemplate.queryForObject(sql, new SharedNoteWithDetailsMapper(), email);
    }

    public List<SharedNoteWithDetailsDto> findUserSharedNoteWithDetails(String email, String id){
        String sql = "SELECT json_agg(json_build_object( " +
                "'note_id', sn.note_id, " +
                "'shared_with_user_email', sn.shared_with_user_email, " +
                "'title', n.title, " +
                "'tag', n.tag, " +
                "'favourite', n.favourite, " +
                "'content', n.content, " +
                "'color', n.color, " +
                "'fileUrl', n.\"fileUrl\", " +
                "'note_owner_id', n.note_owner_id" +
                ")) AS sharedNotesWithDetails_json " +
                "FROM \"SharedNote\" sn " +
                "JOIN \"Note\" n ON sn.note_id = n.id " +
                "WHERE sn.shared_with_user_email LIKE ? AND " +
                "sn.note_id = ?";
        return jdbcTemplate.queryForObject(sql, new SharedNoteWithDetailsMapper(), email, id);
    }

    public int create(String id, JSONObject jsonObject){
        String sql = "SELECT insert_sharednote_from_json(?, ?::json);";
        Optional<Integer> result = Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, Integer.class, id, jsonObject.toString())
        );

        return result.orElse(0);
    }

    public void delete(String note_id, String shared_with_user_email){
        String sql = "DELETE FROM \"SharedNote\" WHERE note_id = ? AND shared_with_user_email = ?;";
        jdbcTemplate.update(sql, note_id, shared_with_user_email);
    }

}
