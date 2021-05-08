package ru.rev69

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

fun getCPUInfo(model: MainViewModel) {
    val procCpuInfo = "/proc/cpuinfo"
    var temp: String?
    val readBlockSize = 8192
    try {
        val fileReader = FileReader(procCpuInfo)
        val bufferedReader = BufferedReader(fileReader, readBlockSize)
        while (bufferedReader.readLine().also { temp = it } != null) {
            Log.i("jop", temp!!)
            if (temp!!.contains("BogoMIPS", true)) {
                Log.i("jop", temp!!.split(":")[1])
                model.mf=temp!!.split(":")[1].toFloat()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


class MainActivity : AppCompatActivity() {
    private val model: MainViewModel by viewModels()

    private fun checkPermission(callbackId: Int, vararg permissionsId: String) {
        var permissions = true
        for (p in permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED
        }
        if (!permissions) ActivityCompat.requestPermissions(this, permissionsId, callbackId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        File(filesDir, "usrlist.csv").createNewFile()
        model.usrfile = File(filesDir, "usrlist.csv")

        model.wait.observe(this, {
            //Log.d("jop","$it ${model.flopsList.value}")
            if (!it) setContent { UI(model) }
        })
        model.state.observe(this, {
            setContent { UI(model) }
            if (model.getState() == "Выбрать врача" && model.user["idPatSuccess"] == "false") Toast.makeText(this, "Запись невозможна: карточки пациента нет в регистратуре!", Toast.LENGTH_LONG).show()
        })
        model.usrList.observe(this, { setContent { UI(model) } })
        model.idPat.observe(this, {
            val usr = model.user as MutableMap
            usr["idPat"] = it["IdPat"].toString()
            usr["idPatSuccess"] = it["Success"].toString()
            model.updateUserInList(usr)
            model.readHistList(usr)
            model.readSpecList(usr)
        })
        model.idTalon.observe(this, {
            if (it["Success"] == "true") {
                if (it["Delete"] == "false") Toast.makeText(this, "Талон отложен!", Toast.LENGTH_LONG).show()
                else Toast.makeText(this, "Талон отменен!", Toast.LENGTH_LONG).show()
                model.readHistList(model.user)
                model.setState("Отложенные талоны")
            } else Toast.makeText(this, "Действие не выполнено: отклонено регистратурой!", Toast.LENGTH_LONG).show()
        })
        //getCPUInfo(model)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener
            { datePicker: DatePicker, day: Int, month: Int, year: Int ->
                /*
                setContent {
                    Column {
                        Text("$day, $month, $year")
                    }
                }
                */
                model.isAdmin=true
            }, year, month, day
        )
        model.dialog = datePickerDialog

        fun dosom(){
            val EVENT_PROJECTION: Array<String> = arrayOf(
                CalendarContract.Calendars._ID,                     // 0
                CalendarContract.Calendars.ACCOUNT_NAME,            // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
                CalendarContract.Calendars.OWNER_ACCOUNT            // 3
            )
            val uri: Uri = CalendarContract.Calendars.CONTENT_URI
            val cur: Cursor = contentResolver.query(uri, EVENT_PROJECTION, null, null, null)!!
            while (cur.moveToNext()) {
                // Get the field values
                val calID: Long = cur.getLong(0)
                val displayName: String = cur.getString(1)
                val accountName: String = cur.getString(2)
                val ownerName: String = cur.getString(3)
                // Do something with the values...
                Log.d("jop", "$calID $accountName")
            }

            val calID: Long = 5
            val startMillis: Long = Calendar.getInstance().run {
                set(2021, 4, 4, 17, 30)
                timeInMillis
            }
            val endMillis: Long = Calendar.getInstance().run {
                set(2021, 4, 4, 18, 45)
                timeInMillis
            }

            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, "Title Jopa")
                put(CalendarContract.Events.DESCRIPTION, "Descrioption Jopa")
                put(CalendarContract.Events.CALENDAR_ID, calID)
                put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Moscow")
            }
            val uri2: Uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!
            val eventID: Long = uri2.lastPathSegment!!.toLong()
            Log.d("jop", uri2.encodedPath + " " + eventID)

        }

        //checkPermission(42, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
        //dosom()
    }

    override fun onResume() {
        super.onResume()
        model.readUsrList()
    }

    override fun onPause() {
        super.onPause()
        model.writeUsrList()
    }

    override fun onBackPressed() {
        var state = model.getState()
        when (state) {
            "Выбрать пациента" -> state = if (model.isAdmin) "Search top 10" else "Выбрать пациента"
            "Выбрать клинику" -> state = "Выбрать пациента"
            "Изменить пациента" -> state = "Выбрать пациента"
            "Добавить пациента" -> state = "Выбрать пациента"
            "Информация" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Мои карточки" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Отложенные талоны" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Взять талон" -> state = "Выбрать специальность"
            "Отменить талон" -> state = "Выбрать специальность"
        }
        model.setState(state)
    }


}

@Composable
fun UI(model: MainViewModel) {
    val usrs = if (!model.usrList.value.isNullOrEmpty()) model.usrList.value as List else listOf()
    val lpus = if (!model.lpuList.value.isNullOrEmpty()) model.lpuList.value as List else listOf()
    val pats = if (!model.patList.value.isNullOrEmpty()) model.patList.value as List else listOf()
    val specs = if (!model.specList.value.isNullOrEmpty()) model.specList.value as List else listOf()
    val docs = if (!model.docList.value.isNullOrEmpty()) model.docList.value as List else listOf()
    val talons = if (!model.talonList.value.isNullOrEmpty()) model.talonList.value as List else listOf()
    val hist = if (!model.histList.value.isNullOrEmpty()) model.histList.value as List else listOf()
    val flops = if (!model.flopsList.value.isNullOrEmpty()) model.flopsList.value as List else listOf()
    val wait = model.wait.value == true

    myTheme {
        Scaffold(floatingActionButton = { myFab(model) }, topBar = { myBar(model) }) {
            Column(modifier = mpadd) {
                patItems(model)
                if (wait) {
                    if (model.getState()!="Информация") LinearProgressIndicator(mfw);
                    Spacer(modifier = Modifier.height(8.dp))
                }
                when (model.getState()) {
                    "Изменить пациента" -> usrItemsEdit(model)
                    "Добавить пациента" -> usrItemsEdit(model)
                    "Выбрать пациента" -> LazyColumn {items(usrs.size) { usrItems(usrs[it], model) }}
                    "Выбрать клинику" -> LazyColumn {items(lpus.size) { lpuItems(lpus[it], model) }}
                    "Мои карточки" -> LazyColumn {items(pats.size) { cardItems(pats[it], model) }}
                    "Выбрать специальность" -> {
                        LazyRow {items(hist.size) { histItems(hist[it], model) }}
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn {items(specs.size) { specItems(specs[it], model) }}
                    }
                    "Выбрать врача" -> LazyColumn {items(docs.size) { docItems(docs[it], model) }}
                    "Выбрать талон" -> LazyColumn {items(talons.size) { talonItems(talons[it], model) }}
                    "Отложенные талоны" -> LazyColumn {items(hist.size) { histItems(hist[it], model) }}
                    "Взять талон" -> talonItemsEdit(model)
                    "Отменить талон" -> talonItemsEdit(model)
                    "Информацияz" -> {
                        Text("${Build.MANUFACTURER} ${Build.MODEL}")
                        val s = String.format("%.2f", model.mf)
                        Button(onClick = {
                            model.runf()
                        }) { Text(s) }
                        model.mf = 0f
                        LazyColumn {items(flops.size) { flopsItems(flops[it], model) }}
                    }
                }
            }
        }
    }
}
