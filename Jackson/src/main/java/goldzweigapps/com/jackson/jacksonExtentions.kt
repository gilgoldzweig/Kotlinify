package goldzweigapps.com.jackson

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Created by gilgoldzweig on 13/09/2017.
 */
val objectMapper = ObjectMapper()

fun Any.toJson() = objectMapper.writeValueAsString(this)
fun Any.toPrettyJson() = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(this)

inline fun <reified T : Any> String.fromJson() = objectMapper.readValue<T>(this)

@JsonIgnoreProperties(ignoreUnknown = true)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonIgnoreUnknown