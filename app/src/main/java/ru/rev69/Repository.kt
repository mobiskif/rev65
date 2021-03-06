package ru.rev69

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception

class Repository {

    val _wait = MutableLiveData(false)
    val wait: LiveData<Boolean> = _wait
    private val _idTalon = MutableLiveData<Map<String, String>>()
    val idTalon: LiveData<Map<String, String>> = _idTalon
    val _idPat = MutableLiveData<Map<String, String>>()
    val idPat: LiveData<Map<String, String>> = _idPat
    private var _histList = MutableLiveData<List<Map<String, String>>>()
    val histList: LiveData<List<Map<String, String>>> = _histList
    private var _talonList = MutableLiveData<List<Map<String, String>>>()
    val talonList: LiveData<List<Map<String, String>>> = _talonList
    private var _docList = MutableLiveData<List<Map<String, String>>>()
    val docList: LiveData<List<Map<String, String>>> = _docList
    private var _specList = MutableLiveData<List<Map<String, String>>>()
    val specList: LiveData<List<Map<String, String>>> = _specList

    private var _patList = MutableLiveData<List<Map<String, String>>>()
    var patList: LiveData<List<Map<String, String>>> = _patList
    private var _lpuList = MutableLiveData<List<Map<String, String>>>()
    val lpuList: LiveData<List<Map<String, String>>> = _lpuList
    private var _usrList = MutableLiveData<List<Map<String, String>>>()
    val usrList: LiveData<List<Map<String, String>>> = _usrList
    private var _distrList = MutableLiveData<List<Map<String, String>>>()
    val distrList: LiveData<List<Map<String, String>>> = _distrList
    private var _flopsList = MutableLiveData<List<Map<String, String>>>()
    val flopsList: LiveData<List<Map<String, String>>> = _flopsList

    private val pat1 = mapOf(
        "F" to "Пациент",
        "I" to "Номер",
        "O" to "Один",
        "D" to "1976-09-03",
        "R" to "Адмиралтейский",
        "iR" to "1",
        "id" to "${Math.random()}"
    )
    private val pat2 = mapOf(
        "F" to "Пациент",
        "I" to "Номер",
        "O" to "Два",
        "D" to "2014-05-29",
        "R" to "Красносельский",
        "iR" to "8",
        "id" to "${Math.random()}"
    )
    private val previewList = MutableLiveData<List<Map<String, String>>>(mutableListOf(pat1, pat2))

    fun readUsrList(usrfile: File) {
        try {
            if (usrfile.length() > 0) {
                val fis = FileInputStream(usrfile)
                val ois = ObjectInputStream(fis)
                //@Suppress("UNCHECKED_CAST")
                val usrlist = ois.readObject() //as List<Map<String, String>>
                if (usrlist is List<*>) {
                    val a: List<Map<String, String>> = usrlist.filterIsInstance<Map<String, String>>()
                    _usrList.postValue(a)
                    Log.d("jop", "$usrfile прочитано: $usrlist")
                }
                fis.close()
            } else _usrList.postValue(previewList.value)
        } catch (e: Exception) {
            _usrList.postValue(previewList.value)
        }
    }

    fun writeUsrList(usrfile: File) {
        _wait.postValue(true)
        val fos = FileOutputStream(usrfile)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(usrList.value)
        oos.close()
        fos.close()
        Log.d("jop", "$usrfile записано: ${usrList.value}")
        _wait.postValue(false)
    }


    suspend fun updatePatList(map: Map<String, String>) {
        Log.d("jop", "-- updateIdPat")
        //val lpuLf = lpuList.value!!
        var mapm = map as MutableMap
        for (lpu in lpuList.value!!) {
            withContext(Dispatchers.IO) {
                _wait.postValue(true)
                val withoutPat = if (patList.value.isNullOrEmpty()) mutableListOf() else patList.value as MutableList
                val currentPat = mutableMapOf<String, String>()
                map["IdLpu"] = lpu["IdLPU"].toString()
                val res = checkPat(map)
                //val res = map
                currentPat["IdPat"] = res["IdPat"].toString()
                currentPat["IdLPU"] = lpu["IdLPU"].toString()
                currentPat["LPUFullName"] = lpu["LPUFullName"].toString()
                currentPat["LPUShortName"] = lpu["LPUShortName"].toString()
                currentPat["Description"] = lpu["Description"].toString()
                currentPat["Success"] = res["Success"].toString()
                withoutPat.add(currentPat)
                _patList.postValue(withoutPat)
                //Log.d("jop","$res")
                _wait.postValue(false)
            }
        }
    }


