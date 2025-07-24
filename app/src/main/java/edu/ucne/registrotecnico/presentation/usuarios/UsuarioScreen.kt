package edu.ucne.registrotecnico.presentation.usuarios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.presentation.UiEvent
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun UsuarioScreen(
    viewModel: UsuariosViewModel = hiltViewModel(), usuarioId: Int? = null, goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UsuarioBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        viewModel = viewModel
    )

    LaunchedEffect(usuarioId) {
        usuarioId?.let {
            if (it > 0) {
                viewModel.onEvent(UsuarioEvent.GetUsuario(it))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBodyScreen(
    uiState: UsuarioUiState,
    onEvent: (UsuarioEvent) -> Unit,
    goBack: () -> Unit,
    viewModel: UsuariosViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateUp -> goBack()
                is UiEvent.ShowSnackbar -> TODO()
            }
        }
    }

    LaunchedEffect(uiState.isSuccess || !uiState.errorMessage.isNullOrBlank()) {
        if (uiState.isSuccess && !uiState.successMessage.isNullOrBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.successMessage, duration = SnackbarDuration.Short
                )
                onEvent(UsuarioEvent.ResetSuccessMessage)
            }
        } else if (!uiState.errorMessage.isNullOrBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.errorMessage, duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (uiState.isSuccess) Color.Green.copy(alpha = 0.8f) else Color.Red.copy(
                        alpha = 0.2f
                    )
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro Usuarios",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }, navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.surface,
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(
                        value = uiState.usuarioId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    OutlinedTextField(
                        value = uiState.nombre ?: "",
                        onValueChange = { onEvent(UsuarioEvent.NombreChange(it)) },
                        label = { Text("Nombre") },
                        placeholder = { Text("CXC") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = !uiState.errorNombre.isNullOrBlank(),
                        supportingText = {
                            uiState.errorNombre?.let { error ->
                                Text(text = error, color = Color.Red)
                            }
                        })

                    val decimalFormat = DecimalFormat("#.##")

                    OutlinedTextField(
                        label = { Text("Balance") },
                        placeholder = { Text("Ej: 25000.00") },
                        value = if (uiState.balance == 0.0) "" else decimalFormat.format(uiState.balance),
                        onValueChange = {
                            val parsed = it.toDoubleOrNull()
                            if (parsed != null) {
                                onEvent(UsuarioEvent.BalanceChange(parsed))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiState.balance != null && uiState.balance <= 0 && !uiState.errorBalance.isNullOrBlank(),
                        supportingText = {
                            uiState.errorBalance?.let { error ->
                                Text(text = error, color = Color.Red)
                            }
                        }

                    )

                    Spacer(modifier = Modifier.height(32.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(UsuarioEvent.Nuevo) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo")
                            Text("Nuevo")
                        }

                        OutlinedButton(
                            onClick = { onEvent(UsuarioEvent.PostUsuario) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}