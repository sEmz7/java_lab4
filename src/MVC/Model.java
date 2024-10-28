package MVC;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import Machines.WashingMachine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Model {
    private int id_to_add = 0;


    public void updateID() throws IOException {
        id_to_add = 0;
        BufferedReader buff = new BufferedReader(new FileReader("src/database.txt"));
        while(buff.readLine() != null){
            id_to_add ++;
        }
        buff.close();
    }



    public int getSize() {
        return id_to_add;
    }

    public JSONObject findUser(String userName) throws IOException, ParseException {
        Object o = new JSONParser().parse(new FileReader("src/users.json"));
        JSONArray jsonArray = (JSONArray) o;

        for (Object obj : jsonArray) {
            JSONObject user = (JSONObject) obj;
            String name = (String) user.get("name");

            if (Objects.equals(name, userName)) {
                return user;
            }
        }
        return null;
    }


    public List<String> readFile() throws IOException {
        return Files.readAllLines(Paths.get("src/database.txt"));
    }


    public String addMachine(WashingMachine machine) throws IOException {
        if (machine == null)
            return null;

        FileOutputStream fos = new FileOutputStream("src/database.txt", true);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        id_to_add += 1;
        String str = id_to_add + ";" + machine.getClass().getSimpleName()
                + ";" + machine.getMaxWeight() + ";"+ machine.getLinen().getLinentype() + ";" +
                machine.getLinen().getTemperature() + ";" + machine.getLinen().getLinenWeight() + "\n";
        osw.write(str);
        osw.close();
        fos.close();
        return str;
    }


    public void updateLines(List<String> lines) throws IOException {
        if (lines == null)
            throw new NullPointerException();

        FileOutputStream fos = new FileOutputStream("src/database.txt", false);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

        for (String line : lines) {
            osw.write(line + "\n");
        }


        osw.close();
    }
}