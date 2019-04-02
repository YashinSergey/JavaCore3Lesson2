package homework.chat.server;


import homework.chat.authorization.Authorization;
import homework.chat.authorization.AuthorizationService;
import homework.chat.message.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {

    private static final Pattern AUTH_PATTERN = Pattern.compile("^/auth (\\w+) (\\w+)$");
    private static final String USER_CONNECTED_PATTERN = "/userconn";
    private static final String USER_DISCONNECTED_PATTERN = "/userdisconn";
    private Authorization authService = new AuthorizationService();
    private Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    private DataInputStream inp;
    private DataOutputStream out;
    private ClientHandler clientHandler;

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started!");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected!");
                inp = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                try {
                    String authMessage = inp.readUTF();
                    Matcher matcher = AUTH_PATTERN.matcher(authMessage);
                    if (matcher.matches()) {
                        String username = matcher.group(1);
                        String password = matcher.group(2);
                        if (authService.authUser(username, password)) {
                            clientHandlerMap.put(username, new ClientHandler(username, socket, this));
                            out.writeUTF("/auth is successful");
                            out.flush();
                            broadcastUserConnected(username);
                            System.out.printf("Authorization for user %s is successful%n", username);
                        } else {
                            System.out.printf("Authorization for user %s is failed%n", username);
                            out.writeUTF("/auth fails");
                            out.flush();
                            socket.close();
                        }
                    } else {
                        System.out.printf("Incorrect authorization message %s%n", authMessage);
                        out.writeUTF("/auth failed");
                        out.flush();
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, ClientHandler> getClientHandlerMap() {
        return clientHandlerMap;
    }

    public void sendMessage(String userTo, String userFrom, String message) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(userTo);
        if(userToClientHandler != null){
            userToClientHandler.sendMessage(userFrom, message);
        } else {
            System.out.printf("User %s not found. Message from %s is lost.%n", userTo, userFrom);
        }
    }

    public List<String> getUserList() {
        return new ArrayList<>(clientHandlerMap.keySet());
    }

    public void unsubscribeClient(ClientHandler clientHandler) {
        clientHandlerMap.remove(clientHandler.getUsername());
        broadcastUserDisconnected(clientHandler);
    }

    public void broadcastUserConnected(String username) {
        // TODO сообщать всем о том, что конкретный пользователь подключился
    }

    public void broadcastUserDisconnected(ClientHandler clientHandler) {
        // TODO сообщать о том, что конкретный пользователь отключился
    }
}
