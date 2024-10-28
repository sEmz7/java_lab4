package Machines;

import java.util.Objects;

public class AutomaticMachine extends WashingMachine{


    public AutomaticMachine(int max_weight) {
        super(max_weight);
    }


    public AutomaticMachine(int max_weight, Linen linen) {
        super(max_weight);
        setLinen(linen);
    }


    @Override
    public String load(Linen linen) {
        if (loadedWeight + linen.getLinenWeight()  > max_linenWeight) {
            return "Превышена максимальная вместимость стиралки";
        }
        else if (Objects.equals(getLinen().getLinentype(), "неопределен")) {
            setLinen(linen);
            this.loadedWeight = linen.getLinenWeight();
            return "Загружено белье типа " + linen.getLinentype()  + " массой " + linen.getLinenWeight() + "кг. Текущая масса " + loadedWeight + "кг";
        }
        else if (linen.getTemperature() != getLinen().getTemperature() ) {
            return "Уже загружено белье с другой температурой стирки";
        }
        else if (Objects.equals(getLinen().getLinentype(), linen.getLinentype())) {
            this.loadedWeight += linen.getLinenWeight();
            return "Добавлено белье типа " + linen.getLinentype() + " массой " + linen.getLinenWeight() + "кг. Текущая масса " + loadedWeight + "кг";
        }

        else {
            return "В стиралке уже загружено белье другого типа";
        }
    }
}