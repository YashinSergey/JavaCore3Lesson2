package homework.chat;

import homework.chat.server.ChatServer;

public class ServerStart {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start(7777);
    }
}
