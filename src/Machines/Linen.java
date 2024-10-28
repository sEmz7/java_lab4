package Machines;

public class Linen {
    private String linentype = "неопределен";
    private int temperature;
    private int linenWeight;

    public Linen() {

    }


    public Linen(String linentype, int temperature,int linenWeight) {
        this.linentype = linentype;
        this.temperature = temperature;
        this.linenWeight = linenWeight;

    }

    public String getLinentype() {
        return linentype;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getLinenWeight() {
        return linenWeight;
    }
}
