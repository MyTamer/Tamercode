package Util.Statistics;

import java.util.Collections;
import Individuals.FitnessPackage.BasicFitness;
import Individuals.FitnessPackage.Fitness;
import Individuals.GEIndividual;
import Individuals.Individual;
import Mapper.GEGrammar;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Stores statistics from the population;
 * @author Blip
 *
 */
public class StatCatcher {

    protected ArrayList<Double> meanFitness;

    protected ArrayList<Double> bestFitness;

    protected ArrayList<Double> varFitness;

    protected ArrayList<Integer> minLength;

    protected ArrayList<Integer> maxLength;

    protected ArrayList<Double> aveLength;

    protected ArrayList<Double> varLength;

    protected ArrayList<Double> meanUsedGenes;

    protected ArrayList<Double> meanDerivationTreeDepth;

    protected ArrayList<Long> time;

    protected ArrayList<Integer> invalids;

    protected ArrayList<ArrayList<Double>> allFitness;

    protected ArrayList<Individual> bestIndividualOfGeneration;

    /** Creates a new instance of StatCatcher */
    public StatCatcher() {
        this.meanFitness = new ArrayList<Double>();
        this.bestFitness = new ArrayList<Double>();
        this.varFitness = new ArrayList<Double>();
        this.aveLength = new ArrayList<Double>();
        this.varLength = new ArrayList<Double>();
        this.maxLength = new ArrayList<Integer>();
        this.minLength = new ArrayList<Integer>();
        this.meanUsedGenes = new ArrayList<Double>();
        this.meanDerivationTreeDepth = new ArrayList<Double>();
        this.time = new ArrayList<Long>();
        this.invalids = new ArrayList<Integer>();
        this.allFitness = new ArrayList<ArrayList<Double>>();
        this.bestIndividualOfGeneration = new ArrayList<Individual>();
    }

    /**
     * New instance
     * @param gen number of generations
     */
    public StatCatcher(int gen) {
        this.meanFitness = new ArrayList<Double>(gen);
        this.bestFitness = new ArrayList<Double>(gen);
        this.varFitness = new ArrayList<Double>(gen);
        this.aveLength = new ArrayList<Double>(gen);
        this.varLength = new ArrayList<Double>(gen);
        this.maxLength = new ArrayList<Integer>(gen);
        this.minLength = new ArrayList<Integer>(gen);
        this.meanUsedGenes = new ArrayList<Double>(gen);
        this.meanDerivationTreeDepth = new ArrayList<Double>(gen);
        this.time = new ArrayList<Long>(gen);
        this.invalids = new ArrayList<Integer>(gen);
        this.allFitness = new ArrayList<ArrayList<Double>>(gen);
        this.bestIndividualOfGeneration = new ArrayList<Individual>(gen);
    }

    /**
     * Clear the structures in the class
     */
    public void clear() {
        this.bestFitness = new ArrayList<Double>();
        this.meanFitness = new ArrayList<Double>();
        this.varFitness = new ArrayList<Double>();
        this.aveLength = new ArrayList<Double>();
        this.varLength = new ArrayList<Double>();
        this.maxLength = new ArrayList<Integer>();
        this.minLength = new ArrayList<Integer>();
        this.meanUsedGenes = new ArrayList<Double>();
        this.meanDerivationTreeDepth = new ArrayList<Double>();
        this.invalids = new ArrayList<Integer>();
        this.allFitness = new ArrayList<ArrayList<Double>>();
        this.bestIndividualOfGeneration = new ArrayList<Individual>();
    }

    /**
     * Add time
     * @param t time
     */
    public void addTime(Long t) {
        this.time.add(t);
    }

    /**
     * Add stats. Extracts information from the fitness of the population.
     * @param popFitness fitness of the population
     */
    public void addStats(Fitness[] popFitness) {
        double total = 0;
        double bestSoFar = Double.MAX_VALUE;
        double temp;
        double n = 0;
        Individual bestIndSoFar = null;
        for (Fitness popFitnes : popFitness) {
            temp = popFitnes.getDouble();
            if (!Double.isNaN(temp) && !Double.isInfinite(temp) && temp != BasicFitness.DEFAULT_FITNESS) {
                total += temp;
                n++;
                if (temp < bestSoFar) {
                    bestSoFar = temp;
                    bestIndSoFar = popFitnes.getIndividual();
                }
            }
        }
        if (Double.isInfinite(total)) {
            total = Double.MAX_VALUE;
        }
        double mean = total / n;
        this.meanFitness.add(mean);
        this.bestFitness.add(bestSoFar);
        this.bestIndividualOfGeneration.add(bestIndSoFar);
        total = 0;
        double x;
        for (Fitness popFitnes : popFitness) {
            if (popFitnes.getIndividual().isValid()) {
                temp = popFitnes.getDouble();
                if (!Double.isNaN(temp) && !Double.isInfinite(temp) && temp != BasicFitness.DEFAULT_FITNESS) {
                    x = (temp - mean);
                    total += x * x;
                }
            }
        }
        double variance = total / n;
        this.varFitness.add(variance);
    }

    /**
     * Add length data
     * @param popLength population length
     */
    public void addLength(ArrayList<Integer> popLength) {
        Iterator<Integer> iterD = popLength.iterator();
        int total = 0;
        int longest = Integer.MIN_VALUE;
        int shortest = Integer.MAX_VALUE;
        int temp;
        while (iterD.hasNext()) {
            temp = iterD.next();
            total += temp;
            if (temp > longest) {
                longest = temp;
            }
            if (temp < shortest) {
                shortest = temp;
            }
        }
        int n = popLength.size();
        double mean = total / n;
        this.aveLength.add(mean);
        this.maxLength.add(longest);
        this.minLength.add(shortest);
        iterD = popLength.iterator();
        total = 0;
        double x;
        while (iterD.hasNext()) {
            x = (iterD.next() - mean);
            total += x * x;
        }
        double variance = total / n;
        this.varLength.add(variance);
    }

