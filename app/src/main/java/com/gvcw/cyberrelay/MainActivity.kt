package com.gvcw.cyberrelay

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("GVCW_PREFS", Context.MODE_PRIVATE)

        // Main Layout Scrollable Container
        val scrollView = ScrollView(this).apply {
            isFillViewport = true
            setBackgroundColor(Color.parseColor("#0F172A")) // Deep Slate Navy Dark Background
        }

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 64, 48, 64)
        }

        // Header Title
        val titleText = TextView(this).apply {
            text = "GVCW OTP Relay"
            textSize = 28f
            setTextColor(Color.WHITE)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val subtitleText = TextView(this).apply {
            text = "Automated Real-Time Forwarder"
            textSize = 14f
            setTextColor(Color.parseColor("#94A3B8"))
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 8, 0, 48)
        }

        // Card View for Numbers Configuration
        val cardBackground = GradientDrawable().apply {
            setColor(Color.parseColor("#1E293B"))
            cornerRadius = 32f
            setStroke(2, Color.parseColor("#334155"))
        }

        val cardLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = cardBackground
            setPadding(48, 48, 48, 48)
        }

        val cardTitle = TextView(this).apply {
            text = "Target Phone Numbers"
            textSize = 18f
            setTextColor(Color.parseColor("#F8FAFC"))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 24)
        }

        // Input 1: Phone Number 1
        val input1 = EditText(this).apply {
            hint = "Enter Target Number 1 (e.g. +92300...)"
            setHintTextColor(Color.parseColor("#64748B"))
            setTextColor(Color.WHITE)
            inputType = InputType.TYPE_CLASS_PHONE
            setText(prefs.getString("NUM_1", ""))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#0F172A"))
                cornerRadius = 16f
                setStroke(2, Color.parseColor("#475569"))
            }
            setPadding(32, 32, 32, 32)
        }

        // Input 2: Phone Number 2
        val input2 = EditText(this).apply {
            hint = "Enter Target Number 2 (e.g. +92300...)"
            setHintTextColor(Color.parseColor("#64748B"))
            setTextColor(Color.WHITE)
            inputType = InputType.TYPE_CLASS_PHONE
            setText(prefs.getString("NUM_2", ""))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#0F172A"))
                cornerRadius = 16f
                setStroke(2, Color.parseColor("#475569"))
            }
            setPadding(32, 32, 32, 32)
        }

        // Save Settings Button
        val saveBtn = Button(this).apply {
            text = "SAVE CONFIGURATION"
            setTextColor(Color.WHITE)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#2563EB")) // Modern Electric Blue
                cornerRadius = 20f
            }
            setPadding(0, 28, 0, 28)
        }

        saveBtn.setOnClickListener {
            val num1 = input1.text.toString().trim()
            val num2 = input2.text.toString().trim()
            prefs.edit().putString("NUM_1", num1).putString("NUM_2", num2).apply()
            Toast.makeText(this, "Configurations Saved Successfully!", Toast.LENGTH_SHORT).show()
        }

        // Add views to card
        cardLayout.addView(cardTitle)
        cardLayout.addView(input1)
        val space1 = Space(this).apply { layoutParams = LinearLayout.LayoutParams(1, 24) }
        cardLayout.addView(space1)
        cardLayout.addView(input2)
        val space2 = Space(this).apply { layoutParams = LinearLayout.LayoutParams(1, 36) }
        cardLayout.addView(space2)
        cardLayout.addView(saveBtn)

        // Status Indicator Card
        val statusBackground = GradientDrawable().apply {
            setColor(Color.parseColor("#065F46")) // Dark Emerald Green
            cornerRadius = 24f
        }

        val statusText = TextView(this).apply {
            text = "● RELAY SERVICE ACTIVE"
            textSize = 14f
            setTextColor(Color.parseColor("#34D399"))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER
            background = statusBackground
            setPadding(32, 24, 32, 24)
        }

        val space3 = Space(this).apply { layoutParams = LinearLayout.LayoutParams(1, 48) }

        // Assemble Main Screen Layout
        mainLayout.addView(titleText)
        mainLayout.addView(subtitleText)
        mainLayout.addView(cardLayout)
        mainLayout.addView(space3)
        mainLayout.addView(statusText)

        scrollView.addView(mainLayout)
        setContentView(scrollView)

        // Request SMS Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS), 101)
        }
    }
}