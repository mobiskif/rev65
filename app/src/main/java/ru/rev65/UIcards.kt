package ru.rev65

import android.util.Log
import androidx.compose.foundation.ScrollableColumn
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
import androidx.compose.ui.unit.sp

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
                Row {
                    TextButton(onClick = {
                        usr["F"] = fF.value.text
                        usr["I"] = iI.value.text
                        usr["O"] = oO.value.text
                        usr["D"] = dD.value.text
                        usr["R"] = rR.value.text
                        usr["iR"] = irR.value.text
                        model.deleteUser(usr)
                        model.setState("Выбрать пациента")
                    }) { Text("Удалить") }

                    Button(onClick = {
                        usr["F"] = fF.value.text
                        usr["I"] = iI.value.text
                        usr["O"] = oO.value.text
                        usr["D"] = dD.value.text
                        usr["R"] = rR.value.text
                        usr["iR"] = irR.value.text
                        if (model.getState() == "Добавить пациента") model.createUser(usr) else model.updateUser(usr)
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
    val wait = model.wait.value == true
    val onclck: (usr: Map<String, String>) -> Unit = {
        model.setState("Изменить пациента")
    }
    val currentState = model.getState()
    if (currentState != "Выбрать пациента") {
        Row(modifier = mGray.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = {onclck(user)})) {
                Text("${user["F"]} ${user["I"]} ${user["O"]}")
                Text("${user["D"]} ")
                //Text(trimNull(user["R"]))
            }
            Column {
                if (state=="Выбрать клинику") Text(trimNull(user["R"]))
                if (state=="Выбрать специальность") Text(trimNull(user["LPUShortName"]))
                if (state=="Выбрать специальность") Text(trimNull(user["idPat"]))
                if (state=="Выбрать врача") Text(trimNull(user["NameSpesiality"]))
                if (state=="Выбрать талон") Text(trimNull(user["DocName"]))
                if (state=="Взять талон") Text(trimNull(user["DocName"]))
                if (model.getState() != "Выбрать клинику" && wait) CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun usrItems(map: Map<String, String>, model: MainViewModel) {
    val onclck: (usr: Map<String, String>) -> Unit = {
        model.user = it
        model.setState("Выбрать клинику")
        model.readLpuList(it as MutableMap<String, String>)
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
        model.readHistList(usr)
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
    }
    else {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) })) {
                Text("${map["NameSpesiality"]}")
            }
            Column {
                Text("Талонов: ${map["CountFreeParticipantIE"]} \nРезерв: ${map["CountFreeTicket"]}")
                //Text("${spec["NearestDate"]}\n${spec["LastDate"]}")
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

    if ("${map["Success"]}"=="true") {
        //
    }
    else {
    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(mf062.clickable(onClick = { onclck(map) })) {
            //doc.forEach() { Text("${it.key} ${it.value}") }
            Text(trimNull(map["Name"]))
            Text(trimNull(map["AriaNumber"]))
        }
        Column {
            Text("Талонов: " + trimNull(map["CountFreeParticipantIE"]))
            Text("Резерв: " + trimNull(map["CountFreeTicket"]))
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

    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(mf062.clickable(onClick = { onclck(map) })) {
            Text(trimNull("Талон №: "+map["IdAppointment"]))
        }
        Column {
            val dat = map["VisitStart"]?.split("T")?.get(0)
            val tim = map["VisitStart"]?.split("T")?.get(1)?.substring(0,5)
            Text(trimNull(dat))
            Text(trimNull(tim))
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun talonItemsEdit(model: MainViewModel) {
    val usr = model.user as MutableMap

    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(mf062) {
            Text(trimNull("Талон №: "+usr["IdAppointment"]))
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = mpadd.then(mfw)) {

                Button(onClick = {
                    model.getTalon(usr)
                    model.setState("Выбрать специальность")
                }) { Text("Взять?") }

                TextButton(onClick = {
                    model.setState("Выбрать врача")
                }) { Text("Нет") }
            }
        }
        Column {
            val dat = usr["VisitStart"]?.split("T")?.get(0)
            val tim = usr["VisitStart"]?.split("T")?.get(1)?.substring(0,5)
            Text(trimNull(dat))
            Text(trimNull(tim))
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun histItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        //usr["IdAppointment"] = it["IdAppointment"].toString()
        //usr["VisitStart"] = map["VisitStart"].toString()
        //model.updateUser(usr)
        model.setState("Выбрать специальность")
    }

    if(!map["IdAppointment"].isNullOrEmpty()) {
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
