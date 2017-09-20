# Kotlinify

![Release](https://jitpack.io/v/GilGoldzweig/kotlinify.svg)


Kotlinify is a suite of extention and classes for easier android development in kotlin. 
I've created it after my own needs and i decided i should publish it and save time for others.

The library contanis 4 modules:
  - Core extensions.
  - Reactive extensions.
  - Jackson extensions.
  - Custom edition of JakeWharton/timber
## Installiton 
In your app's build.gradle add the following.
```
repositories {
    maven {url "https://jitpack.io" }
}

dependencies {
    You can add each module seperatly 
    
    //All of the modules
   implementation 'com.github.GilGoldzweig:kotlinify:latestVersion' 
   // Core module
   implementation 'com.github.GilGoldzweig.kotlinify:core:latestVersion'
     // Reactive module
   implementation 'com.github.GilGoldzweig.kotlinify:reactive:latestVersion'
     // Jackson module
   implementation 'com.github.GilGoldzweig.kotlinify:jackson:latestVersion' 
   // Timber module
   implementation 'com.github.GilGoldzweig.kotlinify:timber:latestVersion' 
}
```

# Core
##### The core module provides the following classes and extensions.


### Bundlify
Let's you create bundles in a simple way and with operators.
```
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
        
```
### GlobalSharedPreferences
GlobalSharedPrefrences a SharedPreferences object that let you use it anywhere without the direct access to context. The class also contains an easy usage
```
First initialize it in your application class 
//You don't have to provide a name if nothing is placed it will use the default value
GlobalSharedPreferences.initialize(application, "sharedPreferencesName")
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
            
        } //not need to apply inside pref
        "key" in GlobalSharedPreferences // return's Boolean
        (GlobalSharedPreferences += "keyPair" to 6L).apply()
        (GlobalSharedPreferences - "key").apply()
```
### Fragment
An easier way to create a fragment using a dsl extension of fragment 
```
fragment(layoutRes = R.layout.fragment_test) {
//every field in fragment is accessable from here
            arguments = bundle // we use the bundle from before to insert the arguments (optinal, you can use a normal Bundle)
            var name: TextView // declaring your views
            onViewCreated { view, context, savedInstanceState ->
                name = view.findViewById(R.id.text) //accessing the views 
            }
        } //return's a ready to use fragment
```
### Notification 
//You can use but it did not get tested so i'm not writing description 
//will be added soon
### GenericRecyclerAdapter
An abstract extension of RecyclerView.Adapter that reduces the creating time of a RecyclerView.
In additon you receive a lot of extension function to make the adapter work simieler to List so by extending the class you receive a lot of bounses
```
The adapter 
class CustomRecyclerAdapter(context: Context, // Require context to make it easily accessible
                            listOfObjects: ArrayList<String> = ArrayList() //Put your list of objects 
):
        GenericRecyclerAdapter<String>(context,
                ArrayList() 
                //the list of objects could by empty and it will use the default value
                , R.layout.item_recycler_test // the item's layout currently one one view type supported
        ) {
    
    override fun View.onBind(currentElementPosition: Int, 
                             currentElement: String, // the current element with the type provided above in this example a String
                             holder: GenericViewHolder) {
        //because of the extension of View i can just call
        // findViewById and that's it you don't need to call holder.view.findViewById
    }
}
extension given 
val customRecyclerAdapter = CustomRecyclerAdapter(this, ArrayList())
customRecyclerAdapter - "" //removing item and notifying the adapter if you are on uiThread
customRecyclerAdapter - 2 //removing item and notifying the adapter if you are on uiThread
customRecyclerAdapter + "" //adding item and notifying the adapter if you are on uiThread
customRecyclerAdapter.add("",5)
customRecyclerAdapter[""] //getItem by object
customRecyclerAdapter[12] //getItem by position
customRecyclerAdapter - (1..4) //removing position range and notifying the adapter if you are on uiThread
customRecyclerAdapter - (listOf("", "")) //removing list of objects and notifying the adapter if you are on uiThread
customRecyclerAdapter.setItem(5, "gg")
customRecyclerAdapter.clear()
customRecyclerAdapter.count()
customRecyclerAdapter.setItems(listOf("", "", "", "", "")) //replace the current list and notifying the adapter if you are on uiThread
customRecyclerAdapter.isEmpty()
customRecyclerAdapter.isNotEmpty()
```
### threads
```
        //single function to run in background
        runInBackground {

        }

        //multiple functions to run in background
        runInBackground({}, {}, {})
        
        //function to run on ui thread
        runOnUI {

        }
        //run's a task after 2 secounds on UI thread
        runAfter(millis = 2000, thread = RunnableThread.UI) {

        }
        //run's a task after 2 secounds in background
        runAfter(millis = 2000, thread = RunnableThread.BACKGROUND) {

        }
        //run's a task after 2 secounds on the current thread
        runAfter(millis = 2000, thread = RunnableThread.CURRENT) {

        }
        //run's a task after 2 secounds
        runAfter(millis = 2000) {

        }

        isUiThread() //Boolean is the current thread is UI
```
### views
``` 
val group = LinearLayout(this)//LinearLayout as example can be any ViewGroup

group += TextView(this)
group += TextView(this)
group += TextView(this)
group += TextView(this)
"in" in TextView(this)
group -= TextView(this)
group[0]
for (view in group.iterator()) {

}
group.first()
group.last()
group.forEach {  }
group.forEachIndexed { i, view ->  }
group.forEachRevered {  }
group.forEachReveredIndexed { i, view ->  }

group.inflate(R.layout.some_layout)
this.inflate(R.layout.some_layout, false, group) //this = context

View(this).onClick { 
    
}
View(this).onLongClick {
    
}
View(this).hide()
View(this).hide()
View(this).show()
View(this).invisible()
View(this).toggleVisibility()
View(this).isVisible()
```
### collections
```
 val testMap = mapOf(0 to 9, 0 to 9, 0 to 9, 0 to 9, 0 to 9)
val testSet = setOf(0 to 9, 0 to 9, 0 to 9, 0 to 9, 0 to 9)
val testList = listOf(0 to 9, 0 to 9, 0 to 9, 0 to 9, 0 to 9)
val testArrayList = ArrayList<Int>()

testMap.isNullOrEmpty()
testMap.isNotNullOrEmpty()

testSet.isNullOrEmpty()
testSet.isNotNullOrEmpty()

testArrayList.isNullOrEmpty()
testArrayList.isNotNullOrEmpty()
testArrayList addIfNotExist 5
testArrayList removeIfExist 5


testList.isNullOrEmpty()
testList.isNotNullOrEmpty()
testList / 3 //returns a map of page number and the amount of items given in this case 3
testList.random() //returns a random element from the list
        
```
### resourses
Provides two extensions 
```
15.toDp() //return's the number as a convertion from px to dp
15.toPx()//return's the number as a convertion from dp to px
```
### permission
Provides few functions
```
isVersionAbove(26) // boolean 
isVersionAbove(14) // boolean same as above but with diffrent version

isMarshmallowOrAbove() //boolean is current version is at least Marshmallow(23)
isLollipopOrAbove()  //boolean is current version is at least Lollipop(21)

Context.isGranted("StringPermission") // boolean checks if the permission is granted or not

```
### ColorGenerator 
A class with 2 lists of colors one normal colors and one material design colors 
the class let's you get all the colors or a random color
```
ColorGenerator.DEFAULT_COLOR_LIST
ColorGenerator.instance.randomColor

ColorGenerator.MATERIAL_COLOR_LIST
ColorGenerator.materialInstance.randomColor
```


# Reactive
##### The Reactive module provides the following extensions.
Provides two extension functions to most if not all reactive types
```
/**
 * observe on main thread
 * subscribe on new thread
 * unsubsidised on error and on complete and removes the need to handle it afterwards
 * @usage
 * someObservable //or any other reactive type
 *  .runSafeOnMain()
 *  .subscribe({}, {])
 */
fun <T> Observable<T>.runSafeOnMain(): Observable<T> =
        observeOn(mainThread)
                .subscribeOn(newThread)
                .doOnError({ unsubscribeOn(newThread) })
                .doOnComplete { unsubscribeOn(newThread) }

/**
 * observe on io thread
 * subscribe on new thread
 * unsubsidised on error and on complete and removes the need to handle it afterwards
 * @usage
 * someObservable //or any other reactive type
 *  .runSafeOnIO()
 *  .subscribe({}, {])
 */
fun <T> Observable<T>.runSafeOnIO(): Observable<T> =
        observeOn(ioThread)
                .subscribeOn(newThread)
                .doOnError({ unsubscribeOn(newThread) })
                .doOnComplete { unsubscribeOn(newThread) }
```

# Jackson
##### The jackson module provides a few extension functions and an annotation
```
val locationObject = LocationObject("Tel-Aviv", arrayOf(5.152155, 1512.5120))

val jsonString = "{\"locationText\":\"Tel-Aviv\",\"locationCoordinates\":[5.152155,1512.512]}"

locationObject.toJson() //{"locationText":"Tel-Aviv","locationCoordinates":[5.152155,1512.512]}


locationObject.toPrettyJson() /** {
        "locationText": "Tel-Aviv",
        "locationCoordinates": [
            5.152155,
            1512.512
            ]
        }*/
        
jsonString.fromJson<LocationObject>() // A new LocationObject 
 
 LocationObject
 
 @JsonIgnoreUnknown //A new annotation to make it easier to use jackson  instad of @JsonIgnoreProperties(ignoreUnknown = true)
data class LocationObject(val locationText: String, val locationCoordinates: Array<Double>)
```

# Timber
##### The timber module provides the following class.
Adds a possibilty to print any object using timber a very small change but saves a lot of annying toString()
```
You can use it like so or just put the object
Timber.d(someObject.toString())
Timber.d(5.toString())
Timber.d(someBoolean.toString())

//you can just do like so
Timber.d(someObject)
Timber.d(5)
Timber.d(someBoolean)
someObject.d()// no need to call Timber 
someOtherObject.e()// no need to call Timber 
someOtherObject.i()// no need to call Timber 
someOtherObject.wtf()// no need to call Timber 
someOtherObject.w()// no need to call Timber 
```

License
----

MIT


**Free Software, Hell Yeah!**
