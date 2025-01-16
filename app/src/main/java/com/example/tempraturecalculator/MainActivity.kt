package com.example.tempraturecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tempraturecalculator.ui.theme.TempratureCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TempratureCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StatefulTempratureInput()
                    ConverterApp()
                    TwoWayConverter()
                }
            }
        }
    }
}

// Stateful State
@Composable
fun StatefulTempratureInput(
    modifier : Modifier = Modifier
){
    var input by remember{ mutableStateOf("") }
    var output by remember{ mutableStateOf("") }
    Column(modifier.padding(16.dp)){
        Text(
            text = stringResource(R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {newInput ->
                input = newInput
                output = CelciusToFahrenheit(newInput)
            },
        )
        Text(stringResource(R.string.temperature_fahrenheit,output))
    }
}

private fun CelciusToFahrenheit(celcius:String) : String{
    return celcius.toDoubleOrNull()?.let {
        (it * 9 / 5) + 32
    }.toString()
}

private fun FahrenheitToCelcius(fahrenheit : String) : String{
    return fahrenheit.toDoubleOrNull()?.let {
        (it - 32) * 5 / 9
    }.toString()
}


// Stateless State Child
@Composable
fun StatelessTempratureInput(
    input : String,
    output : String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.stateless_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange,
        )
        Text(stringResource(R.string.temperature_fahrenheit,output))
    }
}

// Parent dari Stateless
@Composable
fun ConverterApp(
    modifier: Modifier = Modifier
){
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    Column(
        modifier
    ) {
        StatelessTempratureInput(
            input = input,
            output = output,
            onValueChange = {newInput->
                input = newInput
                output = CelciusToFahrenheit(newInput)
            }
        )
    }
}


enum class Scale(val scaleName : String){
    CELCIUS("Celcius"),
    FAHRENHEIT("Fahrenheit")
}

@Composable
fun GeneralTemperatureInput(
    input: String,
    scale: Scale,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier
    ) {
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_temperature,scale.scaleName)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange
        )
    }
}

@Composable
fun TwoWayConverter(
    modifier: Modifier = Modifier
){
    var celcius by remember { mutableStateOf("") }
    var fahrenheit by remember { mutableStateOf("") }
    Column (
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.two_way_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        GeneralTemperatureInput(
            scale = Scale.CELCIUS,
            input = celcius,
            onValueChange = { newInput->
                celcius = newInput
                fahrenheit = CelciusToFahrenheit(newInput)

            }
        )
        GeneralTemperatureInput(
            scale = Scale.FAHRENHEIT,
            input = fahrenheit,
            onValueChange = { newInput->
                fahrenheit = newInput
                celcius = FahrenheitToCelcius(newInput)
            }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun GreetingPreview() {
    TempratureCalculatorTheme {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            StatefulTempratureInput()
            ConverterApp()
            TwoWayConverter()
        }

    }
}