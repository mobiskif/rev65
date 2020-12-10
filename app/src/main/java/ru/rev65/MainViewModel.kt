package ru.rev65

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel : ViewModel() {
    val state = MutableLiveData("Выбрать пациента")
    var user: Map<String, String> = mapOf()
    lateinit var usrfile: File

    private val repository = Repository()
    val isAdmin = false
    val wait = repository.wait
    val idPat = repository.idPat
    val idTalon = repository.idTalon
    val histList = repository.histList
    val lpuList = repository.lpuList
    val patList = repository.patList
    val specList = repository.specList
    val docList = repository.docList
    val talonList = repository.talonList
    var usrList = repository.usrList
    var distrList = repository.distrList

    fun setState(state: String) {
        this.state.postValue(state)
    }
    fun getState(): String {
        return "${state.value}"
    }

    fun readUsrList() {
        viewModelScope.launch {
            repository.readUsrList(usrfile)
        }
    }
    fun writeUsrList() {
        viewModelScope.launch {
            repository.writeUsrList(usrfile)
        }
    }

    fun createUser(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.addUserToList(usr)
        }
    }
    fun updateUserInList(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.updateUserInList(usr)
        }
    }
    fun deleteUser(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.deleteUserFromList(usr)
        }
    }

    private fun readDistrList() {
        viewModelScope.launch {
            repository.readDistrList()
        }
    }

    fun readLpuList(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.readLpuList(usr)
        }
    }

    fun updatePatList(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.updatePatList(usr)
        }
    }

    fun checkPat(usr: Map<String, String>): Map<String,String> {
        var res = mapOf<String,String>()
        viewModelScope.launch {
            res = repository.checkPat(usr)
            repository._idPat.postValue(res)
        }
        return res
    }

    fun readSpecList(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.readSpecList(usr)
        }
    }

    fun readDocList(map: Map<String, String>) {
        viewModelScope.launch {
            repository.readDocList(map)
        }
    }

    fun readTalonList(map: Map<String, String>) {
        viewModelScope.launch {
            repository.readTalonList(map)
        }
    }

    fun readHistList(map: Map<String, String>) {
        viewModelScope.launch {
            repository.readHistList(map)
        }
    }

    fun getTalon(map: Map<String, String>) {
        viewModelScope.launch {
            repository.getTalon(map)
        }
    }
    fun deleteTalon(map: Map<String, String>) {
        viewModelScope.launch {
            repository.deleteTalon(map)
        }
    }

    fun runf() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var value = 0f
                val isVisible = true
                while (isVisible) {
                    var X = 0.0f
                    val t1 = System.currentTimeMillis()
                    var i: Long = 0
                    while (i <= 100e6f) {
                        X = i * 1.0f
                        i++
                    }
                    val t2 = System.currentTimeMillis()
                    val time = (t2 - t1) / 1000f
                    value = 1e-6.toFloat() * X / time
                    //setText(s)
                    try {
                        Thread.sleep(3000)
                        val s = String.format("%.0f", value) + "MF  " + String.format("%.2f", time) + " сек " + Thread.currentThread().id
                        Log.d("jop", "$s")
                    } catch (e: InterruptedException) {
                    }
                }
                value = 0f
            }
        }
    }


    init {
        readDistrList()
    }

}
