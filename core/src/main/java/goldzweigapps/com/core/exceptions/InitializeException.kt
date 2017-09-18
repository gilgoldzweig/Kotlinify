package goldzweigapps.com.core.exceptions

/**
 * Created by gilgoldzweig on 17/09/2017.
 * custom exception to handle cases where user did not initialized the wanted class
 * @param className the name of the class in order to print the right class that caused the exception
 * @param initializeFunctionName the name of the initialize function in order to explain to user how to initialize
 */
class InitializeException(className: String,
                          initializeFunctionName: String
) : RuntimeException("$className must be initialize,\ncall $initializeFunctionName in order to initialize")