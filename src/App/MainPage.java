package App;

import MVC.Controller;
import MVC.Model;
import MVC.View;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.util.List;
import java.util.Objects;

public class MainPage implements ActionListener {
    final Model model = new Model();
    final View view = new View();
    final Controller controller = new Controller(model, view);

    final Font italic = new Font("serif", Font.BOLD, 28);
    final Font viewFont = new Font("Arial", Font.BOLD, 15);

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JFrame viewFrame = new JFrame();

    JLabel hello_label = new JLabel();
    JButton view_button = new JButton("Вывести все записи");
    JButton add_button = new JButton("Добавить запись");
    JButton update_button = new JButton("Обновить запись");
    JButton delete_button = new JButton("Удалить запись");
    JButton debug_button = new JButton("Включить режим отладки");
    JButton autotests_button = new JButton("Включить автотесты");
    JButton exit_button = new JButton("Вернуться в меню");
    JSONObject user = null;



    List<String> lines = controller.getLines();

    public void showMenu(JSONObject user) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        this.user = user;
        String rights = (String) user.get("right");

        if (Objects.equals(rights, "root")) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setBounds(470, 230, 800, 600);
            panel.setLayout(null);
            frame.add(panel);

            hello_label.setFont(italic);
            hello_label.setBounds(285, 10, 270, 30);
            hello_label.setText("Добро пожаловать!");
            panel.add(hello_label);

            view_button.setBounds(40, 100, 200, 45);
            view_button.setFocusable(false);
            view_button.addActionListener(this);
            panel.add(view_button);

            add_button.setBounds(40, 150, 200, 45);
            add_button.setFocusable(false);
            panel.add(add_button);

            update_button.setBounds(40, 200, 200, 45);
            update_button.setFocusable(false);
            panel.add(update_button);

            delete_button.setBounds(40, 250, 200, 45);
            delete_button.setFocusable(false);
            panel.add(delete_button);

            debug_button.setBounds(40, 300, 200, 45);
            debug_button.setFocusable(false);
            panel.add(debug_button);

            autotests_button.setBounds(40, 350, 200, 45 );
            autotests_button.setFocusable(false);
            panel.add(autotests_button);
        }
        else {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setBounds(470, 230, 800, 600);
            panel.setLayout(null);
            frame.add(panel);

            hello_label.setFont(italic);
            hello_label.setBounds(285, 10, 270, 30);
            hello_label.setText("Добро пожаловать!");
            panel.add(hello_label);

            view_button.setBounds(40, 100, 200, 45);
            view_button.setFocusable(false);
            view_button.addActionListener(this);
            panel.add(view_button);
            add_button.setBounds(40, 150, 200, 45);
            add_button.setFocusable(false);
            panel.add(add_button);
            update_button.setBounds(40, 200, 200, 45);
            update_button.setFocusable(false);
            panel.add(update_button);
            delete_button.setBounds(40, 250, 200, 45);
            delete_button.setFocusable(false);
            panel.add(delete_button);
            debug_button.setBounds(40, 300, 200, 45);
            debug_button.setFocusable(false);
            panel.add(debug_button);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == view_button) {
            frame.dispose();

            JPanel viewPanel = new JPanel();
            viewFrame.add(viewPanel);

            viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewFrame.setVisible(true);
            viewFrame.setBounds(470, 230, 900, 600);
            viewPanel.setLayout(null);

            exit_button.setBounds(350, 5, 200, 40);
            exit_button.setFocusable(false);
            exit_button.addActionListener(this);
            viewPanel.add(exit_button);
            int y_position = 40;
            for (String line : lines) {
                JLabel machineInfo = new JLabel();
                machineInfo.setFont(viewFont);

                String[] machine = line.split(";");
                machineInfo.setBounds(10, y_position, 900, 40);
                String text = "ID= " + machine[0] + ", Machine type: " + machine[1] +
                        ", Capacity: " + machine[2] + "kg" + ", Linen type: " + machine[3] +
                        ", Temperature: " + machine[4] + ", Linen weight: " + machine[5] + "kg.";
                machineInfo.setText(text);
                viewPanel.add(machineInfo);
                y_position += 30;
            }

        }

        else if (e.getSource() == exit_button) {
            viewFrame.dispose();

            try {
                showMenu(user);
            } catch (Exception ex) {
                System.out.println("json obj ex");
            }
        }
    }
}