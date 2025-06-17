package edu.ucne.registrotecnico.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.presentation.mensaje.MensajeScreen
import edu.ucne.registrotecnico.presentation.navigation.Screen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnico.presentation.sistemas.SistemaListScreen
import edu.ucne.registrotecnico.presentation.sistemas.SistemaScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketScreen

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object TicketsList : BottomNavItem("tickets", "Tickets", Icons.Default.List)
    object TecnicosList : BottomNavItem("tecnicos", "Técnicos", Icons.Default.People)
    object PrioridadesList : BottomNavItem("prioridades", "Prioridades", Icons.Default.LowPriority)
    object SistemasList: BottomNavItem("sistemas", "Sistemas", Icons.Default.Api)
}
@Composable
fun HomeNavHost(
    navHostController: NavHostController,
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.TicketsList,
        BottomNavItem.TecnicosList,
        BottomNavItem.PrioridadesList,
        BottomNavItem.SistemasList
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navHostController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navHostController.navigate(item.route) {
                                    popUpTo(navHostController.graph.startDestinationId) {
                                        saveState = true
                                    }
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
            navController = navHostController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = navHostController)
            }

            composable(BottomNavItem.TicketsList.route) {
                TicketListScreen(
                    goToTicket = { id ->
                        navHostController.navigate(Screen.Ticket(id ?: 0))
                    },
                    createTicket = {
                        navHostController.navigate(Screen.Ticket(0))
                    },
                    deleteTicket = {},
                    goToMensaje = { ticketId ->
                        require(ticketId != null)
                        navHostController.navigate(Screen.Mensaje(ticketId))
                    }
                )
            }

            composable<Screen.Mensaje> { backStack ->
                val ticketId = backStack.toRoute<Screen.Mensaje>().ticketId
                require(ticketId != null)
                MensajeScreen(
                    ticketId = ticketId,
                    goBack = { navHostController.popBackStack() }
                )
            }

            composable<Screen.Ticket> { backStack ->
                val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
                TicketScreen(
                    ticketId = ticketId,
                    goBack = { navHostController.popBackStack() }
                )
            }

            composable(BottomNavItem.TecnicosList.route) {
                TecnicoListScreen(
                    goToTecnico = { id ->
                        navHostController.navigate(Screen.Tecnico(id ?: 0))
                    },
                    createTecnico = {
                        navHostController.navigate(Screen.Tecnico(0))
                    },
                    deleteTecnico = {}
                )
            }

            composable<Screen.Tecnico> { backStack ->
                val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
                TecnicoScreen(
                    tecnicoId = tecnicoId,
                    goBack = { navHostController.popBackStack() }
                )
            }

            composable(BottomNavItem.PrioridadesList.route) {
                PrioridadListScreen(
                    goToPrioridad = { id ->
                        navHostController.navigate(Screen.Prioridad(id ?: 0))
                    },
                    createPrioridad = {
                        navHostController.navigate(Screen.Prioridad(0))
                    },
                    deletePrioridad = {}
                )
            }

            composable<Screen.Prioridad> { backStack ->
                val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridadId
                PrioridadScreen(
                    prioridadId = prioridadId,
                    goBack = { navHostController.popBackStack() }
                )
            }

            composable (BottomNavItem.SistemasList.route) {
                SistemaListScreen (
                    goToSistema = { id ->
                        navHostController.navigate(Screen.Sistema(id))
                    },
                    createSistema = {
                        navHostController.navigate(Screen.Sistema(null))
                    },
                    goBack = { navHostController.popBackStack() }
                )
            }

            composable <Screen.Sistema>{ backStack ->
                SistemaScreen (
                    goBack = { navHostController.popBackStack() }
                )
            }
        }
    }
}



