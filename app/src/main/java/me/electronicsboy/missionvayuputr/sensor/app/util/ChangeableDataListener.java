package me.electronicsboy.missionvayuputr.sensor.app.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeableDataListener implements Client.DataListener {
    private Client.DataListener l;

    @Override
    public void gotData(JSONObject data) throws JSONException {
        if(l != null) {
            System.out.println("Not ignoring: " + data.toString());
            l.gotData(data);
        } else
            Log.d("ChangeableDataListener", "Ignoring received data \"" + data.toString() + "\"");
    }

    public void changeListener(Client.DataListener newListener) {
         System.out.println("CHANGEDDDD"); this.l = newListener;
    }
}
