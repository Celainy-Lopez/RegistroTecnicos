package edu.ucne.registrotecnico.presentation.prioridades

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListScreen(
    viewModel: PrioridadesViewModel = hiltViewModel(),
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadListBodyScreen(
        uiState,
        goToPrioridad,
        createPrioridad,
        deletePrioridad
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListBodyScreen(
    uiState: PrioridadUiState,
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de prioridades") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createPrioridad) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(1f), text = "ID")
                Text(modifier = Modifier.weight(1f), text = "Descripción")
                Text(modifier = Modifier.weight(1f), text = "Acciones")
            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.prioridades) {
                    PrioridadRow(
                        it,
                        goToPrioridad,
                        createPrioridad,
                        deletePrioridad
                    )
                }
            }
        }
    }
}

@Composable
private fun PrioridadRow(
    it: PrioridadEntity,
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable{
                    goToPrioridad(it.prioridadId?: 0)
                }
        ) {
            Text(modifier = Modifier.weight(1f), text = it.prioridadId.toString())
            Text(modifier = Modifier.weight(1f), text = it.descripcion)

            Row(modifier = Modifier.weight(1f)) {
                IconButton(onClick = createPrioridad) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = deletePrioridad) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
        HorizontalDivider()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val prioridades = listOf(
        PrioridadEntity(
            prioridadId = 1,
            descripcion = "Alta",
        ),
        PrioridadEntity(
            prioridadId = 2,
            descripcion = "Alta",
        )
    )
    RegistroTecnicoTheme {
        PrioridadListScreen(
            goToPrioridad = {},
            createPrioridad = {},
            deletePrioridad = {}

        )
    }
}