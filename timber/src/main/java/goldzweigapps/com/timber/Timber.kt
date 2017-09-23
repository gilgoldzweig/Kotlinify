package goldzweigapps.com.timber

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.*
import java.util.*
import java.util.Collections.unmodifiableList
import java.util.regex.Pattern

@Suppress("unused")
object Timber {

    /**
     * A facade for handling logging calls. Install instances via [Timber.plant()][.plant].
     */
    abstract class Tree {
        internal val explicitTag = ThreadLocal<String>()

        internal open val tag: String?
            get() {
                val tag = explicitTag.get()
                if (tag != null) {
                    explicitTag.remove()
                }
                return tag
            }

        /**
         * Log a verbose message workWith optional format args.
         */
        open fun v(message: Any?, vararg args: Any) {
            prepareLog(Log.VERBOSE, null, message, *args)
        }

        /**
         * Log a verbose exception and a message workWith optional format args.
         */
        open fun v(t: Throwable, message: Any, vararg args: Any) {
            prepareLog(Log.VERBOSE, t, message, *args)
        }

        /**
         * Log a verbose exception.
         */
        open fun v(t: Throwable) {
            prepareLog(Log.VERBOSE, t, null)
        }

        /**
         * Log a debug message workWith optional format args.
         */
        open fun d(message: Any, vararg args: Any) {
            prepareLog(Log.DEBUG, null, message, *args)
        }

        /**
         * Log a debug exception and a message workWith optional format args.
         */
        open fun d(t: Throwable, message: Any, vararg args: Any) {
            prepareLog(Log.DEBUG, t, message, *args)
        }

        /**
         * Log a debug exception.
         */
        open fun d(t: Throwable) {
            prepareLog(Log.DEBUG, t, null)
        }

        /**
         * Log an info message workWith optional format args.
         */
        open fun i(message: Any, vararg args: Any) {
            prepareLog(Log.INFO, null, message, *args)
        }

        /**
         * Log an info exception and a message workWith optional format args.
         */
        open fun i(t: Throwable, message: Any, vararg args: Any) {
            prepareLog(Log.INFO, t, message, *args)
        }

        /**
         * Log an info exception.
         */
        open fun i(t: Throwable) {
            prepareLog(Log.INFO, t, null)
        }

        /**
         * Log a warning message workWith optional format args.
         */
        open fun w(message: Any, vararg args: Any) {
            prepareLog(Log.WARN, null, message, *args)
        }

        /**
         * Log a warning exception and a message workWith optional format args.
         */
        open fun w(t: Throwable, message: Any, vararg args: Any) {
            prepareLog(Log.WARN, t, message, *args)
        }

        /**
         * Log a warning exception.
         */
        open fun w(t: Throwable) {
            prepareLog(Log.WARN, t, null)
        }

        /**
         * Log an error message workWith optional format args.
         */
        open fun e(message: Any, vararg args: Any) {
            prepareLog(Log.ERROR, null, message, *args)
        }

        /**
         * Log an error exception and a message workWith optional format args.
         */
        open fun e(t: Throwable, message: Any, vararg args: Any) {
            prepareLog(Log.ERROR, t, message, *args)
        }

        /**
         * Log an error exception.
         */
        open fun e(t: Throwable) {
            prepareLog(Log.ERROR, t, null)
        }

        /**
         * Log an assert message workWith optional format args.
         */
        open fun wtf(message: Any, vararg args: Any) {
            prepareLog(Log.ASSERT, null, message, *args)
        }

        /**
         * Log an assert exception and a message workWith optional format args.
         */
        open fun wtf(t: Throwable, message: Any, vararg args: Any) {
            prepareLog(Log.ASSERT, t, message, *args)
        }

        /**
         * Log an assert exception.
         */
        open fun wtf(t: Throwable) {
            prepareLog(Log.ASSERT, t, null)
        }

        /**
         * Log at `priority` a message workWith optional format args.
         */
        open fun log(priority: Int, message: Any, vararg args: Any) {
            prepareLog(priority, null, message, *args)
        }

