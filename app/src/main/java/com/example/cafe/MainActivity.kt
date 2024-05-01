package com.example.cafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafe.ui.theme.CafeTheme

const val FOOD_PRICE = 15.00
const val BISCUIT_PRICE = 5.00
const val BOTTLED_WATER_PRICE = 2.00
const val MEAT_TURNOVER_PRICE = 5.00

data class MenuItem(val name: String, val price: Double)

val menuItems = listOf(
    MenuItem("Food", FOOD_PRICE),
    MenuItem("Biscuit", BISCUIT_PRICE),
    MenuItem("Bottled Water", BOTTLED_WATER_PRICE),
    MenuItem("Meat Turnover", MEAT_TURNOVER_PRICE)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CafeTheme { // Consider renaming this theme to CafeTheme
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    var selectedItem by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var cartList by remember { mutableStateOf(mutableListOf<String>()) }
    var totalCost by remember { mutableDoubleStateOf(0.0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Cafeteria Bill Calculator",
           // style = MaterialTheme.typography.h4,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(menuItems) { item ->
                Surface(modifier = Modifier.padding(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = item.name)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "$${item.price}")
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            Text(text = "Select Item:")
            Spacer(modifier = Modifier.weight(1f))
            menuItems.forEach { item ->
                Row( // Each menu item in a Row
                    modifier = Modifier
                        .fillMaxWidth() // Stretch Row to fill available width
                        .padding(0.dp)  // Remove potential padding
                ) {
                    RadioButton(
                        selected = selectedItem == (item.name ?: false),
                        onClick = { selectedItem = item.name },
                        modifier = Modifier.weight(0.1f) // Smaller weight for radio button
                    )
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f) // Remaining space for text
                    )
                }
            }
        }

        TextField(
            value = quantity.toString(),
            onValueChange = { quantity = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Quantity") },
        )
        Button(onClick = {
            if (selectedItem.isNotEmpty() && quantity > 0) {
                val price = when (selectedItem) {
                    "Food" -> FOOD_PRICE
                    "Biscuit" -> BISCUIT_PRICE
                    "Bottled Water" -> BOTTLED_WATER_PRICE
                    "Meat Turnover" -> MEAT_TURNOVER_PRICE
                    else -> 0.0
                }
                val cartItemText = "$selectedItem x $quantity - $${price * quantity}"
                cartList.add(cartItemText)
                totalCost += price * quantity
                selectedItem = ""
                quantity = 0
            }
        }) {
            Text("Add to Cart")
        }

        Column(modifier = Modifier.weight(2f)) {
            Text(text = "Cart List:")
            cartList.forEach { item ->
                Text(text = item)
            }
            Text(
                text = "Total: $$totalCost",
                //style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    CafeTheme { // Consider renaming this theme to CafeTheme
        MyApp()
    }
}