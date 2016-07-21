# MQTT Connection Lifecycle Example

This is a repository that shows how to keep connection alive in between Android Activities.
1. Use FAB button to connect. 

2. Then press Next Activity. 

3. Check log to see the connection persistence log message.


If you want to read the payload, you can simply do

````
Connection.getClient().setCallback(new MqttCallback() {
    @Override
    public void connectionLost(Throwable cause) {
        
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
});
````
