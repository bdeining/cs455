package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class ElderlyPopulationRecord implements Writable {

    private Population population;

    public ElderlyPopulationRecord() {

    }

    public void setPopulation(double elderlyPopulation, double totalPopulation) {
        population = new Population(totalPopulation, elderlyPopulation);
    }

    public double getElderlyPopulation() {
        return population.getElderlyPopulation();
    }

    public double getTotalPopulation() {
        return population.getPopulation();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(population.getPopulation());
        dataOutput.writeDouble(population.getElderlyPopulation());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        double totalPopulation = dataInput.readDouble();
        double elderlyPopulation = dataInput.readDouble();
        population = new Population(totalPopulation, elderlyPopulation);
    }

}
