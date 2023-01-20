package com.thepparat.noteappcleanarchitecture.feature_note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thepparat.noteappcleanarchitecture.feature_note.presentation.notes.components.NoteItem
import com.thepparat.noteappcleanarchitecture.feature_note.presentation.notes.components.OrderSection
import com.thepparat.noteappcleanarchitecture.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    notesViewModel: NotesViewModel = hiltViewModel()
) {
    val state = notesViewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditNoteScreen.route) },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your note", style = MaterialTheme.typography.h4)
                IconButton(onClick = { notesViewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        notesViewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = modifier.height(16.dp))
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(note = note, modifier = modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                        }, onDeleteClick = {
                        notesViewModel.onEvent(NotesEvent.DeleteNote(note))
                        scope.launch {
                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                message = "Note deleted",
                                actionLabel = "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                notesViewModel.onEvent(NotesEvent.RestoreNote)
                            }
                        }
                    }
                    )
                    Spacer(modifier = modifier.height(16.dp))
                }
            }
        }

    }
}