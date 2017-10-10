package goldzweigapps.com.reactive.rxbus.entity

data class EventType(
        /**
         * Event Tag
         */
        private val tag: String,
        /**
         * Event Clazz
         */
        private val clazz: Class<*>) {

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true


        if (obj == null) return false


        if (javaClass != obj.javaClass) return false


        val other = obj as EventType?

        return tag == other!!.tag && clazz == other.clazz
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + clazz.hashCode()
        return result
    }

}