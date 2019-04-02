package homework.chat.swing;



import homework.chat.network.Network;
import homework.chat.exceptions.MyAuthorizationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class LogInDialog extends JDialog {

    private JTextField nameField;
    private JPasswordField passwordField;
    private JLabel lbUserName;
    private JLabel lbPassword;
    private JButton okButton;
    private JButton cancelButton;
    private final Network network;
    private boolean connected;
    private Timer timer;

    public LogInDialog(Frame parent, Network network) {
        super(parent, "Login", true);


        this.network = network;
        this.connected = false;

        JPanel textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        textPanel.setBackground(new Color(85, 80, 90));

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUserName = new JLabel("Username: ");
        lbUserName.setForeground(new Color(255,255,255));
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        textPanel.add(lbUserName, cs);

        nameField = new JTextField(20);
        nameField.setBackground(new Color(99, 87, 110));
        nameField.setForeground(new Color(255,255,255));
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        textPanel.add(nameField, cs);
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    passwordField.requestFocus();
                }
            }
        });

        lbPassword = new JLabel("Password: ");
        lbPassword.setForeground(new Color(255,255,255));
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        textPanel.add(lbPassword, cs);

        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(99, 87, 110));
        passwordField.setForeground(new Color(255,255,255));
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        textPanel.add(passwordField, cs);
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    okListener();
                }
            }
        });

        timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.dispose();
            }
        });

        okButton = new JButton("Ok");
        okButton.setBackground(new Color(120,100,110));
        okButton.setForeground(new Color(255,255,255));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okListener();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(120,100,110));
        cancelButton.setForeground(new Color(255,255,255));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parent.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(85, 80, 90));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);


        getContentPane().add(textPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public void okListener(){
        try {
            network.authorize(nameField.getText(), String.valueOf(passwordField.getPassword()));
            connected = true;
        } catch (MyAuthorizationException ex) {
            timer.start();
            JOptionPane.showMessageDialog(LogInDialog.this,
                    "Ошибка авторизации",
                    "Авторизация",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ex) {
            timer.start();
            JOptionPane.showMessageDialog(LogInDialog.this,
                    "Ошибка сети",
                    "Авторизация",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
    }

    public JTextField getNameField() {
        return nameField;
    }

    public boolean isConnected() {
        return connected;
    }
}
