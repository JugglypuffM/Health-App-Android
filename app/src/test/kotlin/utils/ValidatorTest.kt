package utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

import org.junit.Assert.*
import utils.Validator.*

class ValidatorTest {
    private val validator = Validator()

    @Test
    fun `validateName should return success for valid name`() {
        val result = validator.validateName("John Doe")
        assertTrue(result.isSuccess)
        assertEquals("John Doe", result.getOrNull())
    }

    @Test
    fun `validateName should return failure for empty name`() {
        val result = validator.validateName("")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidNameException)
        assertEquals("User name is empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `validateLogin should return success for valid login`() {
        val result = validator.validateLogin("validLogin")
        assertTrue(result.isSuccess)
        assertEquals("validLogin", result.getOrNull())
    }

    @Test
    fun `validateLogin should return failure for empty login`() {
        val result = validator.validateLogin("")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidLoginException)
        assertEquals("Login is empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `validatePassword should return success for valid password`() {
        val result = validator.validatePassword("secure123")
        assertTrue(result.isSuccess)
        assertEquals("secure123", result.getOrNull())
    }

    @Test
    fun `validatePassword should return failure for short password`() {
        val result = validator.validatePassword("123")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidPasswordException)
        assertEquals("The password must be at least 6 characters long", result.exceptionOrNull()?.message)
    }

    @Test
    fun `validateConfirmPassword should return success if passwords match`() {
        val result = validator.validateConfirmPassword("password123", "password123")
        assertTrue(result.isSuccess)
        assertEquals("password123", result.getOrNull())
    }

    @Test
    fun `validateConfirmPassword should return failure if passwords do not match`() {
        val result = validator.validateConfirmPassword("password123", "differentPassword")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NotEqualPasswordException)
        assertEquals("Passwords do not match", result.exceptionOrNull()?.message)
    }

    @Test
    fun `validateAge should return success for valid age`() {
        val result = validator.validateAge(25)
        assertTrue(result.isSuccess)
        assertEquals(25, result.getOrNull())
    }

    @Test
    fun `validateAge should return failure for invalid age`() {
        val result = validator.validateAge(150)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidAgeException)
        assertEquals("Age must be between 5 and 100", result.exceptionOrNull()?.message)
    }

    @Test
    fun `validateWeight should return success for valid weight`() {
        val result = validator.validateWeight(70)
        assertTrue(result.isSuccess)
        assertEquals(70, result.getOrNull())
    }

    @Test
    fun `validateWeight should return failure for invalid weight`() {
        val result = validator.validateWeight(2000)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidWeightException)
        assertEquals("Weight must be between 10 and 1000", result.exceptionOrNull()?.message)
    }
}
