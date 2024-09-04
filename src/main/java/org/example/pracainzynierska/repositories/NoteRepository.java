package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.models.Note;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository <Note, Long> {

    @Query("SELECT n from Note n WHERE n.note_owner_id = :userid")
    List<Note> findAllUserNotes(@Param("userid") Long userid);

    @Query("SELECT n from Note n WHERE n.id = :id")
    Optional<Note> findById(@Param("id") Long id);

    @Modifying
    @Query(value = "INSERT INTO Note n (n.title, n.tag, n.favourite, n.content, n.imageUrl, n.fileUrl, n.note_owner_id) VALUES (:title, :tag, :favourite, :content, :imageUrl, :fileUrl, :note_owner_id);", nativeQuery = true)
    void create(@Param("title") String title,
                @Param("tag") String tag,
                @Param("favourite") boolean favourite,
                @Param("content") String content,
                @Param("imageUrl") String imageUrl,
                @Param("fileUrl") String fileUrl,
                @Param("note_owner_id") Long note_owner_id);


    @Modifying
    @Query("DELETE FROM Note n WHERE n.id = :id")
    void delete(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Note n SET n.title = :title, n.tag = :tag, n.favourite = :favourite, n.content = :content, n.imageUrl = :imageUrl, n.fileUrl = :fileUrl WHERE n.id = :id")
    void update(@Param("title") String title,
                @Param("tag") String tag,
                @Param("favourite") boolean favourite,
                @Param("content") String content,
                @Param("imageUrl") String imageUrl,
                @Param("fileUrl") String fileUrl,
                @Param("id") Long id);

}
