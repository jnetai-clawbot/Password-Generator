package com.passwordgenerator.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.passwordgenerator.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Password generator views
    private lateinit var passwordLengthInput: EditText
    private lateinit var passwordOutput: EditText
    private lateinit var generatePasswordBtn: Button
    private lateinit var copyPasswordBtn: Button
    private lateinit var passwordStrength: TextView
    private lateinit var passwordStatus: TextView

    // Passphrase generator views
    private lateinit var numWordsInput: EditText
    private lateinit var passphraseOutput: EditText
    private lateinit var generatePassphraseBtn: Button
    private lateinit var addNumbersBtn: Button
    private lateinit var makeUppercaseBtn: Button
    private lateinit var addSymbolsBtn: Button
    private lateinit var copyPassphraseBtn: Button
    private lateinit var passphraseStrength: TextView
    private lateinit var passphraseStatus: TextView

    // Strength checker views
    private lateinit var checkerInput: EditText
    private lateinit var checkStrengthBtn: Button
    private lateinit var strengthResultLayout: View
    private lateinit var checkerStrength: TextView
    private lateinit var checkerDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setupPasswordGenerator()
        setupPassphraseGenerator()
        setupStrengthChecker()
    }

    private fun initViews() {
        // Password
        passwordLengthInput = binding.passwordLengthInput
        passwordOutput = binding.passwordOutput
        generatePasswordBtn = binding.generatePasswordBtn
        copyPasswordBtn = binding.copyPasswordBtn
        passwordStrength = binding.passwordStrength
        passwordStatus = binding.passwordStatus

        // Passphrase
        numWordsInput = binding.numWordsInput
        passphraseOutput = binding.passphraseOutput
        generatePassphraseBtn = binding.generatePassphraseBtn
        addNumbersBtn = binding.addNumbersBtn
        makeUppercaseBtn = binding.makeUppercaseBtn
        addSymbolsBtn = binding.addSymbolsBtn
        copyPassphraseBtn = binding.copyPassphraseBtn
        passphraseStrength = binding.passphraseStrength
        passphraseStatus = binding.passphraseStatus

        // Checker
        checkerInput = binding.checkerInput
        checkStrengthBtn = binding.checkStrengthBtn
        strengthResultLayout = binding.strengthResultLayout
        checkerStrength = binding.checkerStrength
        checkerDetails = binding.checkerDetails
    }

    // ==================== PASSWORD GENERATOR ====================

    private fun setupPasswordGenerator() {
        generatePasswordBtn.setOnClickListener {
            val length = passwordLengthInput.text.toString().toIntOrNull() ?: 12
            val password = PasswordGenerator.generate(length)
            passwordOutput.setText(password)
            showPasswordStrength(password)
            showStatus(passwordStatus, "🔑 Password generated!", "success")
        }

        copyPasswordBtn.setOnClickListener {
            val text = passwordOutput.text.toString()
            if (text.isEmpty()) {
                showStatus(passwordStatus, "Generate a password first!", "error")
                return@setOnClickListener
            }
            copyToClipboard(text)
            showStatus(passwordStatus, "✓ Password copied to clipboard!", "success")
        }
    }

    private fun showPasswordStrength(password: String) {
        val result = StrengthChecker.check(password)
        passwordStrength.visibility = View.VISIBLE

        when (result.level) {
            StrengthChecker.StrengthLevel.STRONG -> {
                passwordStrength.text = "🛡️ Strong"
                passwordStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_strong)
                passwordStrength.setTextColor(ContextCompat.getColor(this, R.color.success))
            }
            StrengthChecker.StrengthLevel.MEDIUM -> {
                passwordStrength.text = "⚡ Medium"
                passwordStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_medium)
                passwordStrength.setTextColor(ContextCompat.getColor(this, R.color.warning))
            }
            StrengthChecker.StrengthLevel.WEAK -> {
                passwordStrength.text = "⚠️ Weak"
                passwordStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_weak)
                passwordStrength.setTextColor(ContextCompat.getColor(this, R.color.danger))
            }
        }
    }

    // ==================== PASSPHRASE GENERATOR ====================

    private fun setupPassphraseGenerator() {
        generatePassphraseBtn.setOnClickListener {
            val numWords = numWordsInput.text.toString().toIntOrNull() ?: 4
            val passphrase = PassphraseGenerator.generate(numWords)
            passphraseOutput.setText(passphrase)
            showPassphraseStrength(passphrase)
            showStatus(passphraseStatus, "🔑 Passphrase generated!", "success")
        }

        addNumbersBtn.setOnClickListener {
            val current = passphraseOutput.text.toString()
            if (current.isEmpty()) {
                showStatus(passphraseStatus, "Generate a passphrase first!", "error")
                return@setOnClickListener
            }
            val modified = PassphraseGenerator.addNumbers(current)
            passphraseOutput.setText(modified)
            showPassphraseStrength(modified)
            showStatus(passphraseStatus, "Numbers added! 🎲", "success")
        }

        makeUppercaseBtn.setOnClickListener {
            val current = passphraseOutput.text.toString()
            if (current.isEmpty()) {
                showStatus(passphraseStatus, "Generate a passphrase first!", "error")
                return@setOnClickListener
            }
            val modified = PassphraseGenerator.makeUppercase(current)
            passphraseOutput.setText(modified)
            showPassphraseStrength(modified)
            showStatus(passphraseStatus, "Letters uppercased! 🔠", "success")
        }

        addSymbolsBtn.setOnClickListener {
            val current = passphraseOutput.text.toString()
            if (current.isEmpty()) {
                showStatus(passphraseStatus, "Generate a passphrase first!", "error")
                return@setOnClickListener
            }
            val modified = PassphraseGenerator.addSpecialChars(current)
            passphraseOutput.setText(modified)
            showPassphraseStrength(modified)
            showStatus(passphraseStatus, "Special chars added! ✨", "success")
        }

        copyPassphraseBtn.setOnClickListener {
            val text = passphraseOutput.text.toString()
            if (text.isEmpty()) {
                showStatus(passphraseStatus, "Generate a passphrase first!", "error")
                return@setOnClickListener
            }
            copyToClipboard(text)
            showStatus(passphraseStatus, "✓ Passphrase copied to clipboard!", "success")
        }
    }

    private fun showPassphraseStrength(passphrase: String) {
        val result = StrengthChecker.checkPassphrase(passphrase)
        passphraseStrength.visibility = View.VISIBLE

        when (result.level) {
            StrengthChecker.StrengthLevel.STRONG -> {
                passphraseStrength.text = "🛡️ Strong"
                passphraseStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_strong)
                passphraseStrength.setTextColor(ContextCompat.getColor(this, R.color.success))
            }
            StrengthChecker.StrengthLevel.MEDIUM -> {
                passphraseStrength.text = "⚡ Medium"
                passphraseStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_medium)
                passphraseStrength.setTextColor(ContextCompat.getColor(this, R.color.warning))
            }
            StrengthChecker.StrengthLevel.WEAK -> {
                passphraseStrength.text = "⚠️ Weak"
                passphraseStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_weak)
                passphraseStrength.setTextColor(ContextCompat.getColor(this, R.color.danger))
            }
        }
    }

    // ==================== STRENGTH CHECKER ====================

    private fun setupStrengthChecker() {
        // Check on button click
        checkStrengthBtn.setOnClickListener {
            val password = checkerInput.text.toString()
            if (password.isEmpty()) {
                strengthResultLayout.visibility = View.GONE
                return@setOnClickListener
            }
            showCheckerResult(password)
        }

        // Real-time checking as user types
        checkerInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s?.toString() ?: ""
                if (password.isNotEmpty()) {
                    showCheckerResult(password)
                } else {
                    strengthResultLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun showCheckerResult(password: String) {
        val result = StrengthChecker.check(password)
        strengthResultLayout.visibility = View.VISIBLE
        checkerDetails.text = result.details

        when (result.level) {
            StrengthChecker.StrengthLevel.STRONG -> {
                checkerStrength.text = "🛡️ Strong"
                checkerStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_strong)
                checkerStrength.setTextColor(ContextCompat.getColor(this, R.color.success))
            }
            StrengthChecker.StrengthLevel.MEDIUM -> {
                checkerStrength.text = "⚡ Medium"
                checkerStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_medium)
                checkerStrength.setTextColor(ContextCompat.getColor(this, R.color.warning))
            }
            StrengthChecker.StrengthLevel.WEAK -> {
                checkerStrength.text = "⚠️ Weak"
                checkerStrength.background = ContextCompat.getDrawable(this, R.drawable.bg_strength_weak)
                checkerStrength.setTextColor(ContextCompat.getColor(this, R.color.danger))
            }
        }
    }

    // ==================== UTILITIES ====================

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("password", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun showStatus(textView: TextView, message: String, type: String) {
        textView.text = message
        textView.visibility = View.VISIBLE

        val bgRes = when (type) {
            "success" -> R.drawable.bg_status_success
            "error" -> R.drawable.bg_status_error
            else -> R.drawable.bg_status_info
        }
        val colorRes = when (type) {
            "success" -> R.color.success
            "error" -> R.color.danger
            else -> R.color.accent_primary
        }

        textView.background = ContextCompat.getDrawable(this, bgRes)
        textView.setTextColor(ContextCompat.getColor(this, colorRes))

        // Auto-hide after 3 seconds
        textView.postDelayed({
            textView.visibility = View.GONE
        }, 3000)
    }
}
