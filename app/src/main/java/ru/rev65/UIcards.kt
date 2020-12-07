package ru.rev65

import android.util.Log
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    onValueChange = { fF.value = it },
                    label = { Text("Фамилия") }//, modifier = Modifier.padding(0.dp, 8.dp)
                )
                OutlinedTextField(
                    value = iI.value,
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    onValueChange = { iI.value = it },
                    label = { Text("Имя") }//, modifier = Modifier.padding(0.dp, 8.dp)
                )
                OutlinedTextField(
                    value = oO.value,
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    onValueChange = { oO.value = it },
                    label = { Text("Отчество") }//, modifier = Modifier.padding(0.dp, 8.dp)
                )
                OutlinedTextField(
                    value = dD.value,
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
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
                        if (model.getState() == "Добавить пациента") {
                            usr["id"] = "${Math.random()}"
                            model.createUser(usr)
                        } else model.updateUserInList(usr)
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
    if (user["idPatSuccess"] == "true") {
        when (model.getState()) {
            "Изменить пациента" -> {}
            "Добавить пациента" -> {}
            "Выбрать пациента" -> {}
            "Выбрать клинику" -> {}
            "Мои карточки" -> {}
            "Выбрать специальность" -> {
                    Text(trimNull(user["LPUShortName"]))
                    Text("Карточка: " + trimNull(user["idPat"]))
            }
            "Выбрать врача" -> {
                    Text(trimNull(user["LPUShortName"]))
                    Text("Карточка: " + trimNull(user["idPat"]))
                    Text(trimNull(user["NameSpesiality"]))
            }
            "Выбрать талон" -> {
                    Text(trimNull(user["LPUShortName"]))
                    Text("Карточка: " + trimNull(user["idPat"]))
            }
            "Отложенные талоны" -> {}
            "Взять талон" -> {
                    Text(trimNull(user["LPUShortName"]))
                    Text("Карточка: " + trimNull(user["idPat"]))
            }
            "Отменить талон" -> {
                    Text(trimNull(user["LPUShortName"]))
                    Text("Карточка: " + trimNull(user["idPat"]))
            }
            "Информация" -> {}
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
    else {
        if ( model.getState() != "Выбрать пациента" && model.getState() != "Выбрать клинику" ) {
            Text(trimNull(user["idPat"]), color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}


@Composable
fun usrItems(map: Map<String, String>, model: MainViewModel) {
    val onclck: (usr: Map<String, String>) -> Unit = {
        model.user = it
        model.setState("Выбрать клинику")
        model.readLpuList(it)
    }

    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(mf062.clickable(onClick = { onclck(map) }).then(mpadd)) {
            Text("${map["F"]} ${map["I"]} ${map["O"]} \n${map["D"]} ", style = typography.body1)
        }
        Column(mpadd) {
            Text("${map["R"]}", style = typography.body2)
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

        /********** мега-важно *******/
        usr["IdPat"] = model.checkPat(usr).toString()
        /*****************************/

        model.user = usr
        model.setState("Выбрать специальность")
    }
    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(Modifier.clickable(onClick = { onclck(map) }).then(mf062).then(mpadd)) {
            Text(text = "${map["LPUFullName"]}", style = typography.body1)
        }
        Column(Modifier.clickable(onClick = { onclck(map) }).then(mpadd)) {
            Text(text = "${map["LPUShortName"]}", style = typography.body2)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun cardItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (lpu: Map<String, String>) -> Unit = {
        usr["IdLpu"] = it["IdLPU"].toString()
        usr["LPUShortName"] = it["LPUShortName"].toString()
        usr["IdPat"] = it["IdPat"].toString()
        usr["IdPat"] = model.checkPat(usr).toString()
        model.user = usr
        //model.readHistList(usr)
        model.setState("Выбрать специальность")
    }
    Row(modifier = mbord.then(mpadd).then(mfw)) {
        Column(Modifier.clickable(onClick = { onclck(map) }).then(mf062).then(mpadd)) {
            //Text(text="${map["LPUFullName"]}", style = typography.body1)
            if (map["Success"] == "true") Text(text = "Карточка №:", style = typography.body1)
            Text(text = trimNull(map["IdPat"]), style = typography.body1)
        }
        Column(Modifier.clickable(onClick = { onclck(map) }).then(mpadd)) {
            Text(text = "${map["LPUShortName"]}", style = typography.body2)
            Spacer(modifier = Modifier.height(16.dp))
            //Text(text="${map["IdLPU"]}", style = typography.overline)
            Text(text = "${map["Description"]}", style = typography.overline)
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
        model.updateUserInList(usr)
        model.setState("Выбрать врача")
        model.readDocList(usr)
    }
    if ("${map["Success"]}" == "true") {
        //
    } else {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) }).then(mpadd)) {
                Text("${map["NameSpesiality"]}", style = typography.body1)
            }
            Column(mpadd) {
                Text("Талонов: ${map["CountFreeParticipantIE"]}", style = typography.body2)
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
        model.updateUserInList(usr)
        model.setState("Выбрать талон")
        model.readTalonList(usr)
    }

    if ("${map["Success"]}" == "true") {
        //
    } else {
        Row(modifier = mbord.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) }).then(mpadd)) {
                //doc.forEach() { Text("${it.key} ${it.value}") }
                Text(trimNull(map["Name"]), style = typography.body1)
                Text(trimNull(map["AriaNumber"]), style = typography.body2)
            }
            Column(mpadd) {
                Text("Талонов: " + trimNull(map["CountFreeParticipantIE"]), style = typography.body2)
                //Text("Резерв: " + trimNull(map["CountFreeTicket"]))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun histItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        usr["IdAppointment"] = it["IdAppointment"].toString()
        usr["NameSpesiality"] = it["NameSpesiality"].toString()
        usr["DocName"] = it["Name"].toString()
        usr["VisitStart"] = it["VisitStart"].toString()
        model.user = usr
        model.setState("Отменить талон")
    }

    val bcolr = Modifier.border(3.dp, MaterialTheme.colors.secondary, shapes.medium)
    if (!map["IdAppointment"].isNullOrEmpty()) {
        Row(modifier = bcolr.then(mpadd).then(mfw)) {
            Column(mf062.clickable(onClick = { onclck(map) }).then(mpadd)) {
                Text(trimNull(map["NameSpesiality"]))
                //Text(trimNull(map["Name"]))
            }
            Column(mpadd) {
                val dat = map["VisitStart"]?.split("T")?.get(0)
                val tim = map["VisitStart"]?.split("T")?.get(1)?.substring(0, 5)
                Text(trimNull(dat))
                Text(trimNull(tim))
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun talonItems(map: Map<String, String>, model: MainViewModel) {
    val usr = model.user as MutableMap
    val onclck: (spec: Map<String, String>) -> Unit = {
        usr["IdAppointment"] = it["IdAppointment"].toString()
        //usr["NameSpesiality"] = it["NameSpesiality"].toString()
        //usr["DocName"] = it["Name"].toString()
        usr["VisitStart"] = it["VisitStart"].toString()
        model.user = usr
        model.setState("Взять талон")
    }
    val bcolr = Modifier.border(3.dp, MaterialTheme.colors.secondary, shapes.medium)
    if (!map["IdAppointment"].isNullOrEmpty()) {
        Row(modifier = bcolr.then(mpadd)) {
            Column(mf062.clickable(onClick = { onclck(map) }).then(mpadd)) {
                Text(trimNull(usr["NameSpesiality"]))
                Spacer(modifier = Modifier.height(8.dp))
                Text(trimNull(usr["DocName"]), style = typography.body2)
            }
            Column(mpadd) {
                val dat = map["VisitStart"]?.split("T")?.get(0)
                val tim = map["VisitStart"]?.split("T")?.get(1)?.substring(0, 5)
                Text(trimNull(dat))
                Spacer(modifier = Modifier.height(8.dp))
                Text(trimNull(tim))
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
    Spacer(modifier = Modifier.height(8.dp))
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
    val bcolr = Modifier.border(3.dp, MaterialTheme.colors.secondary, shapes.medium)

    Row(modifier = bcolr.then(mpadd).then(mfw)) {
        Column(mf062.then(mpadd)) {
            //Text(trimNull("№ " + usr["IdAppointment"]))
            Text(trimNull(usr["NameSpesiality"]))
            Spacer(modifier = Modifier.height(8.dp))
            Text(trimNull(usr["DocName"]), style = typography.body2)
        }
        Column(mpadd) {
            val dat = usr["VisitStart"]?.split("T")?.get(0)
            Text(trimNull(dat))
            Spacer(modifier = Modifier.height(8.dp))
            if (usr["VisitStart"]!="null") {
                val tim = usr["VisitStart"]?.split("T")?.get(1)?.substring(0, 5)
                Text(trimNull(tim))
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = mpadd.then(mfw)) {
        if (model.getState() == "Взять талон") {
            Button(onClick = { clkgettalon(usr) }) { Text("Взять талон") }
        } else if (model.getState() == "Отменить талон") {
            Button(onClick = { clkdeltalon(usr) }) { Text("Отменить талон") }
        }
        TextButton(onClick = { model.setState("Выбрать специальность") }) { Text("Нет") }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
