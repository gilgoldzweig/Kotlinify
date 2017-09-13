package goldzweigapps.com.jackson

/**
 * Created by gilgoldzweig on 13/09/2017.
 */
val objectMapper = ObjectMapper()
fun Any.toJson(): String = objectMapper.writeValueAsString(this)
fun <T: Any> String.fromJson(): String = objectMapper.readValue(this)