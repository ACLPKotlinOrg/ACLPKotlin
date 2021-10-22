package com.nlp.aclpkotlin

import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testParseConditionGreat() {
        val arrays = ["1", "great", "3"];
        String val = ACLPMethods.parseCondition(arrays);
        assertEquals("1>3", val);
    }
}
