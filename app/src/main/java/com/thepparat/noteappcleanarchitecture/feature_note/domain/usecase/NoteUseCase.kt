package com.thepparat.noteappcleanarchitecture.feature_note.domain.usecase

data class NoteUseCase(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteUseCase: GetNoteUseCase
)
