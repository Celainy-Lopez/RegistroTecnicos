package edu.ucne.registrotecnico.presentation.sistemas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.remote.dto.SistemaDto
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import java.text.DecimalFormat

@Composable
fun SistemaListScreen(
    viewModel: SistemasViewModel = hiltViewModel(),
    createSistema: () -> Unit,
    goToSistema: (Int) -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SistemaListBodyScreen(
        uiState = uiState,
        goToSistema = { id -> goToSistema(id) },
        createSistema = createSistema,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SistemaListBodyScreen(
    uiState: SistemaUiState,
    goToSistema: (Int) -> Unit,
    createSistema: () -> Unit,
    onEvent: (SistemaEvent) -> Unit,
    goBack: () -> Unit
) {
    val refreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { onEvent(SistemaEvent.GetSistemas) }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "API Sistemas",
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.surface)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector =  Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createSistema) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.sistemas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay usuarios registrados",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }  else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(uiState.sistemas) { sistema ->
                            SistemaRow (
                                it = sistema,
                                goToSistema = { goToSistema(sistema.sistemaId ?: 0) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                if (!uiState.errorMessage.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun SistemaRow(
    it: SistemaDto,
    goToSistema: () -> Unit
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
            val decimalFormat = DecimalFormat("#,##0.00")

            Row {
                Text(text = "Sistema #: ", fontWeight = FontWeight.ExtraBold)
                Text(text = it.sistemaId.toString())
            }

            Spacer(modifier = Modifier.weight(2f))

            Row {
                Text(text = "Nombre ", fontWeight = FontWeight.ExtraBold)
                Text( it.nombre)
            }

            Spacer(modifier = Modifier.weight(2f))

            Row {
                Text(text = "Descripción: ", fontWeight = FontWeight.ExtraBold)
                Text( it.descripcion)
            }

            Spacer(modifier = Modifier.weight(2f))

            Row {
                Text(text = "Costo: ", fontWeight = FontWeight.ExtraBold)
                Text(decimalFormat.format(it.costo))
            }
        }
    }
    HorizontalDivider()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val sistemas = listOf(
        SistemaDto(
            sistemaId = 1,
            nombre = "Cuentas por Cobrar",
            descripcion = "Cobrar cuentas por cobrar",
            costo = 10000.0
        ),
        SistemaDto(
            sistemaId = 2,
            nombre = "Cuentas por Cobrar",
            descripcion = "Cobrar cuentas por cobrar",
            costo = 10000.0
        )
    )
    RegistroTecnicoTheme {
        SistemaListScreen(
            goToSistema = {},
            createSistema = {},
            goBack = {}
        )
    }
}