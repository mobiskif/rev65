package ru.rev65

import android.os.Bundle
import android.util.Log
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
        model.usrList.observe(this, { model.writeUsrList() })
        model.idPat.observe(this, {
            Log.d("jop", "=== idPat $it ${model.user}")
            val usr = model.user as MutableMap
            usr["idPat"] = it["IdPat"].toString()
            model.updateUser(usr)
            model.readSpecList(usr)
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
            //"Выбрать пациента" -> state = if (model.isAdmin) "Search top 10" else "Выбрать пациента"
            "Выбрать клинику" -> state = "Выбрать пациента"
            "Изменить пациента" -> state = "Выбрать пациента"
            "Добавить пациента" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Отложенные талоны" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Взять талон" -> state = "Выбрать талон"
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
    val dist = if (!model.distrList.value.isNullOrEmpty()) model.distrList.value as List else listOf()
    val talons = if (!model.talonList.value.isNullOrEmpty()) model.talonList.value as List else listOf()
    val wait = model.wait.value == true

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
            }
        }
    }
}
