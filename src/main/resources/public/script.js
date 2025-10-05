// Global variables to store current state
let currentNoteId = null;
let currentTagId = null;
let allNotes = [];
let allTags = [];
let selectedNoteForTagManagement = null;

// DOM elements
const noteForm = document.getElementById('note-form');
const noteIdInput = document.getElementById('note-id');
const noteTitleInput = document.getElementById('note-title');
const noteContentInput = document.getElementById('note-content');
const notesContainer = document.getElementById('notes-container');
const cancelNoteButton = document.getElementById('cancel-note');

const tagForm = document.getElementById('tag-form');
const tagIdInput = document.getElementById('tag-id');
const tagLabelInput = document.getElementById('tag-label');
const tagsContainer = document.getElementById('tags-container');
const cancelTagButton = document.getElementById('cancel-tag');

const noteTagManagement = document.querySelector('.note-tag-management');
const selectedNoteTitle = document.getElementById('selected-note-title');
const availableTagsContainer = document.getElementById('available-tags-container');
const noteTagsContainer = document.getElementById('note-tags-container');
const closeTagManagementButton = document.getElementById('close-tag-management');

// API endpoints
const API = {
    notes: '/v1/notes',
    tags: '/v1/tags',
    noteTag: (noteId, tagId) => `/v1/notes/${noteId}/tags/${tagId}`
};

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    // Initialize the app
    fetchAllNotes();
    fetchAllTags();
    
    // Set up event listeners
    noteForm.addEventListener('submit', handleNoteSubmit);
    cancelNoteButton.addEventListener('click', resetNoteForm);
    
    tagForm.addEventListener('submit', handleTagSubmit);
    cancelTagButton.addEventListener('click', resetTagForm);
    
    closeTagManagementButton.addEventListener('click', closeTagManagement);
});

// Notes CRUD operations
async function fetchAllNotes() {
    try {
        const response = await fetch(API.notes);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        // Handle paginated response - extract content array
        allNotes = Array.isArray(data) ? data : (data.content || []);
        renderNotes();
    } catch (error) {
        console.error('Error fetching notes:', error);
        showError('Failed to load notes. Please try again later.');
    }
}

async function fetchNote(id) {
    try {
        const response = await fetch(`${API.notes}/${id}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching note ${id}:`, error);
        showError('Failed to load note details. Please try again later.');
        return null;
    }
}

