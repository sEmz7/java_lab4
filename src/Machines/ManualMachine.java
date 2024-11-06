package Machines;

public class ManualMachine extends WashingMachine {


    public ManualMachine(int weight) {
        super(weight);
    }
    public ManualMachine(int weight, Linen linen) {
        super(weight);
        setLinen(linen);
    }



}