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

val mbord = Modifier.border(1.dp, Color.LightGray, Shapes().medium)
val mpadd = Modifier.padding(8.dp)
val mWhite = Modifier.background(Color.White, shape = Shapes().medium)
val mfw = Modifier.fillMaxWidth()
val mf062 = Modifier.fillMaxWidth(0.62f)


@Composable
fun myMenu(model: MainViewModel) {
    val expanded = remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded.value = true },
        content = { Icon(Icons.Default.MoreVert, "Menu") }
    )
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            onClick = { model.setState("Выбрать пациента"); expanded.value = false },
            content = { Text("Выбрать пациента") }
        )
        DropdownMenuItem(
            onClick = { model.setState("Мои карточки"); expanded.value = false  },
            content = { Text("Мои карточки") }
        )
        DropdownMenuItem(
            onClick = { model.setState("Отложенные талоны"); expanded.value = false  },
            content = { Text("Отложенные талоны") }
        )
        DropdownMenuItem(
            onClick = { model.setState("Информация"); expanded.value = false  },
            content = { Text("Информация") }
        )
        //Divider()
    }
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
        actions = { myMenu(model) }
    )
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
