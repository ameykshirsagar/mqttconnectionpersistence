package com.alabs.mqttservicetest;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by ameykshirsagar on 21/07/16.
 */
public class Connection {
    private static MqttAndroidClient client;

    public static void setClient(MqttAndroidClient cl){
        client = cl;
    }
    public static MqttAndroidClient getClient(){
        return client;
    }
}
