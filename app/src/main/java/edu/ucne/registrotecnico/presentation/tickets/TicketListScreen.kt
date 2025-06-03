package edu.ucne.registrotecnico.presentation.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TicketListScreen(
    viewModel: TicketsViewModel = hiltViewModel(),
    goToTicket: (Int) -> Unit,
    createTicket: () -> Unit,
    deleteTicket : ((TicketEntity) -> Unit) ? = null,
    goToMensaje: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketListBodyScreen(
        uiState = uiState,
        goToTicket = goToTicket,
        createTicket = createTicket,
        deleteTicket = { ticket ->
            viewModel.onEvent(TicketEvent.TicketChange(ticket.ticketId ?: 0))
            viewModel.onEvent(TicketEvent.Delete)
        },
        goToMensaje = goToMensaje
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListBodyScreen(
    uiState: TicketUiState,
    goToTicket: (Int) -> Unit,
    createTicket : () -> Unit,
    deleteTicket: (TicketEntity) -> Unit,
    goToMensaje: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createTicket) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { InnerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(InnerPadding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.tickets) {ticket ->
                    TicketRow(
                        it = ticket,
                        goToTicket = { goToTicket(ticket.ticketId ?: 0) },
                        deleteTicket = deleteTicket,
                        goToMensaje = goToMensaje,
                        ticket = ticket
                    )
                }
            }
        }
    }
}


@Composable
private fun TicketRow(
    it: TicketEntity,
    goToTicket: () -> Unit,
    deleteTicket: (TicketEntity) -> Unit,
    goToMensaje: (Int) -> Unit,
    ticket: TicketEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ticket #: " + it.ticketId.toString())

                Spacer(modifier = Modifier.weight(1f))

                Text(text = "Fecha: ${formatDate(it.fecha)}")
            }
            Text(text = "Cliente: " + it.cliente, fontWeight = FontWeight.ExtraBold)
            Text(text = "Asunto: " + it.asunto)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton( onClick = { goToMensaje (ticket.ticketId ?:0)}) {
                    Icon(imageVector = Icons.Default.QuestionAnswer, contentDescription = "Eliminar")
                }
                IconButton(onClick = goToTicket ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { deleteTicket(it)}) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
            HorizontalDivider()
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(date)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TicketListPreview() {
    val tickets = listOf(
        TicketEntity(
            ticketId = 1,
            fecha = Date(),
            prioridadId = 1,
            cliente = "Juan Pérez",
            asunto = "Problema con red",
            tecnicoId = 2
        ),
        TicketEntity(
            ticketId = 2,
            fecha = Date(),
            prioridadId = 2,
            cliente = "Ana Gómez",
            asunto = "Error en sistema",
            tecnicoId = 1
        )
    )

    RegistroTecnicoTheme {
        TicketListScreen(
            goToTicket = {},
            createTicket = {},
            deleteTicket = {},
            goToMensaje = {}
        )
    }
}
