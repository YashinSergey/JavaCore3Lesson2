package homework.chat.network;


import homework.chat.exceptions.MyAuthorizationException;
import homework.chat.message.MessageCreator;
import homework.chat.message.MessageSender;

import javax.swing.*;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Network implements Closeable {

    private static final String AUTH_PATTERN = "/auth %s %s";
    private static final String MESSAGE_SEND_PATTERN = "/w %s %s";
    private static final String USER_LIST_PATTERN = "/userlist";
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^/w (\\w+) (.+)", Pattern.MULTILINE);

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private MessageSender messageSender;
    private final Thread receiver;
    private String username;
    private final String hostName;
    private final int port;


    public Network(String hostName, int port, MessageSender messageSender) throws IOException {
        this.hostName = hostName;
        this.port = port;
        this.messageSender = messageSender;

        this.receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("You are online!");
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String text = in.readUTF();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("New message " + text);
                                Matcher matcher = MESSAGE_PATTERN.matcher(text);
                                if (matcher.matches()) {
                                    MessageCreator msg = new MessageCreator(matcher.group(1), username,
                                            matcher.group(2));
                                    messageSender.submitMessage(msg);
                                } else if (text.startsWith(USER_LIST_PATTERN)) {
                                    // TODO обновить список подключенных пользователей
                                }
                            }
                        });
                    } catch (SocketException e) {
                        System.out.println("You are out of chat.");
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void sendMessageToUser(MessageCreator message){
        sendMessage(String.format(MESSAGE_SEND_PATTERN, message.getUserTo(), message.getMessage()));
    }

    public void sendMessage(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authorize(String username, String password) throws IOException {
        socket = new Socket(hostName, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        out.writeUTF(String.format(AUTH_PATTERN, username, password));
        String response = in.readUTF();
        if (response.equals("/auth is successful")) {
            this.username = username;
            receiver.start();
        } else {
            throw new MyAuthorizationException("");
        }
    }

    @Override
    public void close() throws IOException {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiver.interrupt();
        try {
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}
