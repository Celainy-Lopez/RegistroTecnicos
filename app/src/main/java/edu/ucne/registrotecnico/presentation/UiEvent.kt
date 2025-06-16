package edu.ucne.registrotecnico.presentation

sealed class UiEvent {
    object NavigateUp : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}