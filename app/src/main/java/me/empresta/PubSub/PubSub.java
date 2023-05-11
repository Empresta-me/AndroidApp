package me.empresta.PubSub;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;


public class PubSub{

    static String host = "192.168.206.52";


    @Inject
    Message_Handler message_handler;

    @Inject
    public PubSub(Message_Handler message_handler){
        this.message_handler = message_handler;
    }

    public  void start_listening(String EXCHANGE_NAME)throws Exception{

        Thread subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {

                    try {
                        ConnectionFactory factory = new ConnectionFactory();
                        factory.setHost(host);
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(1);

                        //channel.queueDeclare(queue, false, false, false, null);

                        // Declare the exchange
                        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

                        // Declare a queue and bind it to the exchange
                        String queueName = channel.queueDeclare().getQueue();
                        channel.queueBind(queueName, EXCHANGE_NAME, "");

                        System.out.println("Waiting for messages. To exit press CTRL+C");

                        // Create a consumer and start consuming messages
                        Consumer consumer = new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                                String message = new String(body, StandardCharsets.UTF_8);
                                message_handler.Handle(message, EXCHANGE_NAME);
                                channel.basicAck(envelope.getDeliveryTag(), false);
                            }
                        };
                        channel.basicConsume(queueName, true, consumer);


                    } catch (TimeoutException | IOException e) {
                        throw new RuntimeException(e);

                    }
            }
        });
        subscribeThread.start();
    }

    /*
    Vouch
        - Public keys
        - Vouch Type
        - Description
    */
    public static void Publish_Vouch(String my_public_key, String other_public_key, String Description){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();

                    factory.setHost(host);
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(my_public_key, "fanout"); //channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                    int nonce = 0;

                    JSONObject j_message = new JSONObject();
                    j_message.put("my_public_key", my_public_key);
                    j_message.put("nonce", nonce);
                    j_message.put("other_public_key", other_public_key);
                    j_message.put("Description", Description);

                    channel.basicPublish(my_public_key, "", null, j_message.toString().getBytes()); //channel.basicPublish("", QUEUE_NAME, null, j_message.toString().getBytes());

                    System.out.println(" [x] Sent '" +  j_message.toString() + "'");

                    channel.close();
                    connection.close();

                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException("Rabbitmq problem", e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }
    /*
    Item Announcement Update
         X -Public key
         X -Nonce (Proof-of-Work)
         X -Signature
         X -Name
         X -Description
         X -Photo
    */
    public static void Publish_Item_Announcement_Update(String my_public_key, String Signature, String Name, String Description, String Photo){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();

                    factory.setHost(host);
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(my_public_key, "fanout"); //channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                    int nonce = 0; //Nonce that needs to be randomized

                    JSONObject j_message = new JSONObject();
                    j_message.put("my_public_key", my_public_key);
                    j_message.put("Signature", Signature);
                    j_message.put("nonce", nonce);
                    j_message.put("Name", Name);
                    j_message.put("Description", Description);
                    j_message.put("Photo", Photo);

                    channel.basicPublish(my_public_key, "", null, j_message.toString().getBytes()); //channel.basicPublish("", QUEUE_NAME, null, j_message.toString().getBytes());

                    System.out.println(" [x] Sent '" +  j_message.toString() + "'");

                    channel.close();
                    connection.close();

                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException("Rabbitmq problem", e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

    /*
    Item Request
      X -Public key
      X -Nonce (Proof-of-Work)
      X -Signature
      X -Name
      X -Issuer (optional)
    */
    public static void Publish_Item_Request(String my_public_key, String Signature, String Name, String Issuer_public_key){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();

                    factory.setHost(host);
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(my_public_key, "fanout"); //channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                    int nonce = 0; //Nonce that needs to be randomized

                    JSONObject j_message = new JSONObject();
                    j_message.put("my_public_key", my_public_key);
                    j_message.put("nonce", nonce);
                    j_message.put("Issuer_public_key", Issuer_public_key);
                    j_message.put("Signature", Signature);
                    j_message.put("Name", Name);

                    channel.basicPublish(my_public_key, "", null, j_message.toString().getBytes()); //channel.basicPublish("", QUEUE_NAME, null, j_message.toString().getBytes());

                    System.out.println(" [x] Sent '" +  j_message + "'");

                    channel.close();
                    connection.close();

                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException("Rabbitmq problem", e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

}