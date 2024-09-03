package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.models.Note;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends CrudRepository <Note, Long> {

    @Query("SELECT n from Note n WHERE n.note_owner_id = :userid")
    List<Note> findAllUserNotes(@Param("userid") Long userid);

    @Modifying
    @Query(value = "INSERT INTO Note n (n.title, n.tag, n.favourite, n.content, n.imageUrl, n.fileUrl, n.note_owner_id) VALUES (:title, :tag, :favourite, :content, :imageUrl, :fileUrl, :note_owner_id);", nativeQuery = true)
    void create(@Param("title") String title,
                @Param("tag") String tag,
                @Param("favourite") boolean favourite,
                @Param("content") String content,
                @Param("imageUrl") String imageUrl,
                @Param("fileUrl") String fileUrl,
                @Param("note_owner_id") Long note_owner_id);
}
