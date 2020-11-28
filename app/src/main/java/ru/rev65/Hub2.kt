@file:Suppress("unused", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName",
    "LocalVariableName", "LocalVariableName", "LocalVariableName", "LocalVariableName"
)

package ru.rev65

import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class Hub2 {
    //private val _soapPURL = "https://api.gorzdrav.spb.ru/Service/HubService.svc?wsdl"
    private val _soapPURL = "https://api.gorzdrav.spb.ru/Proxy/HubService.svc?wsdl"
    private val _xTEM = "http://tempuri.org/"
    private val _xHUB = "http://schemas.datacontract.org/2004/07/HubService2"
    private val _gUID = "6b2158a1-56e0-4c09-b70b-139b14ffee14"

    private fun getSoap(action: String, request: SoapObject): MutableList<Map<String, String>> {
        val list: MutableList<Map<String, String>> = mutableListOf()
        request.addProperty("guid", _gUID)
        val soapACTION = "http://tempuri.org/IHubService/$action"
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER10)
        envelope.bodyOut = request
        envelope.implicitTypes = true
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)
        val androidHttpTransport = HttpTransportSE(_soapPURL)
        try {
            androidHttpTransport.debug = true
            androidHttpTransport.call(soapACTION, envelope)
            //Log.d("jop", androidHttpTransport.requestDump)
            //Log.d("jop", androidHttpTransport.responseDump)
            //if (action.equals("CheckPatient")) Log.d("jop", androidHttpTransport.responseDump)
            val soapObj = envelope.response as SoapObject
            processObj(soapObj, PropertyInfo(), 0, list)
        }
        catch (e: Exception) {}
        return list
    }

    private fun processObj(
        obj: Any,
        info: PropertyInfo,
        level: Int,
        list: MutableList<Map<String, String>>
    ) {//: MutableMap<String, String> {
        val result: MutableMap<String, String> = mutableMapOf()
        var t = "\t"
        for (l in 0 until level) t += "\t"
        val row: MutableMap<String, String> = mutableMapOf()
        if (obj is SoapObject) {
            for (i in 0 until obj.propertyCount) {
                val obj2 = obj.getProperty(i)
                val info2 = obj.getPropertyInfo(i)
                if (info2.value is SoapObject) {
                    //Log.d("jop", "$t $i ${info2.name}")
                    processObj(obj2, info2, level + 1, list)
                } else {
                    //Log.d("jop", "$t $i ${info2.name} ${info2.value}")
                    row[info2.name] = "${info2.value}"
                }
            }
            result.putAll(row)
        }
        else {
            row[info.name] = "${info.value}"
            result.putAll(row)
        }
        if (result.count() > 0) list.add(result)
        //return result
    }

    fun getDistrList(action: String): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        return getSoap(action, request)
    }

    fun getLpuList(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("IdDistrict", args[0])
        return getSoap(action, request)
    }

    fun checkPat(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        val pat = SoapObject(_xTEM, "pat")
        pat.addProperty(_xHUB, "Birthday", args[3])
        pat.addProperty(_xHUB, "Name", args[1])
        pat.addProperty(_xHUB, "SecondName", args[2])
        pat.addProperty(_xHUB, "Surname", args[0])
        request.addSoapObject(pat)
        request.addProperty("idLpu", args[4])
        return getSoap(action, request)
    }

    fun getSpecList(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("idLpu", args[0])
        return getSoap(action, request)
    }

    fun getDocList(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("idSpesiality", args[1])
        request.addProperty("idLpu", args[0])
        request.addProperty("idPat", args[2])
        return getSoap(action, request)
    }

    fun getTalonList(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("idDoc", args[1])
        request.addProperty("idLpu", args[0])
        request.addProperty("idPat", args[2])
        request.addProperty("visitStart", "2020-11-01")
        request.addProperty("visitEnd", "2020-12-31")
        return getSoap(action, request)
    }

    fun getHistList(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("idLpu", args[0])
        request.addProperty("idPat", args[1])
        return getSoap2(action, request)
    }

    fun getTalon(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("idAppointment", args[1])
        request.addProperty("idLpu", args[0])
        request.addProperty("idPat", args[2])
        return getSoap(action, request)
    }

    private fun getSoap2(action: String, request: SoapObject): MutableList<Map<String, String>> {
        val list: MutableList<Map<String, String>> = mutableListOf()
        request.addProperty("guid", _gUID)
        val soapACTION = "http://tempuri.org/IHubService/$action"
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER10)
        envelope.bodyOut = request
        envelope.implicitTypes = true
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)
        val soap = HttpTransportSE(_soapPURL)
        try {
            soap.debug = true
            soap.call(soapACTION, envelope)
            val resultobj = envelope.bodyIn as SoapObject
            val rootobj = resultobj.getProperty(0) as SoapObject
            val ListHistoryVisit = rootobj.getProperty("ListHistoryVisit") as SoapObject
            for (i in 0 until ListHistoryVisit.propertyCount) {
                val row: MutableMap<String, String> = mutableMapOf()
                val obj = ListHistoryVisit.getProperty(i) as SoapObject
                val DateCreatedAppointment = obj.getPrimitivePropertyAsString("DateCreatedAppointment")
                val IdAppointment = obj.getPrimitivePropertyAsString("IdAppointment")
                val VisitStart = obj.getPrimitivePropertyAsString("VisitStart")
                val doc = obj.getProperty("DoctorRendingConsultation") as SoapObject
                val Name = doc.getPrimitivePropertyAsString("Name")
                val sec = obj.getProperty("SpecialityRendingConsultation") as SoapObject
                val NameSpesiality = sec.getPrimitivePropertyAsString("NameSpesiality")

                row["DateCreatedAppointment"] = DateCreatedAppointment
                row["IdAppointment"] = IdAppointment
                row["VisitStart"] = VisitStart
                row["Name"] =  Name
                row["NameSpesiality"] = NameSpesiality

                //Log.d("jop", "~~ $DateCreatedAppointment $IdAppointment $VisitStart $Name $NameSpesiality")
                list.add(row)
            }
        }
        catch (e: Exception) {}
        return list
    }

    fun deleteTalon(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val request = SoapObject(_xTEM, action)
        request.addProperty("idLpu", args[0])
        request.addProperty("idPat", args[1])
        request.addProperty("idAppointment", args[2])
        return getSoap(action, request)
    }

}

