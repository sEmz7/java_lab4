package MVC;
import Machines.AutomaticMachine;
import Machines.Linen;
import Machines.ManualMachine;
import Machines.WashingMachine;
import config.ConfigReader;
import mylogging.ExcMsgLog;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Controller {
    public final Model model;
    public final View view;
    private ExcMsgLog log;
    private ConfigReader configReader;
    private boolean debugMode = false;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        try {
            this.log = new ExcMsgLog("src/logs/lab4_res", true);
            this.configReader = new ConfigReader();
        } catch (IOException e) {
            view.displayText("Не удалось создать экземпляр объекта ExcMsgLog для записи информации в лог файл\n\n");
        }
    }


    private void runAutotests(){
        log.writeFine("Start autotests: " + LocalDateTime.now().format(formatter));

        List<String> lines = List.of(
                "1;AutomaticMachine;100;темное;60;15",
                "2;ManualMachine;70;светлое;45;30"
        );


        try {
            lines = model.readFile();
            log.writeInfo("Функция readFile отработала без исключений");
            view.displayText("Функция readFile отработала без исключений\n");
        } catch (IOException e) {
            log.writeSevere("Функция readLines выбросила исключение IOException");
            view.displayText("Функция readLines выбросила исключение IOException\n");
        }


        try {
            Linen linen = new Linen("цветное", 30, 15);
            Linen linen2 = new Linen("темное", 45, 40);
            model.addMachine(new AutomaticMachine(70, linen));
            model.addMachine(new ManualMachine(100, linen2));
            log.writeInfo("Функция addMachine отработала без исключений");
            view.displayText("Функция addMachine отработала без исключений\n");

        } catch (IOException e) {
            log.writeSevere("Функция addMachine выбросила исключение IOException");
            view.displayText("Функция addMachine выбросила исключение IOException\n");
        }


        try {
            model.updateLines(lines);
            log.writeInfo("Функция updateLines отработала без исключений");
            view.displayText("Функция updateLines отработала без исключений\n\n");
        } catch (IOException e) {
            log.writeSevere("Функция updateLines выбросила исключение IOException");
            view.displayText("Функция updateLines выбросила исключение IOException\n\n");
        } catch (NullPointerException e) {
            log.writeSevere("Функция updateLines выбросила исключение NullPointerException");
            view.displayText("Функция updateLines выбросила исключение NullPointerException\n\n");
        }


        log.writeFine("Finish autotests: " + LocalDateTime.now().format(formatter));
    }




    public JSONObject getUser() throws IOException, ParseException {
        JSONObject user;
        while (true) {
            view.displayText("Введите ваш никнейм: ");
            String username = view.getLine();

            user = model.findUser(username);

            if (user == null) {
                view.displayText("Пользователь с таким ником не найден. Попробуйте еще раз.\n\n");

                continue;
            }
            break;
        }
        return user;
    }


    public void checkPassword(JSONObject user) {
        String userName = (String) user.get("name");
        String correct_password = (String) user.get("password");

        while(true) {
            view.displayText(userName + ", введите Ваш пароль: ");
            String password = view.getLine();

            if (!Objects.equals(password, correct_password)) {
                view.displayText("Неверный пароль пользователя " + userName + ". Попробуйте еще раз.\n\n");
                continue;
            }
            break;
        }
    }


    public List<String> getLines(){
        try {
            return model.readFile();
        } catch (Exception e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + "Не удалось получить информацию из файла");
            }
            view.displayText("Не удалось получить информацию из файла\n\n");
            return null;
        }
    }


    public void run() {

        try {
            model.updateID();
        } catch (IOException e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось найти пользователя");
            }
            view.displayText("Не удалось обновить данные файла database.txt");
            return;
        }

        JSONObject user = null;
        try {
            user = getUser();
        } catch (IOException | ParseException e) {
            view.displayText("Не удалось найти пользователя\n\n");
        }

        checkPassword(user);

        view.firstMessage((String) user.get("name"));
        view.displayText("\n");

        boolean running = true;

        while(running) {
            view.displayMenu((String) user.get("right"));
            int choice = 0;

            try {
                choice = Integer.parseInt(view.getLine());
            } catch (NumberFormatException e) {
                if (debugMode) {
                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён номер команды");
                }
                view.displayText("Номер команды - целое число! Попробуйте еще раз.\n\n");
                continue;
            }

            List<String> lines = getLines();


            switch (choice){
                case 0: {
                    view.displayText("Программа завершена, данные сохранены.");
                    running = false;
                    break;
                    }
                case 1: {
                    for (String line : lines) {
                        String[] machine = line.split(";");
                        view.displayText("ID= " + machine[0] + ", Machine type: " + machine[1] +
                                ", Capacity: " + machine[2] + "kg" + ", Linen type: " + machine[3] +
                                ", Temperature: " + machine[4] + ", Linen weight: " + machine[5] + "kg.\n");
                    }
                    view.displayText("\n");
                    break;
                }
                case 2: {
                    WashingMachine washingMachine = null;
                    int variant = 1;

                    view.machineVariation();
                    try {
                        variant = Integer.parseInt(view.getLine());
                    } catch (NumberFormatException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён номер стиральной машины");
                        }
                        view.displayText("Номер варианта - целое число! Попробуйте еще раз.\n\n");
                        break;
                    }

                    boolean flag = false;

                    switch (variant) {
                        case 1: {
                            try {
                                washingMachine = view.createAutomaticMachine();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о стиральной машине");
                                }
                                view.displayText("Ошибка при вводе данных, попробуйте еще раз.\n\n");
                                flag = true;
                                break;
                            }
                        }
                        case 2: {
                            try {
                                washingMachine = view.createManualMachine();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о стиральной машине");
                                }
                                view.displayText("\nОшибка при вводе данных, попробуйте еще раз.\n\n");
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        break;
                    }

                    String added_str = null;


                    try {
                        model.updateID();
                        added_str = model.addMachine(washingMachine);
                        lines.add(added_str);
                    } catch (IOException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось добавить стиральную машину");
                        }
                        view.displayText("Не удалось добавить стиралку");
                    }

                    if (added_str != null) {
                        view.displayText("\nСтиралка успешно добавлена\n\n");
                    }

                    break;
                }


                case 3: {


                    view.displayText("Введите ID стиральной машины, информацию о которой хотите обновить: ");

                    int id_to_update;

                    try {
                        id_to_update = Integer.parseInt(view.getLine());
                        if (id_to_update < 1 || id_to_update > model.getSize()) {
                            throw new IllegalArgumentException();
                        }
                    } catch (NumberFormatException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён ID");
                        }
                        view.displayText("ID стиральной машины - число!\n\n");
                        break;
                    } catch (IllegalArgumentException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Нет такого ID");
                        }
                        view.displayText("Нет такого ID.\n\n");
                        break;
                    }


