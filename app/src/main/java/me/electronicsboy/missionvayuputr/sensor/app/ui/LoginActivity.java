package me.electronicsboy.missionvayuputr.sensor.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.ConnectException;
import java.util.regex.Pattern;

import me.electronicsboy.missionvayuputr.sensor.app.R;
import me.electronicsboy.missionvayuputr.sensor.app.callbacks.IAfterSplashScreenLoadCallback;
import me.electronicsboy.missionvayuputr.sensor.app.ui.splash.SplashScreen;
import me.electronicsboy.missionvayuputr.sensor.app.util.ChangeableDataListener;
import me.electronicsboy.missionvayuputr.sensor.app.util.Client;
import me.electronicsboy.missionvayuputr.sensor.app.util.TempStorage;

public class LoginActivity extends AppCompatActivity {
    private static final String REGEX_IP_VALID = "\\b((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}\\b";
    private static final Pattern PATTERN_IP = Pattern.compile(REGEX_IP_VALID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> onLoginClick());
    }

    protected void onLoginClick() {
        EditText etIP = findViewById(R.id.editTextIP);
        EditText etPort = findViewById(R.id.editTextPort);

        String ip = etIP.getText().toString();
        Log.d("LOGIN", ip);

        if(ip.isEmpty()) {
            Toast.makeText(this, "IP cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PATTERN_IP.matcher(ip).matches()) {
            Toast.makeText(this, "IP is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        String sPort = etPort.getText().toString();
        Log.d("LOGIN", sPort);

        if(sPort.isEmpty()) {
            Toast.makeText(this, "Port cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LOGIN", sPort);
        int port = Integer.parseInt(sPort);
        Log.d("LOGIN", String.valueOf(port));
        if(port <= 0 || port >= 65535) {
            Toast.makeText(this, "Port must be valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        TempStorage.addOrSet("SPLASH_SCREEN_CALLBACK", (IAfterSplashScreenLoadCallback) (SplashScreen splashScreen) -> {
            Client c = null;
            ChangeableDataListener changeableDataListener = new ChangeableDataListener();

            c = new Client(changeableDataListener, ip, port);
            while(!c.isConnected()) {
                if(c.isInError()) {
                    Toast.makeText(this, "Error connecting to " + ip + ":" + port + " (" + c.getError().getMessage() + ")", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
            }

            TempStorage.addOrSet("NETWORK_CLIENT_INST", c);
            TempStorage.addOrSet("NETWORK_CHANGEABLE_DATA_LISTENER_INST", changeableDataListener);
            startActivity(new Intent(this, MainActivity.class));
        });

        startActivity(new Intent(this, SplashScreen.class));
        finish();
    }
}