package com.example.mpesastkpush;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mpesastkpush.settings.SandBox;

import org.json.JSONException;

import java.io.IOException;

import static com.example.mpesastkpush.result.Result.ResponseCode;
import static com.example.mpesastkpush.stkpush.StkPush.stkpush;
import static com.example.mpesastkpush.utils.GenerateValues.generateDate;
import static com.example.mpesastkpush.utils.GenerateValues.generatePassword;

public class MainActivity extends AppCompatActivity {
    EditText editTextPhone, editTextAmount;
    Button buttonRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonRequest = findViewById(R.id.buttonRequest);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);


        }
        //settings;
        SandBox.setAccess_token_url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials");
        SandBox.setBusinessShortCode("174379");
        SandBox.setConsumerKey("");//enter consumer key
        SandBox.setConsumerSecret("");//enter consumer secret
        SandBox.setPassKey("");//enter passkey
        SandBox.setStk_push_url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest");
        SandBox.setC2bSimulation_url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/simulate");
//254708374149
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                request(editTextAmount.getText().toString(), editTextPhone.getText().toString());
            }
        });


    }

    private boolean checkPhoneCode(String phone) {
        return phone.startsWith("254");
    }

    private boolean checkPhone(String phone) {
        return phone.length() == 12;
    }

    private boolean checkAmount(String amount) {
        return amount.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void request(String amount, String phone) {
        if (!checkPhoneCode(phone)) {
            editTextPhone.requestFocus();
            editTextPhone.setError("Wrong format");
        } else if (!checkPhone(phone)) {
            editTextPhone.requestFocus();
            editTextPhone.setError("Wrong format");
        } else if (checkAmount(amount)) {
            editTextAmount.requestFocus();
            editTextAmount.setError("Cannot be empty");
        } else {
            try {
                stkpush(SandBox.businessShortCode, generatePassword(), generateDate(), "CustomerPayBillOnline", "10", "254708374149", "254708374149", "174379", "http://192.168.43.17/www.android.com/mpesa/callback_url.php", "take", "Please work");
                Toast.makeText(MainActivity.this, "ResponseCode:" + ResponseCode, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
