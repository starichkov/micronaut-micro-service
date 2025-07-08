package com.templatetasks.java.micronaut.service;

import com.templatetasks.java.micronaut.data.Note;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:20
 */
public interface NoteService extends DataAccessService<Note, Long> {

    /**
     * Add a tag to a note
     *
     * @param noteId the ID of the note
     * @param tagId the ID of the tag to add
     * @return the updated note, or null if the note or tag doesn't exist
     */
    Note addTag(Long noteId, Long tagId);

    /**
     * Remove a tag from a note
     *
     * @param noteId the ID of the note
     * @param tagId the ID of the tag to remove
     * @return the updated note, or null if the note or tag doesn't exist
     */
    Note removeTag(Long noteId, Long tagId);
}
