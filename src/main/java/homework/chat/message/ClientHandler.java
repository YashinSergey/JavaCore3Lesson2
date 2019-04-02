package homework.chat.message;


import homework.chat.server.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^/w (\\w+) (.+)", Pattern.MULTILINE);
    private static final String MESSAGE_SEND_PATTERN = "/w %s %s";
    private Thread handlerThread;
    private final DataOutputStream out;
    private final DataInputStream in;
    private final ChatServer server;
    private final String username;
    private Socket socket;


    public ClientHandler(String username, Socket socket, ChatServer server) throws IOException {
        this.username = username;
        this.socket = socket;
        this.server = server;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());


        this.handlerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (!Thread.currentThread().isInterrupted()){
                        String msg = in.readUTF();
                        System.out.printf("Message from user %s: %s%n", username, msg);
                        Matcher matcher = MESSAGE_PATTERN.matcher(msg);
                        if (matcher.matches()) {
                            String userTo = matcher.group(1);
                            String message = matcher.group(2);
                            server.sendMessage(userTo, username, message);
                        }
                    }
                } catch (EOFException e) {
                    System.out.printf("%s is disconnected%n", username);
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    server.unsubscribeClient(ClientHandler.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerThread.start();
    }

    public void sendMessage(String userTo, String message) throws IOException {
        out.writeUTF(String.format(MESSAGE_SEND_PATTERN, userTo, message));
        out.flush();
    }

    public String getUsername() {
        return username;
    }
}
