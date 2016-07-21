package com.alabs.mqttservicetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttReceivedMessage;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final MqttConnectOptions options = new MqttConnectOptions();
        final MqttAndroidClient client = new MqttAndroidClient(getApplicationContext(),"tcp://test.mosquitto.org:1883","thisapp");
        options.setCleanSession(false);
        String[] urls = new String[2];
        urls[0] = "tcp://test.mosquitto.org:1883";
        options.setServerURIs(urls);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Connection.setClient(client);

                try {
                    Connection.getClient().connect(options, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Snackbar.make(view, "Success connection", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Connection.getClient().setCallback(new MqttCallback() {
                                @Override
                                public void connectionLost(Throwable cause) {
                                    Snackbar.make(view, "Lost connection", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                                @Override
                                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                                    Snackbar.make(view, "Message arrived -> "+topic+" : "+message.toString(), Snackbar.LENGTH_LONG)
//                                            .setAction("Action", null).show();
                                }

                                @Override
                                public void deliveryComplete(IMqttDeliveryToken token) {
                                    Connection.getClient().acknowledgeMessage(String.valueOf(token.getMessageId()));
                                }
                            });
                            try {
                                Connection.getClient().subscribe("mqttpersistservice", 0, view.getContext(), new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {

                                        Log.i("sub","Subscribe success");
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

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Snackbar.make(view, "Failure connection", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Log.e("mqtt",exception.toString());
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });


        Button nxtact = (Button) findViewById(R.id.nxtact);
        nxtact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),TestActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
