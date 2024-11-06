package App;
import MVC.Model;
import com.sun.tools.javac.Main;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class LoginPage implements ActionListener {
    Model model = new Model();
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JLabel login_label = new JLabel("Login");
    JLabel password_label = new JLabel("Password");
    JLabel message_label = new JLabel();
    JTextField userLogin = new JTextField();
    JPasswordField userPassword = new JPasswordField();
    public JButton login_button = new JButton("Login");


    public LoginPage(){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            System.out.println("не удалось подключить lookandfeel");
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBounds(600, 300, 500, 500);
        panel.setLayout(null);
        frame.add(panel);


        login_label.setBounds(30, 30, 70, 30);
        panel.add(login_label);


        password_label.setBounds(30, 60, 70, 30);
        panel.add(password_label);


        userLogin.setBounds(100, 30, 150, 30);
        panel.add(userLogin);


        userPassword.setBounds(100, 60, 150, 30);
        panel.add(userPassword);



        login_button.setBounds(160, 100, 90, 40);
        login_button.setFocusable(false);
        login_button.addActionListener(this);
        panel.add(login_button);




    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject user;
        if (e.getSource() == login_button) {
            try {
                message_label.setText("");
                user = model.findUser(userLogin.getText());
                if (user == null) {
                    panel.add(message_label);
                    message_label.setBounds(80, 130, 200, 50);
                    message_label.setText("Пользователь не найден");

                }
                else {
                    String userName = (String) user.get("name");
                    String correct_password = (String) user.get("password");
                    String inputPassword = userPassword.getText();
                    if (!Objects.equals(inputPassword, correct_password)) {
                        panel.add(message_label);
                        message_label.setBounds(50, 130, 300, 50);
                        message_label.setText("Неверный пароль пользователя " + userName);

                    }
                    else {
                        frame.dispose();
                        MainPage mainPage = new MainPage();
                        mainPage.showMenu(user);
                    }
                }
            } catch (Exception ex) {
                System.out.println("ooshibka");


        }
    }

}

}
