package com.passwordgenerator.app

import java.security.SecureRandom

object PasswordGenerator {

    private val random = SecureRandom()

    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBERS = "0123456789"
    private const val SPECIAL = "!@#$%^&*()_+"

    private const val ALL_CHARS = UPPERCASE + LOWERCASE + NUMBERS + SPECIAL

    /**
     * Generate a random password ensuring at least 1 number and 1 special character.
     */
    fun generate(length: Int): String {
        val clampedLength = length.coerceIn(4, 128)
        val password = StringBuilder(clampedLength)

        // Fill with random chars
        for (i in 0 until clampedLength) {
            password.append(ALL_CHARS[random.nextInt(ALL_CHARS.length)])
        }

        // Ensure at least 1 number
        if (!password.any { it.isDigit() }) {
            val pos = random.nextInt(clampedLength)
            password[pos] = NUMBERS[random.nextInt(NUMBERS.length)]
        }

        // Ensure at least 1 special character
        if (!password.any { it in SPECIAL }) {
            val pos = random.nextInt(clampedLength)
            password[pos] = SPECIAL[random.nextInt(SPECIAL.length)]
        }

        return password.toString()
    }
}
