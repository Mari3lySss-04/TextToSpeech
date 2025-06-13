

package com.example.texttospeech

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.texttospeech.ui.theme.TextToSpeechTheme
import com.example.texttospeech.ui.theme.greenColor
import java.util.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextToSpeechTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                modifier = Modifier.height(48.dp),
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color(0xFF4CAF50)
                                ),
                                title = {
                                    Text(
                                        text = "",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) { paddingValues ->
                        TextToSpeechApp(modifier = Modifier.padding(paddingValues))
                    }
                }
            }
        }
    }
}

@Composable

fun TextToSpeechApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var texto by remember { mutableStateOf("") }
    var selectedIdioma by remember { mutableStateOf("") }
    var selectedVoz by remember { mutableStateOf("") }
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        val textToSpeech = TextToSpeech(context) {}
        tts.value = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    tts.value?.voices?.forEach {
        println("VOZ DISPONIBLE: ${it.name}")
    }


    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //aca se muestra un título
            Text(
                text = "App - Tronco Megáfono",
                color = greenColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .border(2.dp, Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.imagen1),
                contentDescription = null,
                modifier = Modifier.height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))



            //aca se ingresa el número telefónico
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                placeholder = { Text("Ingresa el texto a decir") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color(0xFF4CAF50),
                    unfocusedLabelColor = Color(0xFF4CAF50),
                    focusedTextColor = Color.Black,
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth().background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Columna para idioma
                Column(modifier = Modifier.weight(0.9f)) {
                    Text(
                        text = "Selecciona un idioma:",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(5.dp))
                            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    DropdownMenuList(
                        items = listOf("Español", "Inglés"),
                        selectedItem = selectedIdioma
                    ) {
                        selectedIdioma = it
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Columna para voz
                Column(modifier = Modifier.weight(0.9f)) {
                    Text(
                        text = "Selecciona una voz:",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(5.dp))
                            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp, )
                    )
                    DropdownMenuList(
                        items = listOf("ES", "EN"),
                        selectedItem = selectedVoz
                    ) {
                        selectedVoz = it
                    }
                }
            }


            Button(
                onClick = {
                    if (texto.isBlank()) {
                        Toast.makeText(context, "No has ingresado texto", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (selectedIdioma.isBlank() || selectedVoz.isBlank()) {
                        Toast.makeText(context, "Selecciona idioma y voz", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val locale = when {
                        selectedVoz == "ES" -> Locale("es", "ES")
                        selectedVoz == "EN" -> Locale("en", "US")
                        else -> Locale.getDefault()
                    }
                    tts.value?.language = locale



                    val voiceName = when {
                        selectedIdioma == "Español" && selectedVoz == "ES" -> "es-es-x-ana"
                        selectedIdioma == "Español" && selectedVoz == "EN" -> "en-us-x-iol"
                        selectedIdioma == "Inglés" && selectedVoz == "ES" -> "es-es-x-ana"
                        selectedIdioma == "Inglés" && selectedVoz == "EN" -> "en-us-x-iol"
                        else -> ""
                    }

                    val voice = tts.value?.voices?.find { it.name.contains(voiceName) }
                    if (voice != null) {
                        tts.value?.voice = voice
                    }

                    tts.value?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
                },
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Habla", color = Color.White)
            }
        }
    }
}

@Composable
fun DropdownMenuList(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
            Text(text = if (selectedItem.isEmpty()) "Seleccionar" else selectedItem, color = Color.White)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { label ->
                DropdownMenuItem(text = { Text(label) }, onClick = {
                    onItemSelected(label)
                    expanded = false
                })
            }
        }
    }
}
