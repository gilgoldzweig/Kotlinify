package goldzweigapps.com.extentions

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import goldzweigapps.com.core.bundles.bundle
import goldzweigapps.com.core.bundles.contains
import goldzweigapps.com.core.preferences.GlobalSharedPreferences
import goldzweigapps.com.core.preferences.pref
import goldzweigapps.com.core.support.fragment
import goldzweigapps.com.core.threads.*
import goldzweigapps.com.jackson.fromJson
import goldzweigapps.com.jackson.toJson
import goldzweigapps.com.jackson.toPrettyJson
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val jsonDate = Date()
    val jsonString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonDate.toJson()
        jsonDate.toPrettyJson()
        jsonString.fromJson<String>()
        //single function
        runInBackground {

        }

        //multiple functions
        runInBackground({}, {}, {})
        runOnUI {

        }
        runAfter(millis = 2000, thread = RunnableThread.UI) {

        }
        runAfter(millis = 2000, thread = RunnableThread.BACKGROUND) {

        }
        runAfter(millis = 2000, thread = RunnableThread.CURRENT) {

        }
        runAfter(millis = 2000) {

        }

        isUiThread()
        GlobalSharedPreferences.initialize(application, "")


        pref {
            //put key(String), value(Any)
            //String
            put("key", "value")
            //Int
            put("key", 1)
            //Long
            put("key", 1L)
            //Float
            put("key", 1.5131F)

            "key" += 5
            this += "keyPair" to 6L

            //remove
            remove("keyToRemove")
            this - ""

        }
        "key" in GlobalSharedPreferences
        (GlobalSharedPreferences - "").apply()
        val bundle = bundle {
            //put key(String), value(Any)
            //String
            put("key", "value")
            //Int
            put("key", 1)
            //Long
            put("key", 1L)
            //Float
            put("key", 1.5131F)
            //parcelable
            put("key", Parcelable) //Some parcelable object
            //parcelable array
            put("key", Array<Parcelable>()) //Some parcelable Array
            //parcelable arrayList
            put("key", ArrayList<Parcelable>())//Some parcelable ArrayList

            "key" += 5
            this += "keyPair" to 6L

            //remove
            remove("keyToRemove")
            this - ""

        }
        "key" in bundle

        fragment(layoutRes = R.layout.activity_main) {

            arguments = bundle
            var name: TextView
            onViewCreated { view, context, savedInstanceState ->
                name = view.findViewById(R.id.text)
            }
        }

    }
}
