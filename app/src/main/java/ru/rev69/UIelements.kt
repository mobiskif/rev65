package ru.rev69

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

val mbord = Modifier.border(0.dp, Color.LightGray, Shapes().medium)
val mpadd = Modifier.padding(8.dp)
val mWhite = Modifier.background(Color.White, shape = Shapes().medium)
val mfw = Modifier.fillMaxWidth()
val mf062 = Modifier.fillMaxWidth(0.62f)

@Composable
fun myTopBar(model: MainViewModel) {
    val mod = Modifier
        .background(MaterialTheme.colors.primary)
        .padding(16.dp)
        .fillMaxWidth()
    val stl = MaterialTheme.typography.h6
    Text(modifier = mod, text = model.getState(), style = stl, color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primary))
}

@Composable
fun myBar(model: MainViewModel) {
    TopAppBar(
        title = { Text(model.getState(), maxLines = 1) },

        navigationIcon = {
            IconButton(onClick = { model.setState("Выбрать пациента") }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
/*
        actions = {
            IconButton(onClick = { model.setState("Информация")  }) {
                Icon(Icons.Filled.Warning,"")
            }
        },
*/
        actions = {
            val expanded = remember { mutableStateOf(false) }
            IconButton(onClick = { expanded.value = true }) {
                Icon(Icons.Default.MoreVert, "Menu")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    onClick = { model.setState("Выбрать пациента") },
                    content = { Text("SearchTop10") }
                )
                DropdownMenuItem(
                    onClick = { model.isAdmin = true },
                    content = { Text("Домой") }
                )
                //Divider()
            }
        }
    )
}

@Composable
fun myColumn(
    col: Modifier = mWhite.then(Modifier.clickable(onClick = { })),
    children: @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))
    Column(
        modifier = (col
            .then(mbord)
            .then(mpadd)).then(Modifier.fillMaxWidth())
    ) {
        children()
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun myFab(model: MainViewModel) {
    if (model.getState() == "Выбрать пациента") {
        FloatingActionButton(onClick = { model.setState("Добавить пациента") }) {
            Icon(Icons.Filled.Add, "Add")
        }
    }
}

@Composable
fun myTopDropDownMenu(model: MainViewModel) {
    val expanded = remember { mutableStateOf(false) }
    Row {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(onClick = {
                model.isAdmin = true
            }) {
                Text("SearchTop10")
            }
            //Divider()
        }
    }
}

@Composable
fun myDistrictSpinner(model: MainViewModel, rR: MutableState<TextFieldValue>, irR: MutableState<TextFieldValue>) {
    val dlist = model.distrList.value
    val expanded = remember { mutableStateOf(false) }
    val iconButton =
        Row(mpadd) {
            Text(
                "Район: " + rR.value.text,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            IconButton(onClick = { expanded.value = true }) {
                Icon(Icons.Default.ArrowDropDown, "District")
            }
        }

    Row {
        iconButton
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
            //toggle = iconButton
        ) {
            Column {
                dlist?.forEach {
                    DropdownMenuItem(
                        enabled = true,
                        onClick = { expanded.value = false; },
                        content = {
                            Box(
                                modifier = Modifier.clickable(onClick = {
                                    expanded.value = false
                                    rR.value = TextFieldValue(it["DistrictName"].toString())
                                    irR.value = TextFieldValue(it["IdDistrict"].toString())
                                })
                            ) {
                                Text(it["DistrictName"].toString())
                            }
                        }
                    )
                }
            }
        }
    }
}
