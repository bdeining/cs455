package record;

public class Population {
    private double population;

    private double elderlyPopulation;

    public Population() {
        population = 0.0;
        elderlyPopulation = 0.0;
    }

    public Population(double population, double elderlyPopulation) {
        this.population = population;
        this.elderlyPopulation = elderlyPopulation;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public double getElderlyPopulation() {
        return elderlyPopulation;
    }

    public void setElderlyPopulation(long elderlyPopulation) {
        this.elderlyPopulation = elderlyPopulation;
    }

}
