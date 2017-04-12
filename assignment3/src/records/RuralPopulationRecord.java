package records;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class RuralPopulationRecord implements Writable {

    private long population;

    private long outsideUrbanPopulation;

    private long urbanPopulation;

    public RuralPopulationRecord() {

    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getOutsideUrbanPopulation() {
        return outsideUrbanPopulation;
    }

    public void setOutsideUrbanPopulation(long outsideUrbanPopulation) {
        this.outsideUrbanPopulation = outsideUrbanPopulation;
    }

    public long getUrbanPopulation() {
        return urbanPopulation;
    }

    public void setUrbanPopulation(long urbanPopulation) {
        this.urbanPopulation = urbanPopulation;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(outsideUrbanPopulation);
        dataOutput.writeLong(urbanPopulation);
        dataOutput.writeLong(population);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        outsideUrbanPopulation = dataInput.readLong();
        urbanPopulation = dataInput.readLong();
        population = dataInput.readLong();
    }

    @Override
    public String toString() {
        return generatePopulationString();
    }

    private String generatePopulationString() {
        double ruralPopulation = population - outsideUrbanPopulation - urbanPopulation;
        double percentRural = ruralPopulation / (double) population * 100;
        double percentUrban = ((double) urbanPopulation + outsideUrbanPopulation) / (double) population * 100;
        return String.format("%s\t%s\t%s\t%s\t%s",
                population,
                (outsideUrbanPopulation + urbanPopulation),
                ruralPopulation,
                percentRural,
                percentUrban);
    }
}
