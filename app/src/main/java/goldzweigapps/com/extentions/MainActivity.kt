package goldzweigapps.com.extentions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import goldzweigapps.com.core.threads.RunnableThread
import goldzweigapps.com.timber.Timber

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())


//        findViewById<>()
//        Timber.plant(Timber.DebugTree())
//        runAfter(2000, RunnableThread.BACKGROUND) {
//            "run a function in background".d()
//            if (isUiThread()) "Something is not working".e()
//        }
//        runAfter(3000, RunnableThread.UI) {
//            "run a function on ui".d()
//            if (isUiThread()) "Something is working".d()
//        }
//        notificationManager {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                showSingle(15,notification("channel", 15) {
//                    title = "testTitle"
//                    contentText = "contextTextTest"
//                    smallIconRes = R.mipmap.ic_launcher_round
//                    intent = PendingIntent.getActivity(this@MainActivity,
//                            5,
//                            Intent(),
//                            PendingIntent.FLAG_UPDATE_CURRENT)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        action {
//                            title = "customTitle"
//                            icon = Icon.createWithResource(this@MainActivity, R.mipmap.ic_launcher_round)
//                            intent = PendingIntent.getActivity(this@MainActivity,
//                                    5,
//                                    Intent(),
//                                    PendingIntent.FLAG_UPDATE_CURRENT)
//                        }
//                    }
//                })
//            }
//        }
//
//        runInBackground {
//            if (isUiThread()) "Something is not working".e()
//            runOnUiThread {
//                if (isUiThread()) "Something is working".d()
//            }
//        }


//        locationObject.toJson() //{"locationText":"Tel-Aviv","locationCoordinates":[5.152155,1512.512]}
//        locationObject.toPrettyJson()
        /**{
        "locationText": "Tel-Aviv",
        "locationCoordinates": [
        5.152155,
        1512.512
        ]
        }*/
//        jsonString.fromJson<LocationObject>() // A new LocationObject
//
//
//        //single function
//        runInBackground {
//
//        }
//
//        //multiple functions
//        runInBackground({}, {}, {})
//        runOnUI {
//
//        }
//        runAfter(2000) {
//
//        }
//        runAfter(millis = 2000, thread = RunnableThread.BACKGROUND) {
//
//        }
//        runAfter(millis = 2000, thread = RunnableThread.CURRENT) {
//
//        }
//        runAfter(millis = 2000) {
//
//        }
//
//        isUiThread()
//        GlobalSharedPreferences.initialize(application, "")
//
//
//        pref {
//            //put key(String), value(Any)
//            //String
//            put("key", "value")
//            //Int
//            put("key", 1)
//            //Long
//            put("key", 1L)
//            //Float
//            put("key", 1.5131F)
//
//            "key" += 5
//            this += "keyPair" to 6L
//
//            //remove
//            remove("keyToRemove")
//            this - ""
//
//        }
//        "key" in GlobalSharedPreferences
//        (GlobalSharedPreferences - "").apply()
//
//        val bundle = bundle {
//            //put key(String), value(Any)
//            //String
//            put("key", "value")
//            //Int
//            put("key", 1)
//            //Long
//            put("key", 1L)
//            //Float
//            put("key", 1.5131F)
//            //parcelable
//            put("key", Parcelable) //Some parcelable object
//            //parcelable array
//            put("key", Array<Parcelable>()) //Some parcelable Array
//            //parcelable arrayList
//            put("key", ArrayList<Parcelable>())//Some parcelable ArrayList
//
//            "key" += 5
//            this += "keyPair" to 6L
//
//            //remove
//            remove("keyToRemove")
//            this - ""
//
//        }
//        "key" in bundle
//
//        val re = Recy(this)
//        re + UserKT()
//        re - UserKT()
//        for (count in 0..100) {
//
//        }

//        fragment(layoutRes = R.layout.activity_main) {
//
//            arguments = bundle
//            var name: TextView
//            onViewCreated { view, context, savedInstanceState ->
//                name = view.findViewById(R.id.text)
//
//            }
//        }
//        5.toDp()
//
//        val group = LinearLayout(this)
//        group += TextView(this)
//        group += TextView(this)
//        group += TextView(this)
//        group += TextView(this)
//        "in" in TextView(this)
//        group -= TextView(this)
//        group[0]
//        for (view in group.iterator()) {
//
//        }
//        group.first()
//        group.last()
//        group.forEach {  }
//        group.forEachIndexed { i, view ->  }
//        group.forEachRevered {  }
//        group.forEachReveredIndexed { i, view ->  }
//
//        group.inflate(R.layout.some_layout)
//        this.inflate(R.layout.some_layout, false, group)
//
//        View(this).onClick {
//
//        }
//        View(this).onLongClick {
//
//        }
//        View(this).hide()
//        View(this).hide()
//        View(this).show()
//        View(this).invisible()
//        View(this).toggleVisibility()
//        View(this).isVisible()
//
//
//
//        isVersionAbove(23)
//
//        val customRecyclerAdapter = CustomRecyclerAdapter(this, ArrayList())
//        customRecyclerAdapter - "" //removing item and notifying the adapter if you are on uiThread
//        customRecyclerAdapter - 2 //removing item and notifying the adapter if you are on uiThread
//        customRecyclerAdapter + "" //adding item and notifying the adapter if you are on uiThread
//        customRecyclerAdapter.add("",5)
//        customRecyclerAdapter[""] //getItem by object
//        customRecyclerAdapter[12] //getItem by position
//        customRecyclerAdapter - (1..4) //removing position range and notifying the adapter if you are on uiThread
//        customRecyclerAdapter - (listOf("", "")) //removing list of objects and notifying the adapter if you are on uiThread
//        customRecyclerAdapter.setItem(5, "gg")
//        customRecyclerAdapter.clear()
//        customRecyclerAdapter.count()
//        customRecyclerAdapter.setItems(listOf("", "", "", "", "")) //replace the current list and notifying the adapter if you are on uiThread
//        customRecyclerAdapter.isEmpty()
//        customRecyclerAdapter.isNotEmpty()
//
//        ColorGenerator.DEFAULT_COLOR_LIST
//        ColorGenerator.instance.randomColor
//
//        ColorGenerator.MATERIAL_COLOR_LIST
//        ColorGenerator.materialInstance.randomColor
//
//        val testMap = mapOf(0 to 9, 0 to 9, 0 to 9, 0 to 9, 0 to 9)
//        val testSet = setOf(0 to 9, 0 to 9, 0 to 9, 0 to 9, 0 to 9)
//        val testList = listOf(0 to 9, 0 to 9, 0 to 9, 0 to 9, 0 to 9)
//        val testArrayList = ArrayList<Int>()
//
//        testMap.isNullOrEmpty()
//        testMap.isNotNullOrEmpty()
//
//        testSet.isNullOrEmpty()
//        testSet.isNotNullOrEmpty()
//
//        testArrayList.isNullOrEmpty()
//        testArrayList.isNotNullOrEmpty()
//        testArrayList addIfNotExist 5
//        testArrayList removeIfExist 5
//
//
//        testList.isNullOrEmpty()
//        testList.isNotNullOrEmpty()
//        testList / 3 //returns a map of page number and the amount of items given in this case 3
//        testList.random() //returns a random element from the list
//


    }
}
class NewRunnableThread: RunnableThread {
    override fun run(vararg functions: () -> Unit) {
        for (func in functions) {
            Thread({ func.invoke() }).start()
        }
    }
}