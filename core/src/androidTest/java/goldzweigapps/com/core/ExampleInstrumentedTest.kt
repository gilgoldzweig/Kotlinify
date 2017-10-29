package goldzweigapps.com.core

import android.app.Application
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import goldzweigapps.com.core.preferences.GlobalSharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class e {
    var name: String by generic<String>()
}
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var application: Application
    var name: String by generic<String>()
    var age by preferenceProperty<Int>()
    var latitude by preferenceProperty<Float>()
    @Before
    fun setAppContext() {
        application = InstrumentationRegistry.getTargetContext().applicationContext as Application
        GlobalSharedPreferences.initialize(application)
    }


    @Test
    @Throws(Exception::class)
    fun preferences_delegate_test() {
        // Context of the app under test.
        Looper.prepare()
        name = "Gil Goldzweig"
        age = 18
        latitude = 25.4249124f
        println("name: $name, age: $age, latitude: $latitude")
    }
}
