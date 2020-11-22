package ru.rev65

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {
    val state = MutableLiveData("Выбрать пациента")
    var user: Map<String, String> = mapOf()
    lateinit var usrfile: File

    private val repository = Repository()
    val wait = repository.wait
    val idPat = repository.idPat
    val patList = repository.patList
    val lpuList = repository.lpuList
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

    fun createUser(usr: Map<String, String>) {
        viewModelScope.launch {
            repository.addUserToList(usr)
        }
    }
    fun updateUser(usr: MutableMap<String, String>) {
        viewModelScope.launch {
            repository.updateUserInList(usr)
        }
    }
    fun deleteUser(usr: MutableMap<String, String>) {
        viewModelScope.launch {
            repository.deleteUserFromList(usr)
        }
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

/*
    fun updatePats() {
        viewModelScope.launch {
            repository.updateIdPat(user)
        }
    }
*/

    private fun readDistrList() {
        viewModelScope.launch {
            repository.readDistrList()
        }
    }

    fun readLpuList(map: MutableMap<String, String>) {
        viewModelScope.launch {
            repository.readLpuList(map)
        }
    }

    fun checkPat(map: MutableMap<String, String>) {
        viewModelScope.launch {
            repository.checkPat(map)
        }
    }

    fun readSpecList(map: Map<String, String>) {
        viewModelScope.launch {
            repository.readSpecList(map)
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

    init {
        readDistrList()
    }

}
