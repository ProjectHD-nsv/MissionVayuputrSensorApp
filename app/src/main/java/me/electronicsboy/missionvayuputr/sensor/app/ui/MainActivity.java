
package me.electronicsboy.missionvayuputr.sensor.app.ui;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import me.electronicsboy.missionvayuputr.sensor.app.R;
import me.electronicsboy.missionvayuputr.sensor.app.util.ChangeableDataListener;
import me.electronicsboy.missionvayuputr.sensor.app.util.TempStorage;

public class MainActivity extends AppCompatActivity {
    private int calibratedGyroXDiff = 0;
    private int calibratedGyroYDiff = 0;
    private int calibratedGyroZDiff = 0;

    private int calibratedAccelXDiff = 0;
    private int calibratedAccelYDiff = 0;
    private int calibratedAccelZDiff = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvgyroX = findViewById(R.id.textView8);
        TextView tvgyroY = findViewById(R.id.textView10);
        TextView tvgyroZ = findViewById(R.id.textView12);

        TextView tvaccelX = findViewById(R.id.textView14);
        TextView tvaccelY = findViewById(R.id.textView16);
        TextView tvaccelZ = findViewById(R.id.textView18);

        TextView tvhydrogen = findViewById(R.id.textView20);
        TextView tvsmoke = findViewById(R.id.textView22);
        TextView tvmethane = findViewById(R.id.textView24);
        TextView tvlpg = findViewById(R.id.textView26);

        TextView tvtemp = findViewById(R.id.textView28);
        TextView tvhumid = findViewById(R.id.textView30);

        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        findViewById(R.id.button2).setOnClickListener((v) -> {
            calibratedGyroXDiff = Integer.parseInt(tvgyroX.getText().toString());
            calibratedGyroYDiff = Integer.parseInt(tvgyroY.getText().toString());
            calibratedGyroZDiff = Integer.parseInt(tvgyroZ.getText().toString());
        });
        findViewById(R.id.button3).setOnClickListener((v) -> {
            calibratedAccelXDiff = Integer.parseInt(tvaccelX.getText().toString());
            calibratedAccelYDiff = Integer.parseInt(tvaccelY.getText().toString());
            calibratedAccelZDiff = Integer.parseInt(tvaccelZ.getText().toString());
        });

        ((ChangeableDataListener) TempStorage.get("NETWORK_CHANGEABLE_DATA_LISTENER_INST")).changeListener((JSONObject obj) -> {
            System.out.println("Got: " + obj.toString());
            JSONArray radarDistances = obj.getJSONArray("radar");
            double temperature = obj.getDouble("temp");
            double humidity = obj.getDouble("humid");
            double lpg = obj.getDouble("lpg");
            double methane = obj.getDouble("methane");
            double smoke = obj.getDouble("smoke");

            double hydrogen = obj.getDouble("hydrogen");
//            double soilMoisture = obj.getDouble("soil_moisture");
            JSONArray gyro = obj.getJSONArray("gyro");
            JSONArray accel = obj.getJSONArray("accel");
            boolean metalDetected = obj.getBoolean("metal");

            if(metalDetected) {
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            }

            System.out.println(gyro.toString());
            System.out.println(accel.toString());

//            tvgyroX.setText("hey hi");

            tvgyroX.setText(String.valueOf(gyro.getInt(0) - calibratedGyroXDiff));
            tvgyroY.setText(String.valueOf(gyro.getInt(1) - calibratedGyroYDiff));
            tvgyroZ.setText(String.valueOf(gyro.getInt(2) - calibratedGyroZDiff));

            tvaccelX.setText(String.valueOf(accel.getInt(0) - calibratedAccelXDiff));
            tvaccelY.setText(String.valueOf(accel.getInt(1) - calibratedAccelYDiff));
            tvaccelZ.setText(String.valueOf(accel.getInt(2) - calibratedAccelZDiff));

            tvhydrogen.setText(String.valueOf(hydrogen));
            tvmethane.setText(String.valueOf(methane));
            tvsmoke.setText(String.valueOf(smoke));
            tvlpg.setText(String.valueOf(lpg));

            tvtemp.setText(String.valueOf(temperature));
            tvhumid.setText(String.valueOf(humidity));
        });
    }
}