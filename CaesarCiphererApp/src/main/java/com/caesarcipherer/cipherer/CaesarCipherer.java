package com.caesarcipherer.cipherer;

/**
 * Шифратор Цезаря.
 *
 * @author Жегздринь Ангелина
 * @version 1.1
 */
public class CaesarCipherer {

    /**
     *  Передаваемый алфавит
     */
    private final String alphabet;

    /**
     * Конструктор шифратора Цезаря.
     *
     * @param alphabet Алфавит для шифрования.
     */
    public CaesarCipherer(String alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Шифрует текст с помощью шифра Цезаря.
     *
     * @param plaintext Исходный текст.
     * @param shiftKey  Ключ сдвига.
     * @return Зашифрованный текст.
     */
    public String encrypt(String plaintext, int shiftKey) {
        StringBuilder ciphertext = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            int charPosition = alphabet.indexOf(Character.toUpperCase(c));
            if (charPosition != -1) {
                int keyVal = (shiftKey + charPosition) % alphabet.length();
                char replaceVal = alphabet.charAt(keyVal);
                ciphertext.append(Character.isUpperCase(c) ? replaceVal : Character.toLowerCase(replaceVal));
            } else {
                ciphertext.append(c);
            }
        }
        return ciphertext.toString();
    }

    /**
     * Расшифровывает текст, зашифрованный шифром Цезаря.
     *
     * @param ciphertext Зашифрованный текст.
     * @param shiftKey   Ключ сдвига.
     * @return Расшифрованный текст.
     */
    public String decrypt(String ciphertext, int shiftKey) {
        StringBuilder plaintext = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            int charPosition = alphabet.indexOf(Character.toUpperCase(c));
            if (charPosition != -1) {
                int keyVal = (charPosition - shiftKey) % alphabet.length();
                if (keyVal < 0) {
                    keyVal = alphabet.length() + keyVal;
                }
                char replaceVal = alphabet.charAt(keyVal);
                plaintext.append(Character.isUpperCase(c) ? replaceVal : Character.toLowerCase(replaceVal));
            } else {
                plaintext.append(c);
            }
        }
        return plaintext.toString();
    }
}
