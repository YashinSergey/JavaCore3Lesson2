package ru.geekbrains.classes.lesson8.swing;

import ru.geekbrains.classes.lesson8.Network;
import ru.geekbrains.classes.lesson8.message.MessageCellRenderer;
import ru.geekbrains.classes.lesson8.message.MessageCreator;
import ru.geekbrains.classes.lesson8.message.MessageSender;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MainWindow extends JFrame implements MessageSender {

    private JButton sendButton;
    private JPanel southPanel;
    private JPanel centralPanel;
    private JTextField textField;
    private JList<MessageCreator> messageList;
    private DefaultListModel<MessageCreator> messageListModel;
    private JScrollPane scroll;
    private Network network;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;


    public MainWindow(){
        setBounds(600,250,550,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        southPanel = new JPanel();
        add(southPanel, BorderLayout.SOUTH);
        southPanel.setLayout(new BorderLayout());

        sendButton = new JButton("SEND");
        sendButton.setBackground(new Color(120,100,110));
        sendButton.setForeground(new Color(255,255,255));
        southPanel.add(sendButton, BorderLayout.EAST);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userTo = userList.getSelectedValue();
                if (userTo == null) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Не указан получатель",
                            "Отправка сообщения",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String text = textField.getText();
                if (text == null || text.trim().isEmpty()) {
                    return;
                }

                MessageCreator msg = new MessageCreator(network.getUsername(), userTo, text.trim());
                submitMessage(msg);
                network.sendMessageToUser(msg);
            }
        });


        textField = new JTextField();
        textField.setForeground(Color.WHITE);
        textField.setBackground(new Color(99, 87, 110));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        southPanel.add(textField, BorderLayout.CENTER);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String userTo = userList.getSelectedValue();
                    if (userTo == null) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Не указан получатель",
                                "Отправка сообщения",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String text = textField.getText();
                    if (text == null || text.trim().isEmpty()) {
                        return;
                    }

                    MessageCreator msg = new MessageCreator(network.getUsername(), userTo, text.trim());
                    submitMessage(msg);
                    network.sendMessageToUser(msg);
                }
            }
        });

        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
        messageList.setCellRenderer(new MessageCellRenderer());

        centralPanel = new JPanel();
        centralPanel.setLayout(new BorderLayout());
        centralPanel.add(messageList,BorderLayout.SOUTH);
        centralPanel.setBackground(new Color(85, 80, 90));

        scroll = new JScrollPane(centralPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        messageList.setBackground(centralPanel.getBackground());
        messageList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBackground(new Color(85, 80, 100));
        userList.setForeground(new Color(255,255,255));
        userList.setPreferredSize(new Dimension(70,0));
        add(userList, BorderLayout.EAST);
        addUser("john");
        addUser("valery");
        addUser("bob");
        addUser("jenifer");
        userList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                textField.requestFocus();
            }
        });


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (network != null) {
                        network.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });

        setVisible(true);

        try {
            network = new Network("localhost", 7777, this);
        } catch (IOException e) {
            e.printStackTrace();
        }



        LogInDialog logInDialog = new LogInDialog(this, network);
        logInDialog.setVisible(true);
        logInDialog.getNameField().requestFocus();

        if(!logInDialog.isConnected()){
            System.exit(0);
        }

        setTitle("Chat. user : " + network.getUsername());
        textField.requestFocus();
    }


    @Override
    public void submitMessage(MessageCreator message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                messageListModel.add(messageListModel.size(), message);
                messageList.ensureIndexIsVisible(messageListModel.size() - 1);
                textField.setText(null);
                textField.requestFocus();
            }
        });
    }

    @Override
    public void addUser(String user){
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               userListModel.add(userListModel.size(), user);
               userList.ensureIndexIsVisible(userListModel.size() - 1);
           }
       });
    }
}