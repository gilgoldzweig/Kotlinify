package goldzweigapps.com.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.net.URL

/**
 * Created by gilgoldzweig on 13/09/2017.
 */
val objectMapper by lazy { ObjectMapper() }

fun Any.toJson(): String = objectMapper.writeValueAsString(this)
fun Any.toPrettyJson(): String = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(this)

inline fun <reified T : Any> String.fromJson() = objectMapper.readValue<T>(this)
inline fun <reified T : Any> InputStream.fromJson() = objectMapper.readValue<T>(this)
inline fun <reified T : Any> JsonParser.fromJson() = objectMapper.readValue<T>(this)
inline fun <reified T : Any> ByteArray.fromJson() = objectMapper.readValue<T>(this)
inline fun <reified T : Any> File.fromJson() = objectMapper.readValue<T>(this)
inline fun <reified T : Any> Reader.fromJson() = objectMapper.readValue<T>(this)
inline fun <reified T : Any> URL.fromJson() = objectMapper.readValue<T>(this)
