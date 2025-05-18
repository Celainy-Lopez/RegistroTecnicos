package edu.ucne.registrotecnico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import edu.ucne.registrotecnico.data.repository.TicketsRepository
import edu.ucne.registrotecnico.presentation.navigation.HomeNavHost
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnico.presentation.tickets.TicketsViewModel
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    private lateinit var tecnicosRepository: TecnicosRepository
    private lateinit var tecnicosViewModel: TecnicosViewModel
    private lateinit var prioridadesRepository: PrioridadesRepository
    private lateinit var prioridadesViewModel: PrioridadesViewModel
    private lateinit var ticketsRepository: TicketsRepository
    private lateinit var ticketsViewModel: TicketsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        tecnicosRepository = TecnicosRepository(tecnicoDb.TecnicoDao())
        tecnicosViewModel = TecnicosViewModel(tecnicosRepository)

        prioridadesRepository = PrioridadesRepository(tecnicoDb.PrioridadDao())
        prioridadesViewModel = PrioridadesViewModel(prioridadesRepository)

        ticketsRepository = TicketsRepository(tecnicoDb.TicketDao())
        ticketsViewModel = TicketsViewModel(
            ticketsRepository = ticketsRepository,
            tecnicosRepository = tecnicosRepository,
            prioridadesRepository = prioridadesRepository
        )

        setContent {
            RegistroTecnicoTheme {
                val lifecycleOwner = LocalLifecycleOwner.current

                val tecnicoList by tecnicoDb.TecnicoDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )

                val ticketList by tecnicoDb.TicketDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )

                val prioridadList by tecnicoDb.PrioridadDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )

                val navController = rememberNavController()

                HomeNavHost(
                    navController = navController,
                    tecnicoList = tecnicoList,
                    ticketList = ticketList,
                    prioridadList = prioridadList,
                    tecnicosViewModel = tecnicosViewModel,
                    ticketsViewModel = ticketsViewModel,
                    prioridadesViewModel = prioridadesViewModel
                )
            }
        }
    }
}
