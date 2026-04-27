package com.passwordgenerator.app

object StrengthChecker {

    /**
     * Password strength score and level.
     */
    data class StrengthResult(
        val score: Int,
        val maxScore: Int,
        val level: StrengthLevel,
        val details: String
    )

    enum class StrengthLevel { WEAK, MEDIUM, STRONG }

    /**
     * Check password strength based on length and character variety.
     * Scoring (0-10):
     * - Length: up to 3 pts (< 6 = 0, 6-7 = 1, 8-11 = 2, 12+ = 3)
     * - Uppercase: 1 pt if present
     * - Lowercase: 1 pt if present
     * - Numbers: 2 pts (1 = 1, 2+ = 2)
     * - Special chars: 3 pts (1 = 1, 2 = 2, 3+ = 3)
     */
    fun check(password: String): StrengthResult {
        val length = password.length

        // Length scoring
        val lengthScore = when {
            length >= 12 -> 3
            length >= 8 -> 2
            length >= 6 -> 1
            else -> 0
        }

        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val numCount = password.count { it.isDigit() }
        val specialCount = password.count { !it.isLetterOrDigit() }

        val uppercaseScore = if (hasUppercase) 1 else 0
        val lowercaseScore = if (hasLowercase) 1 else 0
        val numberScore = when {
            numCount >= 2 -> 2
            numCount >= 1 -> 1
            else -> 0
        }
        val specialScore = when {
            specialCount >= 3 -> 3
            specialCount >= 2 -> 2
            specialCount >= 1 -> 1
            else -> 0
        }

        val totalScore = lengthScore + uppercaseScore + lowercaseScore + numberScore + specialScore
        val maxScore = 10

        val level = when {
            totalScore >= 7 -> StrengthLevel.STRONG
            totalScore >= 4 -> StrengthLevel.MEDIUM
            else -> StrengthLevel.WEAK
        }

        val details = buildString {
            append("Length: $length chars\n")
            append("Uppercase: ${if (hasUppercase) "✓" else "✗"} | ")
            append("Lowercase: ${if (hasLowercase) "✓" else "✗"}\n")
            append("Numbers: $numCount | ")
            append("Special: $specialCount\n")
            append("Score: $totalScore/$maxScore")
        }

        return StrengthResult(totalScore, maxScore, level, details)
    }

    /**
     * Check passphrase strength - similar to the web version.
     */
    fun checkPassphrase(passphrase: String): StrengthResult {
        val wordCount = passphrase.split("-").size
        val length = passphrase.length
        val hasNumber = passphrase.any { it.isDigit() }
        val hasUppercase = passphrase.any { it.isUpperCase() }
        val hasSpecial = passphrase.any { !it.isLetterOrDigit() && it != '-' }

        var score = 0
        if (wordCount >= 4) score++
        if (wordCount >= 6) score++
        if (hasNumber) score++
        if (hasUppercase) score++
        if (hasSpecial) score++

        val level = when {
            score >= 4 -> StrengthLevel.STRONG
            score >= 2 -> StrengthLevel.MEDIUM
            else -> StrengthLevel.WEAK
        }

        val details = buildString {
            append("Words: $wordCount | Length: $length\n")
            append("Numbers: ${if (hasNumber) "✓" else "✗"} | ")
            append("Uppercase: ${if (hasUppercase) "✓" else "✗"}\n")
            append("Score: $score/5")
        }

        return StrengthResult(score, 5, level, details)
    }
}
