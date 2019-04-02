package homework.chat.message;

public interface MessageSender {

    void submitMessage(MessageCreator message);

    void addUser(String user);
}
