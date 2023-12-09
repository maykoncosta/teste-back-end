package com.teste.medidorDeSenha.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CredentialServiceTest {

    private CredentialService service = new CredentialService();


    @Test
    void testCountOccurrencesUpperCase() {
        String input = "AbCdEfG";
        int expectedCount = 4; // A, C, E, G
        int expectedWeight = 2; // Peso definido como 2 no método countOccurrences

        int actualResult = service.countOccurrencesLetters(input, "[A-Z]", expectedWeight);

        assertEquals((input.length() - expectedCount) * expectedWeight, actualResult);
    }

    @Test
    void testCountOccurrencesLowerCase() {
        String input = "test123";
        int expectedCount = 4; // b, d, f, G
        int expectedWeight = 2; // Peso definido como 2 no método countOccurrences

        int actualResult = service.countOccurrencesLetters(input, "[a-z]", expectedWeight);

        assertEquals((input.length() - expectedCount) * expectedWeight, actualResult);
    }

    @Test
    void testCountOccurrencesNumbers() {
        String input = "a1b2c3";
        int expectedCount = 3; // 1, 2, 3
        int expectedWeight = 4; // Peso definido como 4 no método countOccurrences

        int actualResult = service.countOccurrences(input, "[0-9]", expectedWeight);

        assertEquals(expectedCount * expectedWeight, actualResult);
    }

    @Test
    void testCountOccurrencesSymbols() {
        String input = "!@#$abc";
        int expectedCount = 4; // !, @, #, $
        int expectedWeight = 6; // Peso definido como 6 no método countOccurrences

        int actualResult = service.countOccurrences(input, "[^a-zA-Z0-9]", expectedWeight);

        assertEquals(expectedCount * expectedWeight, actualResult);
    }

    @Test
    void testCountOccurrencesMiddle() {
        String input = "ab122cd";
        int expectedCount = 3; // !, @, #, $
        int expectedWeight = 2; // Peso definido como 6 no método countOccurrences

        int actualResult = service.countOccurrences(input, "[0-9[^a-zA-Z0-9]]", expectedWeight);

        assertEquals(expectedCount * expectedWeight, actualResult);
    }

    @Test
    void testContainsUpperCase() {
        
        assertTrue(service.containsUpperCase("AbCdEf"));
        assertFalse(service.containsUpperCase("abcdef"));
    }

    @Test
    void testContainsLowerCase() {
        
        assertTrue(service.containsLowerCase("AbCdEf"));
        assertFalse(service.containsLowerCase("ABCDEF"));
    }

    @Test
    void testContainsNumber() {
        
        assertTrue(service.containsNumber("a1b2c3"));
        assertFalse(service.containsNumber("abc"));
    }

    @Test
    void testContainsSymbol() {
        
        assertTrue(service.containsSymbol("!@#abc"));
        assertFalse(service.containsSymbol("abc123"));
    }

    @Test
    void testCountSequentialOccurrences() {
        String input = "#@!maykon78";
        int expectedCount = 1; // !@#
        int expectedWeight = 3;

        
        int actualResult = service.countSequentialOccurrences(input, 3);

        assertEquals(expectedCount * expectedWeight, actualResult);
    }

    @Test
    void testCountConsectiveOccurrences() {
        String input = "abc123def";
        int expectedCount = 6; // abc, 123
        int expectedWeight = 2;


        int actualResult = service.countSequentialOccurrences(input, 2);

        assertEquals(expectedCount * expectedWeight, actualResult);
    }

    @Test
    void testIsSequential() {
        
        assertTrue(service.isSequential("abc"));
        assertFalse(service.isSequential("163"));
        assertTrue(service.isSequential("aBc"));
    }

    @Test
    void testCalculate(){
        String pass = "M@ykon";
        String pass2 = "M@ykonM@ykon";
        String pass3 = "#@!maykon78";

        //De acordo com valores do site
        int expectedResult = 40;
        int expectedResult2 = 79;
        int expectedResult3 = 79;

        int actualResult = service.calculatePasswordStrength(pass);
        int actualResult2 = service.calculatePasswordStrength(pass2);
        int actualResult3 = service.calculatePasswordStrength(pass3);

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult2, actualResult2);
        assertEquals(expectedResult3, actualResult3);
    }

    @Test
    void testGeneratedPassword(){
        String actualPass = service.generatePassword();
        int actualScore = service.calculatePasswordStrength(actualPass);
        int expectedScore = 70;

        assertTrue(actualScore > expectedScore);
        Assertions.assertNotNull(actualPass);
    }

}
