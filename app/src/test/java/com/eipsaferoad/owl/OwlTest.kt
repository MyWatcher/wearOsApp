package com.eipsaferoad.owl

import org.junit.Assert.assertEquals
import org.junit.Test

class OwlTest {

    @Test
    fun testAddition() {
        // Arrange
        val number1 = 1
        val number2 = 1

        // Act
        val sum = number1 + number2

        // Assert
        assertEquals(2, sum)
    }
}
