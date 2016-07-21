package com.alabs.mqttservicetest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TestActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Connection.getClient().setTraceEnabled(true);
                Connection.getClient().setTraceCallback(new MqttTraceHandler() {
                    @Override
                    public void traceDebug(String source, String message) {
                        Log.i("mqtt trace","Source -> "+source+"\n: Message -> "+message);
                    }

                    @Override
                    public void traceError(String source, String message) {
                        Log.e("mqtt trace","Source -> "+source+"\n: Message -> "+message);
                    }

                    @Override
                    public void traceException(String source, String message, Exception e) {
                        Log.e("mqtt trace","Source -> "+source+"\n: Message -> "+message+"\nException -> "+e.toString());
                    }
                });
                if (Connection.getClient().isConnected()){
                    Log.i("mqtt","connection still persists");
                    try {
                        Connection.getClient().subscribe("mqttpersistservice", 0, view.getContext(), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Snackbar.make(view, "Subscribe success", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Snackbar.make(view, "Subscribe failure", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Log.e("error",exception.toString());
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    //Connection.getClient().registerResources(this);
                    Connection.getClient().setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            Log.i(topic,message.toString());
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });

                }else {
                    Log.i("mqtt","connection gone");
                }
            }
        });



    }


}
