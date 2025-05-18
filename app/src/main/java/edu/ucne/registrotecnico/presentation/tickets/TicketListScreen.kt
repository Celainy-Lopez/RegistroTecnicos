package edu.ucne.registrotecnico.presentation.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    onEdit: (Int?) -> Unit,
    onDelete: (TicketEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEdit(0) }) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(ticketList) { ticket ->
                    TicketRow(ticket, { onEdit(ticket.ticketId) },
                        { onDelete(ticket) })
                }
            }
        }
    }
}

@Composable
private fun TicketRow(
    ticket: TicketEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
            Text(text = "Ticket Id: " + ticket.ticketId.toString())
            Text(text = "Fecha: ${formatDate(ticket.fecha)}")
            Text(text = "Prioridad: " +   ticket.tecnicoId)
            Text(text = "Cliente: " +  ticket.cliente)
            Text(text = "Asunto: " +  ticket.asunto)
            Text(text = "Descripción : " + ticket.descripcion)
            Text(text = "Tecnico : " + ticket.tecnicoId.toString())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
            HorizontalDivider()
        }
    }
}

fun formatDate(date: java.util.Date): String {
    val formatter = java.text.SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(date)
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TicketListPreview() {
    val tickets = listOf(
        TicketEntity(
            ticketId = 1,
            fecha = java.util.Date(),
            prioridadId = 1,
            cliente = "Juan Pérez",
            asunto = "Problema con red",
            tecnicoId = 2
        ),
        TicketEntity(
            ticketId = 2,
            fecha = java.util.Date(),
            prioridadId = 2,
            cliente = "Ana Gómez",
            asunto = "Error en sistema",
            tecnicoId = 1
        )
    )

    RegistroTecnicoTheme {
        TicketListScreen(
            ticketList = tickets,
            onEdit = {},
            onDelete = {}
        )
    }
}
