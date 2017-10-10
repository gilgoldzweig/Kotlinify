package goldzweigapps.com.core.exceptions

class UnsupportedTypeException(classType: String) :
        RuntimeException("SharedPreferences does not support $classType")