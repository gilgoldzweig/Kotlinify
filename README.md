# Kotlinify

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
GlobalSharedPreferences.initialize(this, "sharedPreferencesName")
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
            arguments = bundle // we you the bundle from before to insert the arguments (optinal, you can use a normal Bundle)
            var name: TextView // declaring your views
            onViewCreated { view, context, savedInstanceState ->
                name = view.findViewById(R.id.text) //accessing the views 
            }
        }
```
### Notification
### Support
### threads
### views
### collections
### recourses
### permission
### colors
### database

# Reactive
##### The core module provides the following classes and extensions.

### Bundlify
# Jackson
##### The core module provides the following classes and extensions.

### Bundlify
# Timber
##### The core module provides the following classes and extensions.

### Bundlify
License
----

MIT


**Free Software, Hell Yeah!**
