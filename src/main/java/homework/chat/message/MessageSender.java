package ru.geekbrains.classes.lesson8.message;

public interface MessageSender {

    void submitMessage(MessageCreator message);

    void addUser(String user);
}