async function createNote(note) {
    try {
        const response = await fetch(API.notes, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(note)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const createdNote = await response.json();
        allNotes.push(createdNote);
        renderNotes();
        resetNoteForm();
        return createdNote;
    } catch (error) {
        console.error('Error creating note:', error);
        showError('Failed to create note. Please try again.');
        return null;
    }
}

async function updateNote(id, note) {
    try {
        const response = await fetch(`${API.notes}/${id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(note)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const updatedNote = await response.json();
        allNotes = allNotes.map(n => n.id === id ? updatedNote : n);
        renderNotes();
        resetNoteForm();
        return updatedNote;
    } catch (error) {
        console.error(`Error updating note ${id}:`, error);
        showError('Failed to update note. Please try again.');
        return null;
    }
}

async function deleteNote(id) {
    try {
        const response = await fetch(`${API.notes}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        allNotes = allNotes.filter(note => note.id !== id);
        renderNotes();
        return true;
    } catch (error) {
        console.error(`Error deleting note ${id}:`, error);
        showError('Failed to delete note. Please try again.');
        return false;
    }
}

// Tags CRUD operations
async function fetchAllTags() {
    try {
        const response = await fetch(API.tags);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        // Handle paginated response - extract content array
        allTags = Array.isArray(data) ? data : (data.content || []);
        renderTags();
    } catch (error) {
        console.error('Error fetching tags:', error);
        showError('Failed to load tags. Please try again later.');
    }
}

async function fetchTag(id) {
    try {
        const response = await fetch(`${API.tags}/${id}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching tag ${id}:`, error);
        showError('Failed to load tag details. Please try again later.');
        return null;
    }
}

async function createTag(tag) {
    try {
        const response = await fetch(API.tags, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tag)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const createdTag = await response.json();
        allTags.push(createdTag);
        renderTags();
        resetTagForm();
        return createdTag;
    } catch (error) {
        console.error('Error creating tag:', error);
        showError('Failed to create tag. Please try again.');
        return null;
    }
}

async function updateTag(id, tag) {
    try {
        const response = await fetch(`${API.tags}/${id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tag)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const updatedTag = await response.json();
        allTags = allTags.map(t => t.id === id ? updatedTag : t);
        renderTags();
        resetTagForm();
        return updatedTag;
    } catch (error) {
        console.error(`Error updating tag ${id}:`, error);
        showError('Failed to update tag. Please try again.');
        return null;
    }
}

async function deleteTag(id) {
    try {
        const response = await fetch(`${API.tags}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        allTags = allTags.filter(tag => tag.id !== id);
        renderTags();
        return true;
    } catch (error) {
        console.error(`Error deleting tag ${id}:`, error);
        showError('Failed to delete tag. Please try again.');
        return false;
    }
}

// Note-Tag relationship operations
async function addTagToNote(noteId, tagId) {
    try {
        const response = await fetch(API.noteTag(noteId, tagId), {
            method: 'POST'
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const updatedNote = await response.json();
        // Update the note in our local array
        allNotes = allNotes.map(n => n.id === noteId ? updatedNote : n);
        renderNotes();
        
        if (selectedNoteForTagManagement && selectedNoteForTagManagement.id === noteId) {
            selectedNoteForTagManagement = updatedNote;
            renderTagManagement();
        }
        
        return updatedNote;
    } catch (error) {
        console.error(`Error adding tag ${tagId} to note ${noteId}:`, error);
        showError('Failed to add tag to note. Please try again.');
        return null;
    }
}

async function removeTagFromNote(noteId, tagId) {
    try {
        const response = await fetch(API.noteTag(noteId, tagId), {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const updatedNote = await response.json();
        // Update the note in our local array
        allNotes = allNotes.map(n => n.id === noteId ? updatedNote : n);
        renderNotes();
        
        if (selectedNoteForTagManagement && selectedNoteForTagManagement.id === noteId) {
            selectedNoteForTagManagement = updatedNote;
            renderTagManagement();
        }
        
        return updatedNote;
    } catch (error) {
        console.error(`Error removing tag ${tagId} from note ${noteId}:`, error);
        showError('Failed to remove tag from note. Please try again.');
        return null;
    }
}

// UI Event Handlers
function handleNoteSubmit(event) {
    event.preventDefault();
    
    const note = {
        title: noteTitleInput.value.trim(),
        content: noteContentInput.value.trim()
    };
    
    if (!note.title) {
        showError('Note title is required.');
        return;
    }
    
    if (currentNoteId) {
        updateNote(currentNoteId, note);
    } else {
        createNote(note);
    }
}

function handleTagSubmit(event) {
    event.preventDefault();
    
    const tag = {
        label: tagLabelInput.value.trim()
    };
    
    if (!tag.label) {
        showError('Tag label is required.');
        return;
    }
    
    if (currentTagId) {
        updateTag(currentTagId, tag);
    } else {
        createTag(tag);
    }
}

function editNote(id) {
    const note = allNotes.find(note => note.id === id);
    if (note) {
        currentNoteId = note.id;
        noteIdInput.value = note.id;
        noteTitleInput.value = note.title || '';
        noteContentInput.value = note.content || '';
    }
}

function editTag(id) {
    const tag = allTags.find(tag => tag.id === id);
    if (tag) {
        currentTagId = tag.id;
        tagIdInput.value = tag.id;
        tagLabelInput.value = tag.label || '';
    }
}

function resetNoteForm() {
    currentNoteId = null;
    noteForm.reset();
}

function resetTagForm() {
    currentTagId = null;
    tagForm.reset();
}

function openTagManagement(noteId) {
    const note = allNotes.find(note => note.id === noteId);
    if (note) {
        selectedNoteForTagManagement = note;
        selectedNoteTitle.textContent = note.title;
        noteTagManagement.style.display = 'block';
        renderTagManagement();
    }
}

function closeTagManagement() {
    selectedNoteForTagManagement = null;
    noteTagManagement.style.display = 'none';
}

function handleTagClick(tagId) {
    if (!selectedNoteForTagManagement) return;
    
    const noteId = selectedNoteForTagManagement.id;
    const noteTags = selectedNoteForTagManagement.tags || [];
    const hasTag = noteTags.some(tag => tag.id === tagId);
    
    if (hasTag) {
        removeTagFromNote(noteId, tagId);
    } else {
        addTagToNote(noteId, tagId);
    }
}

// Rendering functions
function renderNotes() {
    notesContainer.innerHTML = '';
    
    if (allNotes.length === 0) {
        notesContainer.innerHTML = '<p>No notes found. Create one!</p>';
        return;
    }
    
    allNotes.forEach(note => {
        const noteCard = document.createElement('div');
        noteCard.className = 'note-card';
        
        const title = document.createElement('h4');
        title.textContent = note.title;
        
        const content = document.createElement('p');
        content.textContent = note.content || 'No content';
        
        const tags = document.createElement('div');
        tags.className = 'note-tags-list';
        if (note.tags && note.tags.length > 0) {
            note.tags.forEach(tag => {
                const tagSpan = document.createElement('span');
                tagSpan.className = 'tag-item';
                tagSpan.textContent = tag.label;
                tags.appendChild(tagSpan);
            });
        } else {
            const noTags = document.createElement('span');
            noTags.textContent = 'No tags';
            noTags.style.fontStyle = 'italic';
            tags.appendChild(noTags);
        }
        
        const actions = document.createElement('div');
        actions.className = 'note-actions';
        
        const editButton = document.createElement('button');
        editButton.className = 'edit-btn';
        editButton.textContent = 'Edit';
        editButton.addEventListener('click', () => editNote(note.id));
        
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-btn';
        deleteButton.textContent = 'Delete';
        deleteButton.addEventListener('click', () => {
            if (confirm('Are you sure you want to delete this note?')) {
                deleteNote(note.id);
            }
        });
        
        const manageTagsButton = document.createElement('button');
        manageTagsButton.className = 'manage-tags-btn';
        manageTagsButton.textContent = 'Manage Tags';
        manageTagsButton.addEventListener('click', () => openTagManagement(note.id));
        
        actions.appendChild(editButton);
        actions.appendChild(deleteButton);
        actions.appendChild(manageTagsButton);
        
        noteCard.appendChild(title);
        noteCard.appendChild(content);
        noteCard.appendChild(tags);
        noteCard.appendChild(actions);
        
        notesContainer.appendChild(noteCard);
    });
}

function renderTags() {
    tagsContainer.innerHTML = '';
    
    if (allTags.length === 0) {
        tagsContainer.innerHTML = '<p>No tags found. Create one!</p>';
        return;
    }
    
    allTags.forEach(tag => {
        const tagCard = document.createElement('div');
        tagCard.className = 'tag-card';
        
        const label = document.createElement('h4');
        label.textContent = tag.label;
        
        const actions = document.createElement('div');
        actions.className = 'tag-actions';
        
        const editButton = document.createElement('button');
        editButton.className = 'edit-btn';
        editButton.textContent = 'Edit';
        editButton.addEventListener('click', () => editTag(tag.id));
        
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-btn';
        deleteButton.textContent = 'Delete';
        deleteButton.addEventListener('click', () => {
            if (confirm('Are you sure you want to delete this tag?')) {
                deleteTag(tag.id);
            }
        });
        
        actions.appendChild(editButton);
        actions.appendChild(deleteButton);
        
        tagCard.appendChild(label);
        tagCard.appendChild(actions);
        
        tagsContainer.appendChild(tagCard);
    });
}

function renderTagManagement() {
    if (!selectedNoteForTagManagement) return;
    
    // Render available tags
    availableTagsContainer.innerHTML = '';
    if (allTags.length === 0) {
        availableTagsContainer.innerHTML = '<p>No tags available. Create some tags first!</p>';
    } else {
        allTags.forEach(tag => {
            const tagItem = document.createElement('div');
            tagItem.className = 'tag-item';
            
            // Check if this tag is already associated with the note
            const noteTags = selectedNoteForTagManagement.tags || [];
            const isSelected = noteTags.some(t => t.id === tag.id);
            
            if (isSelected) {
                tagItem.classList.add('selected');
            }
            
            tagItem.textContent = tag.label;
            tagItem.addEventListener('click', () => handleTagClick(tag.id));
            
            availableTagsContainer.appendChild(tagItem);
        });
    }
    
    // Render note's tags
    noteTagsContainer.innerHTML = '';
    const noteTags = selectedNoteForTagManagement.tags || [];
    if (noteTags.length === 0) {
        noteTagsContainer.innerHTML = '<p>This note has no tags yet.</p>';
    } else {
        noteTags.forEach(tag => {
            const tagItem = document.createElement('div');
            tagItem.className = 'tag-item selected';
            tagItem.textContent = tag.label;
            tagItem.addEventListener('click', () => handleTagClick(tag.id));
            
            noteTagsContainer.appendChild(tagItem);
        });
    }
}

// Utility functions
function showError(message) {
    alert(message);
}