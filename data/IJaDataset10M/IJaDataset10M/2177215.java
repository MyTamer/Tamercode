package Autocatalysis;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.valueLayer.GridValueLayer;

/**
 * Model of a grid of interconnected chemical reactors with cubic 
 * autocatalysis kinetics as described in 
 * 
 * Tatara, E., I. Birol, F. Teymour, and A. Cinar, Static and Dynamic Behavior
 *   of Autocatalytic Replicators in Reactor Networks, Industrial and Engineering
 *   Chemistry Research, 43, 3972-3993, 2004.
 * 
 * @author Eric Tatara
 *
 */
public class AutoCatContextBuilder implements ContextBuilder<Reactor> {

    public Context<Reactor> build(Context<Reactor> context) {
        Parameters parm = RunEnvironment.getInstance().getParameters();
        int gridWidth = (Integer) parm.getValue("gridWidth");
        int gridHeight = (Integer) parm.getValue("gridHeight");
        Grid<Reactor> grid = GridFactoryFinder.createGridFactory(null).createGrid("Grid", context, new GridBuilderParameters<Reactor>(new WrapAroundBorders(), new RandomGridAdder<Reactor>(), false, gridWidth, gridHeight));
        GridValueLayer resourceLayer = new GridValueLayer("Resource", true, new WrapAroundBorders(), gridWidth, gridHeight);
        GridValueLayer speciesLayer = new GridValueLayer("Species", true, new WrapAroundBorders(), gridWidth, gridHeight);
        context.addValueLayer(resourceLayer);
        context.addValueLayer(speciesLayer);
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Reactor cell = new Reactor();
                context.add(cell);
                grid.moveTo(cell, i, j);
            }
        }
        grid.getObjectAt((int) (gridWidth / 2), (int) (gridHeight / 2)).setSpecies(0.25);
        return context;
    }
}
