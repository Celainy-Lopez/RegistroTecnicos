package edu.ucne.registrotecnico.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadesViewModel


@Composable
fun PrioridadesNavHost(
    navHostController: NavHostController,
    prioridadList: List<PrioridadEntity>,
    viewModel: PrioridadesViewModel,
    navController: NavController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.PrioridadList
    ) {
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                prioridadList = prioridadList,
                onEdit = { prioridadId ->
                    navHostController.navigate(Screen.Prioridad(prioridadId))
                },
                onDelete = { prioridad ->
                    viewModel.deletePrioridad(prioridad)
                }
            )
        }

        composable<Screen.Prioridad>{ backStack ->
            val prioridadId  = backStack.toRoute<Screen.Prioridad>().prioridadId
            PrioridadScreen(
                prioridadId,
                viewModel,
                navController
            )
        }
    }
}