    suspend fun checkPat(map: Map<String, String>): Map<String, String> {
        Log.d("jop", "-- checkPat")
        val result = mutableMapOf<String, String>()
        val f = map["F"].toString()
        val i = map["I"].toString()
        val o = map["O"].toString()
        val d = map["D"].toString()
        val l = map["IdLpu"].toString()
        val args = arrayOf(f, i, o, d, l)
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val res = Hub2().checkPat("CheckPatient", args)
            if (res.isNotEmpty()) {
                if (res[0]["Success"]=="true") {
                    result["IdPat"] = res[0]["IdPat"].toString()
                    result["Success"] = "true"
                } else {
                    result["IdPat"] = res[0]["ErrorDescription"].toString()
                    result["Success"] = "false"
                }
            } else {
                result["IdPat"] = "Учреждение не ответило"
                result["Success"] = "false"
            }
            _wait.postValue(false)
        }
        //_idPat.postValue(result)
        return result
    }

    suspend fun addUserToList(map: Map<String, String>) {
        Log.d("jop", "-- addUserToList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _usrList.postValue(usrList.value?.plus(map))
            _wait.postValue(false)
        }
    }

    suspend fun deleteUserFromList(map: Map<String, String>) {
        Log.d("jop", "-- deleteUserFromList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val lst = usrList.value?.filterNot { it == map }
            _usrList.postValue(lst)
            _wait.postValue(false)
        }
    }

    suspend fun updateUserInList(map: Map<String, String>) {
        Log.d("jop", "-- updateUserInList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            //исключаем из списка старого юсера с id текущего
            val lst = usrList.value?.filterNot { it["id"] == map["id"] }
            //вместо него добавляем нового текущего и подменяем usrList
            _usrList.postValue(lst?.plus(map))
            _wait.postValue(false)
        }
    }

    suspend fun readDistrList() {
        Log.d("jop", "-- readDistrList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            //val args =
            val res = Hub2().getDistrList("GetDistrictList")
            _distrList.postValue(res)
            _wait.postValue(false)
        }
    }

    suspend fun readLpuList(map: Map<String, String>) {
        Log.d("jop", "-- readLpuList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["iR"].toString())
            val res = Hub2().getLpuList("GetLPUList", args)
            val resf = res.filter { it.containsKey("Description") }
            _lpuList.postValue(resf)
            _wait.postValue(false)
        }
    }

    suspend fun readSpecList(map: Map<String, String>) {
        Log.d("jop", "-- readSpecList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["IdLpu"].toString())
            val res = Hub2().getSpecList("GetSpesialityList", args)
            _specList.postValue(res)
            _wait.postValue(false)
        }
    }

    suspend fun readDocList(map: Map<String, String>) {
        Log.d("jop", "-- readDocList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["IdLpu"].toString(), map["IdSpesiality"].toString(), map["idPat"].toString())
            val res = Hub2().getDocList("GetDoctorList", args)
            _docList.postValue(res)
            _wait.postValue(false)
        }
    }

    suspend fun readTalonList(map: Map<String, String>) {
        Log.d("jop", "-- readTalonList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["IdLpu"].toString(), map["IdDoc"].toString(), map["idPat"].toString())
            val res = Hub2().getTalonList("GetAvaibleAppointments", args)
            _talonList.postValue(res)
            _wait.postValue(false)
        }
    }

    suspend fun readHistList(map: Map<String, String>) {
        Log.d("jop", "-- readHistList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["IdLpu"].toString(), map["idPat"].toString())
            val res = Hub2().getHistList("GetPatientHistory", args)
            _histList.postValue(res)
            _wait.postValue(false)
        }
    }

    suspend fun getTalon(map: Map<String, String>) {
        Log.d("jop", "-- getTalon")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["IdLpu"].toString(), map["IdAppointment"].toString(), map["idPat"].toString())
            val res = Hub2().getTalon("SetAppointment", args)
            if (res[0]["Success"] == "true") {
                val res2 = mutableListOf(mapOf("Success" to "true", "Delete" to "false"))
                _idTalon.postValue(res2[0])
            } else {
                val res2 = mutableListOf(mapOf("Success" to "false", "Delete" to "false"))
                _idTalon.postValue(res2[0])
            }
            _wait.postValue(false)
        }
    }

    suspend fun deleteTalon(map: Map<String, String>) {
        Log.d("jop", "-- deleteTalon")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            val args = arrayOf(map["IdLpu"].toString(), map["idPat"].toString(), map["IdAppointment"].toString())
            val res = Hub2().deleteTalon("CreateClaimForRefusal", args)
            if (res[0]["Success"] == "true") {
                val res2 = mutableListOf(mapOf("Success" to "true", "Delete" to "true"))
                _idTalon.postValue(res2[0])
            } else {
                val res2 = mutableListOf(mapOf("Success" to "false", "Delete" to "true"))
                _idTalon.postValue(res2[0])
            }
            _wait.postValue(false)
        }
    }

    suspend fun addFlopsToList(map: Map<String, String>) {
        Log.d("jop", "-- addFlopsToList")
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            var ll = if (!flopsList.value.isNullOrEmpty()) flopsList.value as List else listOf()
            _flopsList.postValue(ll.plus(map))
            _wait.postValue(false)
        }
    }

}