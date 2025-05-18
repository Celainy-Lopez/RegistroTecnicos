package edu.ucne.registrotecnico.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketListScreen

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Tickets : BottomNavItem("tickets", "Tickets", Icons.Default.List)
    object Tecnicos : BottomNavItem("tecnicos", "Técnicos", Icons.Default.People)
    object Prioridades : BottomNavItem("prioridades", "Prioridades", Icons.Default.LowPriority)
}

@Composable
fun HomeScreen(
    tickets: List<TicketEntity>,
    tecnicos: List<TecnicoEntity>,
    prioridades: List<PrioridadEntity>,
    onEditTecnico: (Int?) -> Unit,
    onDeleteTecnico: (TecnicoEntity) -> Unit,
    onEditPrioridad: (Int?) -> Unit,
    onDeletePrioridad: (PrioridadEntity) -> Unit,
    onEditTicket: (Int?) -> Unit,
    onDeleteTicket: (TicketEntity) -> Unit,
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Tickets,
        BottomNavItem.Tecnicos,
        BottomNavItem.Prioridades,
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                DashboardContent(
                    tickets = tickets,
                    tecnicos = tecnicos,
                    prioridades = prioridades,
                )
            }

            composable(BottomNavItem.Tickets.route) {
                TicketListScreen(
                    ticketList = tickets,
                    onEdit = onEditTicket,
                    onDelete = onDeleteTicket
                )
            }

            composable(BottomNavItem.Tecnicos.route) {
                TecnicoListScreen(
                    tecnicoList = tecnicos,
                    onEdit = onEditTecnico,
                    onDelete = onDeleteTecnico
                )
            }

            composable(BottomNavItem.Prioridades.route) {
                PrioridadListScreen(
                    prioridadList = prioridades,
                    onEdit = onEditPrioridad,
                    onDelete = onDeletePrioridad
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    tickets: List<TicketEntity>,
    tecnicos: List<TecnicoEntity>,
    prioridades: List<PrioridadEntity>
) {
    val items = listOf(
        Triple("Técnicos", tecnicos.size, Icons.Default.People),
        Triple("Prioridades", prioridades.size, Icons.Default.LowPriority),
        Triple("Tickets", tickets.size, Icons.Default.List)
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