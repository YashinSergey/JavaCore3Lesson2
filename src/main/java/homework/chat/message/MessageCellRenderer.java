package ru.geekbrains.classes.lesson8.message;

import javax.swing.*;
import java.awt.*;


public class MessageCellRenderer extends JPanel implements ListCellRenderer<MessageCreator> {

    private JLabel userName;
    private JLabel message;


    public MessageCellRenderer() {
        super();
        setLayout(new BorderLayout());
        userName = new JLabel();
        Font f = userName.getFont();
        userName.setFont(f.deriveFont(f.ITALIC | Font.BOLD));
        userName.setBackground(new Color(100, 80, 90));
        userName.setForeground(new Color(255,123,0));
        message = new JLabel();
        message.setForeground(Color.WHITE);
        add(userName, BorderLayout.NORTH);
        add(message, BorderLayout.SOUTH);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MessageCreator> list, MessageCreator value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setBackground(list.getBackground());
        userName.setOpaque(true);
        userName.setText(value.getUserFrom());
        message.setText(value.getMessage());
        return this;
    }

}
