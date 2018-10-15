package goldzweigapps.com.core.other

/**
 * Created by gilgoldzweig on 05/11/2017.
 */
open class LazyInitializeObject<T: Any> {
    protected lateinit var type: T

    open fun initialize(type: T) {
        try {
            this.type
        } catch (ex: UninitializedPropertyAccessException) {
            this.type = type
        }
    }
}