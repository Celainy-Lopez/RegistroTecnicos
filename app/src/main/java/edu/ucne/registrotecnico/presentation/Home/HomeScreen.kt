package edu.ucne.registrotecnico.presentation

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnico.presentation.sistemas.SistemasViewModel
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnico.presentation.tickets.TicketsViewModel
import edu.ucne.registrotecnico.presentation.usuarios.UsuariosViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    tecnicosViewModel: TecnicosViewModel = hiltViewModel(),
    prioridadesViewModel: PrioridadesViewModel = hiltViewModel(),
    ticketsViewModel: TicketsViewModel = hiltViewModel(),
    sistemasViewModel: SistemasViewModel = hiltViewModel(),
    usuariosViewModel: UsuariosViewModel = hiltViewModel()
) {
    val tecnicoUiState by tecnicosViewModel.uiState.collectAsState()
    val prioridadUiState by prioridadesViewModel.uiState.collectAsState()
    val ticketUiState by ticketsViewModel.uiState.collectAsState()
    val sistemaUiState by sistemasViewModel.uiState.collectAsState()
    val usuariosUiState by usuariosViewModel.uiState.collectAsState()

    val tecnicoCount = tecnicoUiState.tecnicos.size
    val prioridadCount = prioridadUiState.prioridades.size
    val ticketCount = ticketUiState.tickets.size
    val sistemaCount = sistemaUiState.sistemas.size
    val usuarioCount = usuariosUiState.usuarios.size

    val items = listOf(
        Triple("Técnicos", tecnicoCount, Icons.Default.People),
        Triple("Prioridades", prioridadCount, Icons.Default.LowPriority),
        Triple("Tickets", ticketCount, Icons.Default.List),
        Triple("Sistemas", sistemaCount, Icons.Default.Api),
        Triple("Usuarios", usuarioCount, Icons.Default.Person)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        TopAppBar(
            title = {
                Text(
                    "Bienvenidos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { (label, count, icon) ->
                AnimatedDashboardCard(
                    label = "$label Registrados",
                    count = count,
                    icon = icon
                )
            }
        }
    }
}


@Composable
fun AnimatedDashboardCard(
    label: String,
    count: Int,
    icon: ImageVector
) {
    val transition = rememberInfiniteTransition(label = "CardAnim")
    val scale by transition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScaleAnimation"
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        elevation = cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$count", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