//                    String[] id_line = lines.get(id_to_update - 1).split(";");
                    String machine_type = lines.get(id_to_update - 1).split(";")[1];
//                    String[] machine_params = {id_line[3], id_line[4], id_line[5], id_line[6]};


                    String machine_str = "";

                    boolean flag = false;

                    switch (machine_type) {
                        case "AutomaticMachine":
                            try {
                                AutomaticMachine machine = view.createAutomaticMachine();
                                machine_str = String.valueOf(id_to_update) + ";" +
                                        machine.getClass().getSimpleName() + ";" + machine.getMaxWeight() + ";" +
                                        machine.getLinen().getLinentype() + ";" + machine.getLinen().getTemperature()
                                        + ";" + machine.getLinen().getLinenWeight();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о стиральной машине");
                                }
                                view.displayText("\nОшибка при вводе данных.\n\n");
                                flag = true;
                                break;
                            }
                        case "ManualMachine":
                            try {
                                ManualMachine machine = view.createManualMachine();
                                machine_str = String.valueOf(id_to_update) + ";" +
                                        machine.getClass().getSimpleName() + ";" + machine.getMaxWeight() + ";" +
                                        machine.getLinen().getLinentype() + ";" + machine.getLinen().getTemperature()
                                        + ";" + machine.getLinen().getLinenWeight();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о стиральной машине");
                                }
                                view.displayText("\nОшибка при вводе данных.\n\n");
                                flag = true;
                                break;
                            }
                    }

                    if (flag) {
                        break;
                    }

                    lines.set(id_to_update - 1, machine_str);

                    try {
                        model.updateLines(lines);
                        view.displayText("\nИнформация успешно обновлена.\n\n");
                    } catch (IOException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось записать изменения в файл");
                        }
                        view.displayText("\nНе удалось обновить информацию в файле.\n\n");
                    }

                    break;
                }


                case 4: {
                    view.displayText("Введите ID стиральной машины информацию о которой хотите удалить: ");
                    int id_to_remove = 0;

                    try {
                        id_to_remove = Integer.parseInt(view.getLine());
                        if (id_to_remove > model.getSize() || id_to_remove < 1) {
                            throw new IllegalArgumentException();
                        }
                    } catch (NumberFormatException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён ID");
                        }
                        view.displayText("\nID стиральной машины - число!\n\n");
                        break;
                    } catch (IllegalArgumentException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Нет такого ID.");
                        }
                        view.displayText("\nНет такого ID.\n\n");
                        break;
                    }


                    if (id_to_remove - 1 != lines.size() - 1) {
                        for (int i = id_to_remove; i < lines.size(); i++) {
                            String[] lineparts = lines.get(i).split(";");
                            lineparts[0] = "" + (i);
                            String newline = lineparts[0] + ";" + lineparts[1] + ";" + lineparts[2] + ";" +
                                    lineparts[3] + ";" + lineparts[4] + ";" + lineparts[5];
                            lines.set(i, newline);
                        }
                    }


                    lines.remove(id_to_remove - 1);

                    try {
                        model.updateLines(lines);
                        view.displayText("\nИнформация о стиральной машине успешно удалена.\n\n");
                    } catch (IOException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось записать изменения в файл");
                        }
                        view.displayText("\nНе удалось обновить информацию.\n\n");
                    }

                    break;
                }

                case 5: {
                    if (!debugMode) {
                        configReader.setProperty("debugmode", "true");
                        debugMode = true;
                        view.displayText("\nРежим отладки включен\n\n");
                    } else {
                        view.displayText("\nРежим откладки уже включен.\n\n");
                    }
                    break;
                }
                case 6: {
                    if (user.get("right") == "root") {
                        continue;
                    }
                    else {
                        view.displayText("\nАвтотесты запущены\n\n");
                        runAutotests();
                        break;
                    }
                }

                default:
                    view.displayText("Нет такой команды.\n\n");
            }
        }
    }


}