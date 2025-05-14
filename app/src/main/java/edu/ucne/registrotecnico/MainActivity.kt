package edu.ucne.registrotecnico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()
        setContent {
            RegistroTecnicoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ){
                        TecnicoScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun TecnicoScreen(
    ){
        var tecnicoId by remember { mutableStateOf(0) }
        var nombres by remember { mutableStateOf("") }
        var sueldo by remember { mutableDoubleStateOf(0.0) }
        var errorMessage:String? by remember { mutableStateOf(null) }
        var existe by remember { mutableStateOf<TecnicoEntity?>(null) }
        val scope = rememberCoroutineScope()


        Scaffold { innerPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ){
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ){
                        Text(
                            text = "Registro técnico",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        OutlinedTextField(
                            value = tecnicoId.toString(),
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
                            placeholder = {Text("Ej:25000.00")},
                            value = if (sueldo == 0.0)  "" else decimalFormat.format(sueldo),
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
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            OutlinedButton(
                                onClick = {
                                    tecnicoId = 0
                                    nombres = ""
                                    sueldo = 0.0
                                    errorMessage = null
                                    existe = null
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Add,
                                    contentDescription = "new button")
                                Text(text = "Nuevo")
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedButton(
                                onClick = {
                                    if(nombres.isBlank()) {
                                        errorMessage = "Nombre vacio."
                                        return@OutlinedButton
                                    }

                                    if(sueldo <= 0.0) {
                                        errorMessage = "El sueldo debe ser mayor que cero."
                                        return@OutlinedButton
                                    }

                                    scope.launch {
                                        saveTecnico(
                                            TecnicoEntity(
                                                tecnicoId = existe?.tecnicoId,
                                                nombres = nombres,
                                                sueldo = sueldo
                                            )
                                        )
                                        tecnicoId = 0
                                        nombres = ""
                                        sueldo = 0.0
                                        errorMessage = null
                                        existe = null
                                    }
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

                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                val tecnicoList by tecnicoDb.TecnicoDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                TecnicoListScreen(
                    tecnicoList = tecnicoList,
                    onEdit = { tecnico ->
                        tecnicoId = tecnico.tecnicoId ?: 0
                        nombres = tecnico.nombres
                        sueldo = tecnico.sueldo
                        existe = tecnico
                    },
                    onDelete = { tecnico ->
                        scope.launch {
                            tecnicoDb.TecnicoDao().delete(tecnico)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun TecnicoListScreen(
        tecnicoList: List<TecnicoEntity>,
        onEdit: (TecnicoEntity) -> Unit,
        onDelete: (TecnicoEntity) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Lista de técnicos")
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico, onEdit, onDelete)
                }
            }
        }
    }


    @Composable

    private fun TecnicoRow(
        tecnico: TecnicoEntity,
        onEdit: (TecnicoEntity) -> Unit,
        onDelete: (TecnicoEntity) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {

            val decimalFormat = DecimalFormat("#,##0.00")
            Text(modifier = Modifier.weight(1f), text = tecnico.tecnicoId.toString())
            Text(modifier = Modifier.weight(2f), text = tecnico.nombres)
            Text(modifier = Modifier.weight(2f),  text = decimalFormat.format(tecnico.sueldo) )

            IconButton(onClick = { onEdit(tecnico) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = { onDelete(tecnico) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
        HorizontalDivider()
    }

    private suspend fun saveTecnico(tecnico: TecnicoEntity){
        tecnicoDb.TecnicoDao().save(tecnico)
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        RegistroTecnicoTheme {
            val scope = rememberCoroutineScope()
            val tecnicoList = listOf(
                TecnicoEntity(1, "Celainy", 10000.0),
                TecnicoEntity(2, "Juan", 15000.60),
            )
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { tecnico ->
                    tecnico.tecnicoId ?: 0
                    tecnico.nombres
                    tecnico.sueldo
                    tecnico
                },
                onDelete = { tecnico ->
                    scope.launch {
                        tecnicoDb.TecnicoDao().delete(tecnico)
                    }
                }
            )
        }
    }
}