package goldzweigapps.com.extentions

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
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
        GlobalSharedPreferences.initialize(this)

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
            put("key", Parcelable)
            //parcelable array
            put("key", Array<Parcelable>())
            //parcelable arrayList
            put("key", ArrayList<Parcelable>())

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
