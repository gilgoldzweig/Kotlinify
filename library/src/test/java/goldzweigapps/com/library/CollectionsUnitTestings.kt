package goldzweigapps.com.library

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import goldzweigapps.com.library.collections.asIterableIndexed
import goldzweigapps.com.library.views.iterator
import goldzweigapps.com.library.views.plusAssign
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by gilgoldzweig on 08/09/2017.
 */
class CollectionsUnitTestings {
    @Test
    @Throws(Exception::class)
    fun intIterable_isCounting(ctx: Context) {
        val group = LinearLayout(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += View(ctx)
        group += Button(ctx)
        group.iterator().forEach { view -> }
        "GilIsAwesome".asIterable()
        val expectedResult = listOf(0 to 0, 1 to 3, 2 to 6, 3 to 9, 4 to 12, 5 to 15)
        val list = asIterableIndexed(
                { it <= 5 },
                { it * 3 },
                { it.inc() }
        )
                .mapIndexed { index, i -> index to i }
        assertEquals(expectedResult, list)
    }
}