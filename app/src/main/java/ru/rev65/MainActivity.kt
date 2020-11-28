package ru.rev65

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import java.io.File

class MainActivity : AppCompatActivity() {
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        File(filesDir, "usrlist.csv").createNewFile()
        model.usrfile = File(filesDir, "usrlist.csv")

        model.wait.observe(this, { setContent { UI(model) } })
        model.state.observe(this, { setContent { UI(model) } })
        model.usrList.observe(this, { setContent { UI(model)} })
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
            }
            else Toast.makeText(this, "Действие не выполнено, отказ!", Toast.LENGTH_LONG).show()
        })
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
            "Выбрать специальность" -> state = "Выбрать клинику"
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
    val specs = if (!model.specList.value.isNullOrEmpty()) model.specList.value as List else listOf()
    val docs = if (!model.docList.value.isNullOrEmpty()) model.docList.value as List else listOf()
    val talons = if (!model.talonList.value.isNullOrEmpty()) model.talonList.value as List else listOf()
    val hist = if (!model.histList.value.isNullOrEmpty()) model.histList.value as List else listOf()
    val wait = model.wait.value == true

    myTheme {
        Scaffold(floatingActionButton = { myFab(model) }) {
            Column(modifier = mpadd) {
                if (wait) LinearProgressIndicator(mfw)
                Text(model.getState())
                Spacer(modifier = Modifier.height(8.dp))
                when (model.getState()) {
                    "Изменить пациента" -> {
                        usrItemsEdit(model)
                    }
                    "Добавить пациента" -> {
                        usrItemsEdit(model)
                    }
                    "Выбрать пациента" -> {
                        LazyColumnFor(usrs) { usrItems(it, model) }
                    }
                    "Выбрать клинику" -> {
                        patItems(model)
                        LazyColumnFor(lpus) { lpuItems(it, model) }
                    }
                    "Выбрать специальность" -> {
                        patItems(model)
                        LazyColumnFor(specs) { specItems(it, model) }
                    }
                    "Выбрать врача" -> {
                        patItems(model)
                        LazyColumnFor(docs) { docItems(it, model) }
                    }
                    "Выбрать талон" -> {
                        patItems(model)
                        LazyColumnFor(talons) { talonItems(it, model) }
                    }
                    "Отложенные талоны" -> {
                        patItems(model)
                        LazyColumnFor(hist) { histItems(it, model) }
                    }
                    "Взять талон" -> {
                        patItems(model)
                        talonItemsEdit(model)
                    }
                    "Отменить талон" -> {
                        patItems(model)
                        talonItemsEdit(model)
                    }
                }
            }
        }
    }
}
