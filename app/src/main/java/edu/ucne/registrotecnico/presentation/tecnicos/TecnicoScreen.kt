package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoScreen(
    tecnicoId: Int? = null,
    viewModel: TecnicosViewModel,
    navController: NavController,
    function: () -> Boolean,
) {
    var nombres by remember { mutableStateOf("") }
    var sueldo by remember { mutableDoubleStateOf(0.0) }
    var errorMessage: String? by remember { mutableStateOf(null) }
    var existe by remember { mutableStateOf<TecnicoEntity?>(null) }

    LaunchedEffect(tecnicoId) {
        if (tecnicoId != null && tecnicoId > 0) {
            val tecnico = viewModel.findTecnico(tecnicoId)
            tecnico?.let {
                existe = it
                nombres = it.nombres
                sueldo = it.sueldo
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
                                "Registro Técnicos",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(
                        value = tecnicoId.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    OutlinedTextField(
                        value = nombres,
                        onValueChange = { nombres = it },
                        label = { Text("Nombres") },
                        placeholder = { Text("Ej: Juan Pérez") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )


                    val decimalFormat = DecimalFormat("#.##")

                    OutlinedTextField(
                        label = { Text("Sueldo") },
                        placeholder = { Text("Ej:25000.00") },
                        value = if (sueldo == 0.0) "" else decimalFormat.format(sueldo),
                        onValueChange = {
                            val parsed = it.toDoubleOrNull()
                            if (parsed != null) {
                                sueldo = parsed
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

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
                                nombres = ""
                                sueldo = 0.0
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
                                if (nombres.isBlank()) {
                                    errorMessage = "Nombre vacio."
                                    return@OutlinedButton
                                }

                                if (sueldo <= 0.0) {
                                    errorMessage = "El sueldo debe ser mayor que cero."
                                    return@OutlinedButton
                                }

                                viewModel.saveTecnico(
                                    TecnicoEntity(
                                        tecnicoId = existe?.tecnicoId,
                                        nombres = nombres,
                                        sueldo = sueldo

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

