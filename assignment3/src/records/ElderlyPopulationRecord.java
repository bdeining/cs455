package records;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class ElderlyPopulationRecord implements Writable {

    private ElderlyPopulation elderlyPopulation;

    public ElderlyPopulationRecord() {

    }

    public void setPopulation(double elderlyPopulation, double totalPopulation) {
        this.elderlyPopulation = new ElderlyPopulation(totalPopulation, elderlyPopulation);
    }

    public double getElderlyPopulation() {
        return elderlyPopulation.getElderlyPopulation();
    }

    public double getTotalPopulation() {
        return elderlyPopulation.getPopulation();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(elderlyPopulation.getPopulation());
        dataOutput.writeDouble(elderlyPopulation.getElderlyPopulation());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        double totalPopulation = dataInput.readDouble();
        double elderlyPopulation = dataInput.readDouble();
        this.elderlyPopulation = new ElderlyPopulation(totalPopulation, elderlyPopulation);
    }

}
