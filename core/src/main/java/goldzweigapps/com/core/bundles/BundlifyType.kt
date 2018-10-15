package goldzweigapps.com.core.bundles

/**
 * Created by gilgoldzweig on 05/11/2017.
 */
interface BundlifyType<E: Any> {
    var key: String
    var value: E
}