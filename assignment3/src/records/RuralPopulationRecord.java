package records;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class RuralPopulationRecord implements Writable {

    private long population;

    private long ruralPopulation;

    private long urbanPopulation;

    public RuralPopulationRecord() {

    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getRuralPopulation() {
        return ruralPopulation;
    }

    public void setRuralPopulation(long ruralPopulation) {
        this.ruralPopulation = ruralPopulation;
    }

    public long getUrbanPopulation() {
        return urbanPopulation;
    }

    public void setUrbanPopulation(long urbanPopulation) {
        this.urbanPopulation = urbanPopulation;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(ruralPopulation);
        dataOutput.writeLong(urbanPopulation);
        dataOutput.writeLong(population);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        ruralPopulation = dataInput.readLong();
        urbanPopulation = dataInput.readLong();
        population = dataInput.readLong();
    }

    @Override
    public String toString() {
        return generatePopulationString();
    }

    private String generatePopulationString() {
        double percentRural = (double) ruralPopulation / (double) population * 100;
        double percentUrban = (double) urbanPopulation / (double) population * 100;
        return String.format("%s\t%s\t%s\t%s\t%s", population, ruralPopulation, percentRural, urbanPopulation, percentUrban);
    }
}
