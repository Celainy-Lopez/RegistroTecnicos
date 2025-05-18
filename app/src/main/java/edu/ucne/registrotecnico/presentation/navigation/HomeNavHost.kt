package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnico.presentation.tickets.TicketsViewModel
import edu.ucne.registrotecnico.presentation.HomeScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    prioridadesViewModel: PrioridadesViewModel,
    tecnicosViewModel: TecnicosViewModel,
    ticketsViewModel: TicketsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            val tecnicos by tecnicosViewModel.tecnicos.collectAsState()
            val prioridades by prioridadesViewModel.prioridades.collectAsState()
            val tickets by ticketsViewModel.ticketsS.collectAsState()

            HomeScreen(
                tecnicos = tecnicos,
                prioridades = prioridades,
                tickets = tickets,
                onEditTecnico = { id -> navController.navigate(Screen.Tecnico(id ?: 0)) },
                onDeleteTecnico = { tecnico -> tecnicosViewModel.deleteTecnico(tecnico) },
                onEditPrioridad = { id -> navController.navigate(Screen.Prioridad(id ?: 0)) },
                onDeletePrioridad = { prioridad -> prioridadesViewModel.deletePrioridad(prioridad) },
                onEditTicket = { id -> navController.navigate(Screen.Ticket(id ?: 0)) },
                onDeleteTicket = { ticket -> ticketsViewModel.deleteTicket(ticket) }
            )
        }

        //pantalla lista de prioridades
        composable<Screen.PrioridadList> {
            val prioridades by prioridadesViewModel.prioridades.collectAsState()

            PrioridadListScreen(
                prioridadList = prioridades,
                onEdit = { id ->
                    navController.navigate(Screen.Prioridad(id ?: 0))
                },
                onDelete = { prioridad ->
                    prioridadesViewModel.deletePrioridad(prioridad)
                }
            )

        }

        //pantalla formulario de prioridades
        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridadId
            PrioridadScreen(
                prioridadId = prioridadId,
                viewModel = prioridadesViewModel,
                navController = navController,
                function = { navController.popBackStack() }
            )
        }

        //pantalla lista de tecnicos
        composable<Screen.TecnicoList> {
            val tecnicos by tecnicosViewModel.tecnicos.collectAsState()

            TecnicoListScreen(
                tecnicoList = tecnicos,
                onEdit = { id ->
                    navController.navigate(Screen.Tecnico(id ?: 0))
                },
                onDelete = { tecnico ->
                    tecnicosViewModel.deleteTecnico(tecnico)
                }
            )
        }

        //pantalla formulario de tecnico
        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId = tecnicoId,
                viewModel = tecnicosViewModel,
                navController = navController,
                function = { navController.popBackStack() }
            )
        }

        //pantalla lista de tickets
        composable<Screen.TicketList> {
            val tickets by ticketsViewModel.ticketsS.collectAsState()

            TicketListScreen(
                ticketList = tickets,
                onEdit = { id ->
                    navController.navigate(Screen.Ticket(id ?: 0))
                },
                onDelete = { ticket ->
                    ticketsViewModel.deleteTicket(ticket)
                }
            )
        }

        //pantalla formulario tickets
        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                viewModel = ticketsViewModel,
                navController = navController,
                function = { navController.popBackStack() }
            )
        }
    }
}
