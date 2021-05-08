package ru.rev69

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import java.util.*

val c = Calendar.getInstance()
val year = c.get(Calendar.YEAR)
val month = c.get(Calendar.MONTH)
val day = c.get(Calendar.DAY_OF_MONTH)
/*
val datePickerDialog = DatePickerDialog(
    this, DatePickerDialog.OnDateSetListener
    { datePicker: DatePicker, day: Int, month: Int, year: Int ->

        setContent {
            Column {
                Text("$day, $month, $year")
            }
        }

    }, year, month, day
)
model.dialog = datePickerDialog
checkPermission(42, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
*/

/*
private fun checkPermission(callbackId: Int, vararg permissionsId: String) {
    var permissions = true
    for (p in permissionsId) {
        permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PermissionChecker.PERMISSION_GRANTED
    }
    if (!permissions) ActivityCompat.requestPermissions(this, permissionsId, callbackId)
}
*/

/*
fun doCalendar(){
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
*/

/*
                    "Информация" -> {
                        Text("${Build.MANUFACTURER} ${Build.MODEL}")
                        val s = String.format("%.2f", model.mf)
                        Button(onClick = {
                            model.runf()
                        }) { Text(s) }
                        model.mf = 0f
                        LazyColumn {items(flops.size) { flopsItems(flops[it], model) }}
                    }

 */