package records;

/**
 * This abstraction class is used to transfer multiple data entries
 * from the Mapper to the Reducer in the ElderlyPopulation Map/Reduce Job.
 */
public class ElderlyPopulation {
    private double population;

    private double elderlyPopulation;

    public ElderlyPopulation(double population, double elderlyPopulation) {
        this.population = population;
        this.elderlyPopulation = elderlyPopulation;
    }

    public double getPopulation() {
        return population;
    }

    public double getElderlyPopulation() {
        return elderlyPopulation;
    }

}
