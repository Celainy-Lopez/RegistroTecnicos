package edu.ucne.registrotecnico.presentation.tickets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    ticketId: Int? = null,
    viewModel: TicketsViewModel,
    navController: NavController,
    function: () -> Boolean,
) {
    var fecha by remember { mutableStateOf(Date()) }
    var prioridadId: Int by remember { mutableIntStateOf(0) }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnicoId: Int by remember { mutableIntStateOf(0) }
    var errorMessage: String? by remember { mutableStateOf(null) }
    var existe by remember { mutableStateOf<TicketEntity?>(null) }
    var expandidoPrioridad by remember { mutableStateOf(false) }
    var expandidoTecnico by remember { mutableStateOf(false) }

    val prioridades by viewModel.getPrioridades.collectAsState()
    val tecnicos by viewModel.getTecnicos.collectAsState()

    LaunchedEffect(ticketId) {
        if (ticketId != null && ticketId > 0) {
            val ticket = viewModel.findTicket(ticketId)
            ticket?.let {
                existe = it
                fecha = it.fecha
                prioridadId = it.prioridadId
                cliente = it.cliente
                asunto = it.asunto
                descripcion = it.descripcion
                tecnicoId = it.tecnicoId
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navController != null) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    TopAppBar(
                        title = {
                            Text(
                                "Registro Tickets",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        })

                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(
                        value = ticketId.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    DialogoFecha(
                        fechaActual = fecha,
                        onFechaSeleccionada = { nuevaFecha -> fecha = nuevaFecha }
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandidoPrioridad,
                        onExpandedChange = { expandidoPrioridad = !expandidoPrioridad }
                    ) {
                        OutlinedTextField(
                            value = prioridades.find { it.prioridadId == prioridadId }?.descripcion
                                ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Seleccionar prioridad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoPrioridad) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandidoPrioridad,
                            onDismissRequest = { expandidoPrioridad = false }
                        ) {
                            prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(prioridad.descripcion) },
                                    onClick = {
                                        prioridadId = prioridad.prioridadId!!
                                        expandidoPrioridad = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = cliente,
                        onValueChange = { cliente = it },
                        label = { Text("Cliente") },
                        placeholder = { Text("Ej: Juan Pérez") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        placeholder = { Text("Ej: Juan Pérez") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        placeholder = { Text("Ej: Usuario desabilitado") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandidoTecnico,
                        onExpandedChange = { expandidoTecnico = !expandidoTecnico }
                    ) {
                        OutlinedTextField(
                            value = tecnicos.find { it.tecnicoId == tecnicoId }?.nombres ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Seleccionar técnico") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTecnico) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandidoTecnico,
                            onDismissRequest = { expandidoTecnico = false }
                        ) {
                            tecnicos.forEach { tecnico ->
                                DropdownMenuItem(
                                    text = { Text(tecnico.nombres) },
                                    onClick = {
                                        tecnicoId = tecnico.tecnicoId!!
                                        expandidoTecnico = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(2.dp))
                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                fecha = Date()
                                prioridadId = 0
                                cliente = ""
                                asunto = ""
                                descripcion = ""
                                tecnicoId = 0
                                errorMessage = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button"
                            )
                            Text(text = "Nuevo")
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                if (fecha.toString().isBlank()) {
                                    errorMessage = "fecha vacia."
                                    return@OutlinedButton
                                }
                                if (prioridadId <= 0) {
                                    errorMessage = "Seleccione una prioridad."
                                    return@OutlinedButton
                                }
                                if (cliente.isBlank()) {
                                    errorMessage = "ingrese un cliente."
                                    return@OutlinedButton
                                }
                                if (asunto.isBlank()) {
                                    errorMessage = "ingrese un asunto."
                                    return@OutlinedButton
                                }

                                if (descripcion.isBlank()) {
                                    errorMessage = "ingrese un asunto."
                                    return@OutlinedButton
                                }

                                if (tecnicoId <= 0) {
                                    errorMessage = "Seleccione un tecnico."
                                    return@OutlinedButton
                                }

                                viewModel.saveTicket(
                                    TicketEntity(
                                        ticketId = existe?.ticketId,
                                        fecha = fecha,
                                        prioridadId = prioridadId,
                                        cliente = cliente,
                                        asunto = asunto,
                                        descripcion = descripcion,
                                        tecnicoId = tecnicoId
                                    )
                                )
                            }
                        )

                        {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoFecha(
    fechaActual: Date,
    onFechaSeleccionada: (Date) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    OutlinedTextField(
        value = formato.format(fechaActual),
        onValueChange = {},
        label = { Text("Fecha") },
        trailingIcon = {
            IconButton(onClick = { openDialog.value = true }) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Seleccionar fecha"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        readOnly = true,
    )

    if (openDialog.value) {
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onFechaSeleccionada(Date(millis))
                        }
                        openDialog.value = false
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
