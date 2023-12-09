package com.teste.medidorDeSenha.service;

import com.teste.medidorDeSenha.domain.Credential;
import com.teste.medidorDeSenha.repository.CredentialHistoryRepository;
import com.teste.medidorDeSenha.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CredentialService {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    private static final String DIGITS = "0123456789";

    private static final String SYMBOLS = "!@#$%^&*()";

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SYMBOLS;

    private static final int RUIM = 20;

    private static final int MEDIA = 45;

    private static final int BOM = 75;

    private static final int FORTE = 100;

    @Autowired
    private CredentialRepository repository;

    @Autowired
    private CredentialHistoryRepository credentialHistoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Credential saveCredential(String password){

        int score = calculatePasswordStrength(password);
        int level = defineStrengthLevel(score);
        String passEncoder = passwordEncoder.encode(password);

        return Credential
                .builder()
                .password(passEncoder)
                .score(score)
                .passwordStrength(level)
                .build();
    }

    protected int defineStrengthLevel(int score){
        if(score <= RUIM){
            return 1;
        }else if(score <= MEDIA){
            return 2;
        }else if(score <= BOM){
            return 3;
        }else{
            return 4;
        }
    }

    public int calculatePasswordStrength(String password) {
            int score = 0;
            int len = password.length();

            // Número de caracteres +(n*4)
            score += len * 4;

            // Letras maiúsculas, minúsculas, números e símbolos
            score += countOccurrencesLetters(password, "[A-Z]", 2); // Letras maiúsculas +((len-n)*2)
            score += countOccurrencesLetters(password, "[a-z]", 2); // Letras minúsculas +((len-n)*2)
            score += countOccurrences(password, "[0-9]", 4); // Números +(n*4)
            score += countOccurrences(password, "[^a-zA-Z0-9]", 6); // Símbolos +(n*6)

            // Números ou símbolos no meio +(n*2)
            String middle = password.substring(1, len - 1);
            score += countOccurrences(middle, "[0-9[^a-zA-Z0-9]]", 2);

            // Requisitos mínimos +(n*2)
            score += getMinRequirements(password, len);

            // Apenas letras (-n)
            if (isLetters(password)) score -= len;

            // Apenas números (-n)
            if (isNumbers(password)) score -= len;
            // Letras consecutivas, números consecutivos, símbolos consecutivos (-n*2)
            score -= countConsectiveOccurrences(password, 2);


            // Letras sequenciais (3+) e Números sequenciais (3+) e Símbolos sequenciais (3+) (-n*3)
            score -= countSequentialOccurrences(password, 3);

            score -= countRepeatedCharacters(password);

            // Pontuação final
            if (score < 0) score = 0;
            if (score > 100) score = 100;

            return score;
        }

    private int getMinRequirements(String password, int len) {
        int minRequirements = 2;
        if (len >= 8 ) {
            if (containsUpperCase(password)) minRequirements +=2;
            if (containsLowerCase(password)) minRequirements +=2;
            if (containsNumber(password)) minRequirements +=2;
            if (containsSymbol(password) )minRequirements +=2;

        }
        return (minRequirements >= 8) ? minRequirements : 0;
    }

    protected boolean isNumbers(String password) {
        return password.matches("[0-9]+");
    }

    protected boolean isLetters(String password) {
        return password.matches("[a-zA-Z]+");
    }

    protected boolean isOnlyLettersUpperCase(String password) {
        return password.matches("[A-Z]+");
    }

    protected boolean isOnlyLettersLowCase(String password) {
        return password.matches("[a-z]+");
    }

    protected int countOccurrences(String input, String pattern, int weight) {
            int count = 0;
            Matcher matcher = Pattern.compile(pattern).matcher(input);
            while (matcher.find()) count++;
            return count * weight;
        }

        protected int countOccurrencesLetters(String input, String pattern, int weight) {
            int count = 0;
            Matcher matcher = Pattern.compile(pattern).matcher(input);
            while (matcher.find()) count++;
            return count == 0 ? 0 : (input.length() -count)* weight;
        }

        protected boolean containsUpperCase(String input) {
            return Pattern.compile("[A-Z]").matcher(input).find();
        }

        protected boolean containsLowerCase(String input) {
            return Pattern.compile("[a-z]").matcher(input).find();
        }

        protected  boolean containsNumber(String input) {
            return Pattern.compile("[0-9]").matcher(input).find();
        }

        protected  boolean containsSymbol(String input) {
            return Pattern.compile("[^a-zA-Z0-9]").matcher(input).find();
        }

        protected int countConsectiveOccurrences(String input, int length) {
            int count = 0;
            for (int i = 0; i < input.length() - (length - 1); i++) {
                if (isConsective(input.substring(i, i + length))) count += length;
            }
            return count;
        }

        protected int countSequentialOccurrences(String input, int length) {
            int count = 0;
            for (int i = 0; i < input.length() - (length - 1); i++) {
                if (isSequential(input.substring(i, i + length))) count += length;
            }
            return count;
        }

    protected boolean isSequential(String sequence) {
        String reversed = new StringBuilder(sequence).reverse().toString().toLowerCase();
        String sequenceLowCase = sequence.toLowerCase();
        String symbols = ")!@#$%^&*()";

            for (int i = 0; i < sequence.length() - 1; i++) {
                if ((sequenceLowCase.charAt(i) + 1 != sequenceLowCase.charAt(i + 1)
                        && symbols.indexOf(sequenceLowCase.charAt(i)) + 1 != symbols.indexOf(sequenceLowCase.charAt(i + 1))) &&
                        (reversed.charAt(i) + 1 != reversed.charAt(i + 1) && symbols.indexOf(reversed.charAt(i)) + 1 != symbols.indexOf(reversed.charAt(i + 1)))) {
                    return false;
                }
            }
        return true;
    }


        protected boolean isConsective(String sequence) {
            return isNumbers(sequence)
                    || isOnlyLettersLowCase(sequence)
                    || isOnlyLettersUpperCase(sequence);
        }


    protected int countRepeatedCharacters(String password) {
        int nRepInc = 0, nRepChar = 0, nUnqChar;
        int len = password.length();
        for (int a = 0; a < len; a++) {
            boolean bCharExists = false;
            for (int b = 0; b < len; b++) {
                if (password.charAt(a) == password.charAt(b) && a != b) {
                    bCharExists = true;
                    int diff = (b - a);
                    nRepInc += Math.abs(len / diff);
                }
            }
            if (bCharExists) {
                nRepChar++;
                nUnqChar = len - nRepChar;
                nRepInc = (nUnqChar != 0) ? (int) Math.ceil((double) nRepInc / nUnqChar) : (int) Math.ceil(nRepInc);
            }
        }
        return nRepInc;
    }

    public String generatePassword() {
        int length = 8;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        // Garante pelo menos um caractere de cada tipo
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));

        // Preenche o restante da senha com caracteres aleatórios
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        return password.toString();
    }

}
