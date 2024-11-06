package MVC;

import Machines.AutomaticMachine;
import Machines.Linen;
import Machines.ManualMachine;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

public class View {
    private final Scanner scanner = new Scanner(System.in);

    public String getLine() {
        return scanner.nextLine();
    }

    public int getInt() {
        return scanner.nextInt();
    }

    public void displayText(String text) {
        System.out.print(text);
    }

    public void firstMessage(String user_name){
        System.out.println( "Добро пожаловать, " + user_name + "!");
    }


    public void displayMenu(String right) {
        if(Objects.equals(right, "user")) {
            System.out.println(
                    """
                            0. Выйти из программы
                            1. Вывести все записи о стиральных машинах
                            2. Добавить новую запись о стиральной машине
                            3. Обновить запись о стиральной машине
                            4. Удалить запись о стиральной машине
                            5. Включить режим откладки
                            """
            );
        }
        else if (Objects.equals(right, "root")){
            System.out.println(
                    """
                            0. Выйти из программы
                            1. Просмотреть все записи о стиральных машинах
                            2. Добавить новую запись о стиральной машине
                            3. Обновить запись о стиральной машине
                            4. Удалить запись о стиральной машине
                            5. Включить режим откладки
                            6. Запустить автотесты
                            """
            );
        }



    }
    public void machineVariation(){
        System.out.println(
                """
                        Выбери тип стиральной машины для добавления:
                        
                        1. Automatic Machine
                        2. Manual Machine
                        """
        );
    }

    public AutomaticMachine createAutomaticMachine(){
        displayText("Введите макс вместимость автоматической стиралки: ");
        int max_weight = Integer.parseInt(getLine());
        displayText("Введите тип загружаемого белья: ");
        String linentype = getLine();
        displayText("Введите температуру стирки: ");
        int temperature = Integer.parseInt(getLine());
        displayText("Введите вес белья: ");
        int linen_weight = Integer.parseInt(getLine());
        Linen linen = new Linen(linentype, temperature, linen_weight);

        return new AutomaticMachine(max_weight, linen);
    }


    public ManualMachine createManualMachine() {
        displayText("Введите макс вместимость автоматической стиралки: ");
        int max_weight = Integer.parseInt(getLine());
        displayText("Введите тип загружаемого белья: ");
        String linentype = getLine();
        displayText("Введите температуру стирки: ");
        int temperature = Integer.parseInt(getLine());
        displayText("Введите вес белья: ");
        int linen_weight = Integer.parseInt(getLine());
        Linen linen = new Linen(linentype, temperature, linen_weight);

        return new ManualMachine(max_weight, linen);
    }

}