    /**
     * Add mean depth of the derivation trees in the population
     * @param mG fitness
     */
    public void addMeanDerivationTreeDepth(Fitness[] mG) {
        int total = 0;
        int temp;
        double n = 0;
        for (Fitness aMG : mG) {
            if (aMG.getIndividual().isValid()) {
                GEIndividual ind = (GEIndividual) aMG.getIndividual();
                if (ind.getMapper() == null) {
                    temp = 0;
                } else {
                    temp = ((GEGrammar) ind.getMapper()).getMaxCurrentTreeDepth();
                }
                total += temp;
                n++;
            }
        }
        double mean = total / n;
        this.meanDerivationTreeDepth.add(mean);
    }

    /**
     * Add mean used genes
     * @param mG fitness
     */
    public void addMeanUsedGenes(Fitness[] mG) {
        int total = 0;
        int temp;
        double n = 0;
        for (Fitness aMG : mG) {
            if (aMG.getIndividual().isValid()) {
                temp = ((GEIndividual) (aMG.getIndividual())).getUsedCodons();
                total += temp;
                n++;
            }
        }
        double mean = total / n;
        this.meanUsedGenes.add(mean);
    }

    /**
     * Add population to the statcatcher to work with. Calls many of the 
     * functions for adding statistics.
     * @param population incoming population
     */
    public void addStatsPop(ArrayList<Individual> population) {
        Fitness[] fits = new Fitness[population.size()];
        Fitness[] usedGenes = new Fitness[population.size()];
        Iterator<Individual> iterP = population.iterator();
        ArrayList<Double> allFit = new ArrayList<Double>(population.size());
        ArrayList<Integer> allLength = new ArrayList<Integer>(population.size());
        Individual i;
        int cnt = 0;
        int nr_invalids = 0;
        while (iterP.hasNext()) {
            i = iterP.next();
            fits[cnt] = i.getFitness();
            usedGenes[cnt] = i.getFitness();
            if (i.getGenotype() == null) {
                allLength.add(0);
            } else {
                allLength.add(i.getGenotype().get(0).getLength());
            }
            if (!i.isValid()) {
                nr_invalids++;
            }
            if (i.getAge() < 2) {
                allFit.add(i.getFitness().getDouble());
            }
            cnt++;
        }
        this.allFitness.add(allFit);
        this.invalids.add(nr_invalids);
        this.addLength(allLength);
        this.addStats(fits);
        this.addMeanUsedGenes(usedGenes);
        this.addMeanDerivationTreeDepth(usedGenes);
    }

    /**
     * Get best individual of generation
     * @return best individual of the latest generation
     */
    public Individual getBestIndividualOfGeneration() {
        return this.bestIndividualOfGeneration.get(this.bestIndividualOfGeneration.size() - 1);
    }

    /**
     * Get best individuals of all generations
     * @return best individuals of the generations
     */
    public ArrayList<Individual> getBestIndividualOfGenerations() {
        return this.bestIndividualOfGeneration;
    }

    /**
     * Get the mean max depth of the derivation trees in the population
     * @return
     */
    public ArrayList<Double> getMeanDerivationTreeDepth() {
        return meanDerivationTreeDepth;
    }

    /**
     * Get all fitness stored
     * @return all fitness stored
     */
    public ArrayList<ArrayList<Double>> getAllFitness() {
        return allFitness;
    }

    /**
     * Get mean used genes
     * @return mean used genes
     */
    public ArrayList<Double> getMeanUsedGenes() {
        return meanUsedGenes;
    }

    /**
     * Get the mean used genes for the latest generation
     * @return current mean used genes
     */
    public double getCurrentMeanUsedGenes() {
        return meanUsedGenes.get(this.meanUsedGenes.size() - 1);
    }

    /**
     * Get mean for the latest generation
     * @return current mean
     */
    public double getCurrentMean() {
        return meanFitness.get(this.meanFitness.size() - 1);
    }

    /**
     * Get number of invalid individuals
     * @return invalids
     */
    public ArrayList<Integer> getInvalids() {
        return invalids;
    }

    /**
     * Get the best fitness for the latest generation
     * @return current best fitness
     */
    public double getCurrentBestFitness() {
        return this.bestFitness.get(this.bestFitness.size() - 1);
    }

    /**
     * Get best fitness
     * @return best fitness
     */
    public ArrayList<Double> getBestFitness() {
        return bestFitness;
    }

    /**
     * Get variance of the fitness in the population
     * @return variance of fitness
     */
    public ArrayList<Double> getVarFitness() {
        return varFitness;
    }

    /**
     * Get mean fitness of the population
     * @return mean fitness
     */
    public ArrayList<Double> getMeanFitness() {
        return meanFitness;
    }

    /**
     * Get the average length
     * @return average length
     */
    public ArrayList<Double> getAveLength() {
        return aveLength;
    }

    /**
     * Get max length
     * @return max length
     */
    public ArrayList<Integer> getMaxLength() {
        return maxLength;
    }

    /**
     * Get min length
     * @return min length
     */
    public ArrayList<Integer> getMinLength() {
        return minLength;
    }

    /**
     * Get variance of the lengths in the population
     * @return variance of the lengths
     */
    public ArrayList<Double> getVarLength() {
        return varLength;
    }

    /**
     * Get the time for execution of a generation
     * @return time
     */
    public ArrayList<Long> getTime() {
        return this.time;
    }
}