        /**
         * Log at `priority` an exception and a message workWith optional format args.
         */
        open fun log(priority: Int, t: Throwable, message: Any, vararg args: Any) {
            prepareLog(priority, t, message, *args)
        }

        /**
         * Log at `priority` an exception.
         */
        open fun log(priority: Int, t: Throwable) {
            prepareLog(priority, t, null)
        }

        private fun prepareLog(priority: Int, t: Throwable?, messageObject: Any?, vararg args: Any) {
            var message: String? = messageObject.toString()
            // Consume tag even when message is not loggable so that next message is correctly tagged.
            val tag = tag
            if (message != null && message.isEmpty()) {
                message = null
            }
            if (message == null) {
                if (t == null) {
                    return  // Swallow message if it's null and there's no throwable.
                }
                message = getStackTraceString(t)
            } else {
                if (args.isNotEmpty()) {
                    message = formatMessage(message, args)
                }
                if (t != null) {
                    message += "\n" + getStackTraceString(t)
                }
            }

            log(priority, tag, message, t)
        }

        /**
         * Formats a log message workWith optional arguments.
         */
        private fun formatMessage(message: String, args: Array<out Any>): String =
                String.format(message, *args)

        private fun getStackTraceString(t: Throwable): String {
            // Don't replace this workWith Log.getStackTraceString() - it hides
            // UnknownHostException, which is not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        /**
         * Write a log message to its destination. Called for all level-specific methods by default.
         *
         * @param priority Log level. See [Log] for constants.
         * @param tag      Explicit or inferred tag. May be `null`.
         * @param message  Formatted log message. May be `null`, but then `t` will not be.
         * @param t        Accompanying exceptions. May be `null`, but then `message` will not be.
         */
        protected abstract fun log(priority: Int, tag: String?, message: Any, t: Throwable?)
    }

    /**
     * A [Tree] for debug builds. Automatically infers the tag from the calling class.
     */
    class DebugTree : Tree() {

        override// DO NOT switch this to Thread.getCurrentThread().getStackTrace()
        val tag: String?
            get() {
                val tag = super.tag
                if (tag != null) {
                    return tag
                }
                val stackTrace = Throwable().stackTrace
                if (stackTrace.size <= CALL_STACK_INDEX) {
                    throw IllegalStateException(
                            "Synthetic stacktrace didn't have enough elements: are you using proguard?")
                }
                return createStackElementTag(stackTrace[CALL_STACK_INDEX])
            }

        /**
         * Extract the tag which should be used for the message from the `element`. By default
         * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
         * becomes `Foo`).
         *
         *
         * Note: This will not be called if a [manual tag][.tag] was specified.
         */
        private fun createStackElementTag(element: StackTraceElement): String {
            var tag = element.className
            val m = ANONYMOUS_CLASS.matcher(tag)
            if (m.find()) {
                tag = m.replaceAll("")
            }
            tag = tag.substring(tag.lastIndexOf('.') + 1)
            return if (tag.length > MAX_TAG_LENGTH) tag.substring(0, MAX_TAG_LENGTH) else tag
        }

        /**
         * Break up `message` into maximum-length chunks (if needed) and send to either
         * [Log.println()][Log.println] or
         * [Log.wtf()][Log.wtf] for logging.
         *
         *
         * {@inheritDoc}
         */
        override fun log(priority: Int, tag: String?, message: Any, t: Throwable?) {
            var messageString = message.toString()
            if (message is Exception) {
                messageString = message.cause.toString()
            }


            if (messageString.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, messageString)
                } else {
                    Log.println(priority, tag, messageString)
                }
                return
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length = messageString.length
            while (i < length) {
                var newline = messageString.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = Math.min(newline, i + MAX_LOG_LENGTH)
                    val part = messageString.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part)
                    } else {
                        Log.println(priority, tag, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }
        }

        companion object {
            private val MAX_LOG_LENGTH = 4000
            private val MAX_TAG_LENGTH = 23
            private val CALL_STACK_INDEX = 5
            private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        }
    }


