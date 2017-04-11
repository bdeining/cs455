package record;

public class Population {
    private double population;

    private double elderlyPopulation;

    public Population(double population, double elderlyPopulation) {
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
