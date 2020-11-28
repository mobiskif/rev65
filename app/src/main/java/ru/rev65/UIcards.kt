package ru.rev65

import android.util.Log
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

private fun trimNull(s: String?): String {
    var result = "$s"
    if (result == "null") result = ""
    return result
}

@Composable
fun usrItemsEdit(model: MainViewModel) {
    var fF = remember { mutableStateOf(TextFieldValue()) }
    var iI = remember { mutableStateOf(TextFieldValue()) }
    var oO = remember { mutableStateOf(TextFieldValue()) }
    var dD = remember { mutableStateOf(TextFieldValue()) }
    var rR = remember { mutableStateOf(TextFieldValue()) }
    var irR = remember { mutableStateOf(TextFieldValue()) }
    if (model.getState() == "Изменить пациента") {
        fF = remember { mutableStateOf(TextFieldValue("${model.user["F"]}")) }
        iI = remember { mutableStateOf(TextFieldValue("${model.user["I"]}")) }
        oO = remember { mutableStateOf(TextFieldValue("${model.user["O"]}")) }
        dD = remember { mutableStateOf(TextFieldValue("${model.user["D"]}")) }
        rR = remember { mutableStateOf(TextFieldValue("${model.user["R"]}")) }
        irR = remember { mutableStateOf(TextFieldValue("${model.user["iR"]}")) }
    }
    ScrollableColumn {

        Row {
            Column {
                OutlinedTextField(
                    value = fF.value,
                    onValueChange = { fF.value = it },
                    label = { Text("Фамилия") }//, modifier = Modifier.padding(0.dp, 8.dp)
                )
                OutlinedTextField(
                    value = iI.value,
                    onValueChange = { iI.value = it },
                    label = { Text("Имя") }//, modifier = Modifier.padding(0.dp, 8.dp)
                )
                OutlinedTextField(
                    value = oO.value,
                    onValueChange = { oO.value = it },
                    label = { Text("Отчество") }//, modifier = Modifier.padding(0.dp, 8.dp)
                )
                OutlinedTextField(
                    value = dD.value,
                    onValueChange = { dD.value = it },
                    label = { Text("Дата рождения") }, //modifier = Modifier.padding(0.dp, 8.dp),
                    placeholder = { Text(text = "1986-04-26") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                myDistrictSpinner(model, rR, irR)

                val usr = model.user.toMutableMap()
                val fieldstouser: () -> Unit = {
                    usr["F"] = fF.value.text
                    usr["I"] = iI.value.text
                    usr["O"] = oO.value.text
                    usr["D"] = dD.value.text
                    usr["R"] = rR.value.text
                    usr["iR"] = irR.value.text
                }
                Row {
                    TextButton(onClick = {
                        fieldstouser()
                        model.deleteUser(usr)
                        model.setState("Выбрать пациента")
                    }) { Text("Удалить") }

                    Button(onClick = {
                        fieldstouser()
                        if (model.getState() == "Добавить пациента") model.createUser(usr)
                        else model.updateUser(usr)
                        model.setState("Выбрать пациента")
                    }) { Text("Сохранить") }
                }

            }
        }
    }
}

@Composable
fun patItems(model: MainViewModel) {
    val user = model.user
    val state = model.state.value
    val col = Modifier.border(3.dp, color =  MaterialTheme.colors.primary, shape = Shapes().medium)
    val onclck: (usr: Map<String, String>) -> Unit = {
        model.setState("Изменить пациента")
    }
    val currentState = model.getState()
    if (currentState != "Выбрать пациента") {
        Row(modifier = col.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(user) })) {
                Text("${user["F"]} ${user["I"]} ${user["O"]}")
                Text("${user["D"]} ")
            }
            Column {
                if (state == "Выбрать клинику") Text(trimNull(user["R"]))
                else if (user["idPatSuccess"] == "true") {
                    if (state == "Выбрать специальность") {
                        Text(trimNull(user["LPUShortName"]))
                        Text("№: " + trimNull(user["idPat"]))
                    }
                    if (state == "Отложенные талоны") {
                        Text(trimNull(user["LPUShortName"]))
                        Text("№: " + trimNull(user["idPat"]))
                    }
                    if (state == "Выбрать врача") Text(trimNull(user["NameSpesiality"]))
                    if (state == "Выбрать талон") Text(trimNull(user["DocName"]))
                    if (state == "Взять талон") Text(trimNull(user["DocName"]))
                    if (state == "Отменить талон") Text(trimNull(user["NameSpesiality"]))
                } else {
                    Text(style = error, text = trimNull(user["idPat"]))
                }
            }
            //if (model.getState() != "Выбрать клинику" && wait) CircularProgressIndicator()
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@Composable
fun usrItems(map: Map<String, String>, model: MainViewModel) {
    val onclck: (usr: Map<String, String>) -> Unit = {
        model.user = it
        model.setState("Выбрать клинику")
        model.readLpuList(it)
    }

    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(mf062.clickable(onClick = { onclck(map) })) {
            Text("${map["F"]} ${map["I"]} ${map["O"]} \n${map["D"]} ")
        }
        Column {
            Text("${map["R"]}")
            IconButton(onClick = { model.user = map; model.setState("Изменить пациента") }) {
                Icon(Icons.Default.Edit, tint = Color.LightGray, modifier = Modifier.preferredSize(18.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun distItems(map: Map<String, String>, wait: Boolean) {
    Row(modifier = mpadd.then(mfw)) {
        Column(Modifier.fillMaxWidth(0.62f)) {
            map.forEach {
                Text("$it")
            }
        }
        Column {
            if (wait) CircularProgressIndicator()
        }
    }
}

@Composable
fun lpuItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (lpu: Map<String, String>) -> Unit = {
        usr["IdLpu"] = it["IdLPU"].toString()
        usr["LPUShortName"] = it["LPUShortName"].toString()
        model.user = usr
        model.checkPat(usr)
        //model.readHistList(usr)
        model.setState("Выбрать специальность")
    }
    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(Modifier.clickable(onClick = { onclck(map) })) {
            Text("${map["LPUFullName"]}")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun specItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        usr["IdSpesiality"] = it["IdSpesiality"].toString()
        usr["NameSpesiality"] = it["NameSpesiality"].toString()
        model.updateUser(usr)
        model.setState("Выбрать врача")
        model.readDocList(usr)
    }
    if ("${map["Success"]}" == "true") {
        //
    } else {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) })) {
                Text("${map["NameSpesiality"]}")
            }
            Column {
                Text("Талонов: ${map["CountFreeParticipantIE"]}")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun docItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        usr["IdDoc"] = it["IdDoc"].toString()
        usr["DocName"] = it["Name"].toString()
        model.updateUser(usr)
        model.setState("Выбрать талон")
        model.readTalonList(usr)
    }

    if ("${map["Success"]}" == "true") {
        //
    } else {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) })) {
                //doc.forEach() { Text("${it.key} ${it.value}") }
                Text(trimNull(map["Name"]))
                Text(trimNull(map["AriaNumber"]))
            }
            Column {
                Text("Талонов: " + trimNull(map["CountFreeParticipantIE"]))
                //Text("Резерв: " + trimNull(map["CountFreeTicket"]))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun talonItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        usr["IdAppointment"] = it["IdAppointment"].toString()
        usr["VisitStart"] = map["VisitStart"].toString()
        model.updateUser(usr)
        model.setState("Взять талон")
    }

    if (map["IdAppointment"] != "null") {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) })) {
                Text(trimNull("Талон №: " + map["IdAppointment"]))
            }
            Column {
                val dat = map["VisitStart"]?.split("T")?.get(0)
                val tim = map["VisitStart"]?.split("T")?.get(1)?.substring(0, 5)
                Text(trimNull(dat))
                Text(trimNull(tim))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun talonItemsEdit(model: MainViewModel) {
    val usr = model.user as MutableMap
    val clkgettalon: (spec: Map<String, String>) -> Unit = {
        Log.d("jop", "get $it")
        model.getTalon(usr)
        model.setState("Выбрать специальность")
    }
    val clkdeltalon: (spec: Map<String, String>) -> Unit = {
        model.deleteTalon(usr)
        model.setState("Выбрать специальность")
    }

    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(mf062) {
            Text(trimNull("Талон №: " + usr["IdAppointment"]))
            Spacer(modifier = Modifier.height(8.dp))
        }
        Column {
            val dat = usr["VisitStart"]?.split("T")?.get(0)
            val tim = usr["VisitStart"]?.split("T")?.get(1)?.substring(0, 5)
            Text(trimNull(dat))
            Text(trimNull(tim))
        }
    }
    Row(modifier = mpadd.then(mfw)) {
        if (model.getState() == "Взять талон") {
            Button(onClick = { clkgettalon(usr) }) { Text("Взять талон?") }
        } else if (model.getState() == "Отменить талон") {
            Button(onClick = { clkdeltalon(usr) }) { Text("Отменить талон?") }
        }
        TextButton(onClick = { model.setState("Выбрать специальность") }) { Text("Нет") }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun histItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        //model.deleteTalon(it)
        usr["IdAppointment"] = it["IdAppointment"].toString()
        model.setState("Отменить талон")
    }

    if (!map["IdAppointment"].isNullOrEmpty()) {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) })) {
                Text(trimNull("Талон №: " + map["IdAppointment"]))
                Text(trimNull(map["Name"]))
                Text(trimNull(map["NameSpesiality"]))
            }
            Column {
                val dat = map["VisitStart"]?.split("T")?.get(0)
                val tim = map["VisitStart"]?.split("T")?.get(1)?.substring(0, 5)
                Text(trimNull(dat))
                Text(trimNull(tim))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