    private val TREE_ARRAY_EMPTY = arrayOfNulls<Tree>(0)
    // Both fields guarded by 'FOREST'.
    private val FOREST = ArrayList<Tree>()
    @Volatile private var forestAsArray = TREE_ARRAY_EMPTY
    /**
     * A [Tree] that delegates to all planted trees in the [forest][.FOREST].
     */
    internal val TREE_OF_SOULS = object : Tree() {
        override fun v(message: Any?, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.v(message, *args)
                i++
            }
        }

        override fun v(t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.v(t, message, *args)
                i++
            }
        }

        override fun v(t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.v(t)
                i++
            }
        }

        override fun d(message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.d(message, *args)
                i++
            }
        }

        override fun d(t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.d(t, message, *args)
                i++
            }
        }

        override fun d(t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.d(t)
                i++
            }
        }

        override fun i(message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.i(message, *args)
                i++
            }
        }

        override fun i(t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.i(t, message, *args)
                i++
            }
        }

        override fun i(t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.i(t)
                i++
            }
        }

        override fun w(message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.w(message, *args)
                i++
            }
        }

        override fun w(t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.w(t, message, *args)
                i++
            }
        }

        override fun w(t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.w(t)
                i++
            }
        }

        override fun e(message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.e(message, *args)
                i++
            }
        }

        override fun e(t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.e(t, message, *args)
                i++
            }
        }

        override fun e(t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.e(t)
                i++
            }
        }

        override fun wtf(message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.wtf(message, *args)
                i++
            }
        }

        override fun wtf(t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.wtf(t, message, *args)
                i++
            }
        }

        override fun wtf(t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.wtf(t)
                i++
            }
        }

        override fun log(priority: Int, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.log(priority, message, *args)
                i++
            }
        }

        override fun log(priority: Int, t: Throwable, message: Any, vararg args: Any) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.log(priority, t, message, *args)
                i++
            }
        }

        override fun log(priority: Int, t: Throwable) {
            val forest = forestAsArray

            var i = 0
            val count = forest.size
            while (i < count) {
                forest[i]?.log(priority, t)
                i++
            }
        }

        override fun log(priority: Int, tag: String?, message: Any, t: Throwable?) {
            throw AssertionError("Missing override for log method.")
        }
    }

    /**
     * Log a verbose message workWith optional format args.
     */
    fun v(message: Any, vararg args: Any) {
        TREE_OF_SOULS.v(message, *args)
    }

    /**
     * Log a verbose exception and a message workWith optional format args.
     */
    fun v(t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.v(t, message, *args)
    }

    /**
     * Log a verbose exception.
     */
    fun v(t: Throwable) {
        TREE_OF_SOULS.v(t)
    }

    /**
     * Log a debug message workWith optional format args.
     */
    fun d(
            message: Any,
            vararg args: Any
    ) {
        TREE_OF_SOULS.d(message, *args)
    }

    /**
     * Log a debug exception and a message workWith optional format args.
     */
    fun d(t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.d(t, message, *args)
    }

    /**
     * Log a debug exception.
     */
    fun d(t: Throwable) {
        TREE_OF_SOULS.d(t)
    }

    /**
     * Log an info message workWith optional format args.
     */
    fun i(message: Any, vararg args: Any) {
        TREE_OF_SOULS.i(message, *args)
    }

    /**
     * Log an info exception and a message workWith optional format args.
     */
    fun i(t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.i(t, message, *args)
    }

    /**
     * Log an info exception.
     */
    fun i(t: Throwable) {
        TREE_OF_SOULS.i(t)
    }

    /**
     * Log a warning message workWith optional format args.
     */
    fun w(message: Any, vararg args: Any) {
        TREE_OF_SOULS.w(message, *args)
    }

    /**
     * Log a warning exception and a message workWith optional format args.
     */
    fun w(t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.w(t, message, *args)
    }

    /**
     * Log a warning exception.
     */
    fun w(t: Throwable) {
        TREE_OF_SOULS.w(t)
    }

    /**
     * Log an error message workWith optional format args.
     */
    fun e(message: Any, vararg args: Any) {
        TREE_OF_SOULS.e(message, *args)
    }

    /**
     * Log an error exception and a message workWith optional format args.
     */
    fun e(t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.e(t, message, *args)
    }

    /**
     * Log an error exception.
     */
    fun e(t: Throwable) {
        TREE_OF_SOULS.e(t)
    }

    /**
     * Log an error exception.
     */
    fun e(e: Exception) {
        if (e.cause == null) {
            TREE_OF_SOULS.e(e.message ?: "")
        } else {
            TREE_OF_SOULS.e(e.cause!!)
        }
    }

    /**
     * Log an assert message workWith optional format args.
     */
    fun wtf(message: Any, vararg args: Any) {
        TREE_OF_SOULS.wtf(message, *args)
    }

    /**
     * Log an assert exception and a message workWith optional format args.
     */
    fun wtf(t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.wtf(t, message, *args)
    }

    /**
     * Log an assert exception.
     */
    fun wtf(t: Throwable) {
        TREE_OF_SOULS.wtf(t)
    }

    /**
     * Log at `priority` a message workWith optional format args.
     */
    fun log(priority: Int, message: Any, vararg args: Any) {
        TREE_OF_SOULS.log(priority, message, *args)
    }

    /**
     * Log at `priority` an exception and a message workWith optional format args.
     */
    fun log(priority: Int, t: Throwable, message: Any, vararg args: Any) {
        TREE_OF_SOULS.log(priority, t, message, *args)
    }

    /**
     * Log at `priority` an exception.
     */
    fun log(priority: Int, t: Throwable) {
        TREE_OF_SOULS.log(priority, t)
    }

    /**
     * A view into Timber's planted trees as a tree itself. This can be used for injecting a logger
     * instance rather than using static methods or to facilitate testing.
     */
    fun asTree() = TREE_OF_SOULS

    /**
     * Set a one-time tag for use on the next logging call.
     */
    fun tag(tag: String): Tree {
        val forest = forestAsArray

        var i = 0
        val count = forest.size
        while (i < count) {
            forest[i]?.explicitTag?.set(tag)
            i++
        }
        return TREE_OF_SOULS
    }

    /**
     * Add a new logging tree.
     */
    fun plant(tree: Tree) {
        if (tree === TREE_OF_SOULS) {
            throw IllegalArgumentException("Cannot plant Timber into itself.")
        }
        synchronized(FOREST) {
            FOREST.add(tree)
            forestAsArray = FOREST.toTypedArray()
        }
    }

    /**
     * Adds new logging trees.
     */
    fun plant(vararg trees: Tree) {

        trees
                .filter { it === TREE_OF_SOULS }
                .forEach { throw IllegalArgumentException("Cannot plant Timber into itself.") }
        synchronized(FOREST) {
            Collections.addAll(FOREST, *trees)
            forestAsArray = FOREST.toTypedArray()
        }
    }


    /**
     * Remove a planted tree.
     */
    fun uproot(tree: Tree) {

        synchronized(FOREST) {
            if (!FOREST.remove(tree)) {
                throw IllegalArgumentException("Cannot uproot tree which is not planted: " + tree)
            }
            forestAsArray = FOREST.toTypedArray()
        }
    }

    /**
     * Remove all planted trees.
     */
    fun uprootAll() {
        synchronized(FOREST) {
            FOREST.clear()
            forestAsArray = TREE_ARRAY_EMPTY
        }
    }

    /**
     * Return a copy of all planted [trees][Tree].
     */
    fun forest(): List<Tree> {
        synchronized(FOREST) {
            return unmodifiableList(ArrayList(FOREST))
        }
    }

    fun treeCount(): Int {
        synchronized(FOREST) {
            return FOREST.size
        }
    }
}

@Suppress("unused")
fun Any?.d() = Timber.TREE_OF_SOULS.d(this ?: "null")

@Suppress("unused")
fun Any?.e() = Timber.TREE_OF_SOULS.e(this ?: "null")

@Suppress("unused")
fun Any?.i() = Timber.TREE_OF_SOULS.i(this ?: "null")

@Suppress("unused")
fun Any?.wtf() = Timber.TREE_OF_SOULS.wtf(this ?: "null")

@Suppress("unused")
fun Any?.w() = Timber.TREE_OF_SOULS.w(this ?: "null")
