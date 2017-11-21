package goldzweigapps.com.jackson

import org.junit.Assert
import org.junit.Test

/**
 * Created by gilgoldzweig on 19/10/2017.
 */
class JacksonUnitTest {
    val simplePersonTestJson = "{\"firstName\":\"testFirstName\",\"lastName\":\"testFamilyName\",\"age\":41}"
    val simplePersonTestPrettyJson = "{\n" +
            "  \"firstName\" : \"testFirstName\",\n" +
            "  \"lastName\" : \"testFamilyName\",\n" +
            "  \"age\" : 41\n" +
            "}\n"
    @Test
    fun tojson_isCurrent() {
        Assert.assertEquals(simplePersonTestJson,
                PersonTestObject("testFirstName", "testFamilyName", 41)
                        .toJson())
    }
    @Test
    fun prettyjson_IsCurrect() {
        Assert.assertEquals(simplePersonTestJson,
                PersonTestObject("testFirstName", "testFamilyName", 41)
                        .toPrettyJson())
    }
}

data class PersonTestObject(var firstName: String = "",
                            var lastName: String = "",
                            var age: Int = 0)