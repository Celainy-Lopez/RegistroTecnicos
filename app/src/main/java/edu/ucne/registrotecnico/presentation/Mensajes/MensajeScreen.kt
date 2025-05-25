package edu.ucne.registrotecnico.presentation.mensaje

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.presentation.Mensajes.MensajeEvent
import edu.ucne.registrotecnico.presentation.Mensajes.MensajeUiState
import edu.ucne.registrotecnico.presentation.Mensajes.MensajesViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MensajeScreen(
    viewModel: MensajesViewModel = hiltViewModel(),
    goBack: () -> Unit,
    ticketId: Int
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        viewModel.onEvent(MensajeEvent.TicketChange(ticketId))
    }

    MensajeBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        ticketId = ticketId
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MensajeBodyScreen(
    uiState: MensajeUiState,
    onEvent: (MensajeEvent) -> Unit,
    goBack: () -> Unit,
    ticketId: Int
) {
    var selectedRemitente by remember { mutableStateOf(uiState.remitente ?: "") }
    val listState = rememberLazyListState()


    val mensajesOrdenados = uiState.mensajes.sortedByDescending { it.fecha }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ticket ${ticketId}",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50)
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(mensajesOrdenados) { mensaje ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut()
                    ) {
                        MensajeRow(mensaje)
                    }
                }
            }
            LaunchedEffect(mensajesOrdenados.size) {
                if (mensajesOrdenados.isNotEmpty()) {
                    listState.animateScrollToItem(mensajesOrdenados.lastIndex)
                }
            }
            var showExpandedInput by remember { mutableStateOf(false) }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            if (!showExpandedInput) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showExpandedInput = true }
                        .background(
                            color = Color(0xFF2C3E50),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Escribe un mensaje...",
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = Color.Gray
                    )
                }
            } else {
                Column {
                    Text("Responder", style = MaterialTheme.typography.titleMedium)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        listOf("Operator", "Owner").forEach { tipo ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                RadioButton(
                                    selected = selectedRemitente == tipo,
                                    onClick = {
                                        selectedRemitente = tipo
                                        onEvent(MensajeEvent.TipoRemitenteChange(tipo))
                                    }
                                )
                                Text(tipo)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = uiState.remitente ?: "",
                        onValueChange = { onEvent(MensajeEvent.RemitenteChange(it)) },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = uiState.contenido ?: "",
                        onValueChange = { onEvent(MensajeEvent.ContenidoChange(it)) },
                        label = { Text("Mensaje") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showExpandedInput = false }) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                onEvent(MensajeEvent.Save)
                                showExpandedInput = false
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Filled.Send,
                                contentDescription = "EnviarMensaje",
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MensajeRow(mensaje: MensajeEntity) {
    val isOperator = mensaje.tipoRemitente == "Operator"
    val bubbleColor = if (isOperator) Color(0xFF1E1E2E) else Color(0xFF2C3E50)
    val textColor = Color.White
    val dateColor = Color.Gray
    val alignment = if (isOperator) Alignment.Start else Alignment.End
    val arrangement = if (isOperator) Arrangement.Start else Arrangement.End

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = arrangement
    ) {

        val inicial = mensaje.remitente.first().uppercaseChar().toString()

        if (isOperator) {
            Avatar(nombre = inicial)
            Spacer(modifier = Modifier.width(8.dp))
        }


        Column(horizontalAlignment = alignment) {
            Text(
                text = mensaje.remitente,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )


            Box(
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 280.dp)
            ) {
                Text(text = mensaje.contenido, color = textColor)
            }
            Text(
                text =
                    SimpleDateFormat(
                        "MMM dd, yyyy - HH:mm",
                        Locale.getDefault()
                    ).format(mensaje.fecha),
                style = MaterialTheme.typography.labelSmall,
                color = dateColor,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
            )
        }

        if (!isOperator) {
            Spacer(modifier = Modifier.width(8.dp))
            Avatar(nombre = inicial)
        }
    }
}

@Composable
fun Avatar(nombre: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Gray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}