package org.sf.xrime.algorithms.layout.gfr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.Transformer;
import org.sf.xrime.algorithms.GraphAlgorithm;
import org.sf.xrime.algorithms.layout.ConstantLabels;
import org.sf.xrime.algorithms.layout.ellipse.CoordinatesCalculation;
import org.sf.xrime.algorithms.layout.ellipse.SequentialNumAssign;
import org.sf.xrime.model.Graph;
import org.sf.xrime.postprocessing.SequenceFileToTextFileTransformer;
import org.sf.xrime.utils.MRConsoleReader;
import org.sf.xrime.utils.SequenceTempDirMgr;

/**
 * Input is raw graph in the form of AdjSetVertex.
 * Output is the same graph in the form of textified LabeledAdjSetVertex, in which
 * coordinates for each vertex is specified as labels.
 * @author xue
 */
public class GridVariantFRAlgorithm2 extends GraphAlgorithm {

    /**
   * Default constructor.
   */
    public GridVariantFRAlgorithm2() {
        super();
    }

    @Override
    public void setArguments(String[] params) throws ProcessorExecutionException {
        List<String> other_args = new ArrayList<String>();
        int max_x = 0;
        int max_y = 0;
        int iterations = 0;
        int temperature = 0;
        for (int i = 0; i < params.length; ++i) {
            try {
                if ("-x".equals(params[i])) {
                    max_x = Integer.parseInt(params[++i]);
                } else if ("-y".equals(params[i])) {
                    max_y = Integer.parseInt(params[++i]);
                } else if ("-it".equals(params[i])) {
                    iterations = Integer.parseInt(params[++i]);
                } else if ("-tem".equals(params[i])) {
                    temperature = Integer.parseInt(params[++i]);
                } else {
                    other_args.add(params[i]);
                }
            } catch (NumberFormatException except) {
                throw new ProcessorExecutionException(except);
            } catch (ArrayIndexOutOfBoundsException except) {
                throw new ProcessorExecutionException(except);
            }
        }
        if (max_x == 0 || max_y == 0 || iterations == 0) {
            throw new ProcessorExecutionException("Should specify the size of display frame and iteration times");
        }
        if (other_args.size() != 2) {
            throw new ProcessorExecutionException("Wrong number of parameters: " + other_args.size() + " instead of 2.");
        }
        Graph src = new Graph(Graph.defaultGraph());
        src.setPath(new Path(other_args.get(0)));
        Graph dest = new Graph(Graph.defaultGraph());
        dest.setPath(new Path(other_args.get(1)));
        setSource(src);
        setDestination(dest);
        setParameter(ConstantLabels.MAX_X_COORDINATE, "" + max_x);
        setParameter(ConstantLabels.MAX_Y_COORDINATE, "" + max_y);
        setParameter(ConstantLabels.ITERATIONS, "" + iterations);
        if (temperature != 0) {
            setParameter(ConstantLabels.TEMPERATURE, "" + temperature);
        }
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        try {
            if (getSource().getPaths() == null || getSource().getPaths().size() == 0 || getDestination().getPaths() == null || getDestination().getPaths().size() == 0) {
                throw new ProcessorExecutionException("No input and/or output paths specified.");
            }
            String temp_dir_prefix = getDestination().getPath().getParent().toString() + "/gfr_" + getDestination().getPath().getName() + "_";
            SequenceTempDirMgr dirMgr = new SequenceTempDirMgr(temp_dir_prefix, context);
            dirMgr.setSeqNum(0);
            Path tmpDir;
            int iterations = Integer.parseInt(context.getParameter(ConstantLabels.ITERATIONS));
            int max_x = Integer.parseInt(context.getParameter(ConstantLabels.MAX_X_COORDINATE));
            int max_y = Integer.parseInt(context.getParameter(ConstantLabels.MAX_Y_COORDINATE));
            int temperature = 0;
            if (context.getParameter(ConstantLabels.TEMPERATURE) != null) {
                temperature = Integer.parseInt(context.getParameter(ConstantLabels.TEMPERATURE));
            }
            Graph src;
            Graph dest;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": SequentialNumAssign");
            src = new Graph(Graph.defaultGraph());
            src.setPath(getSource().getPath());
            tmpDir = dirMgr.getTempDir();
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(tmpDir);
            GraphAlgorithm gen_seq_num = new SequentialNumAssign();
            gen_seq_num.setConf(context);
            gen_seq_num.setSource(src);
            gen_seq_num.setDestination(dest);
            gen_seq_num.setMapperNum(getMapperNum());
            gen_seq_num.setReducerNum(getReducerNum());
            gen_seq_num.execute();
            int num_of_vertexes = (int) MRConsoleReader.getReduceOutputRecordNum(gen_seq_num.getFinalStatus());
            System.out.println("" + max_x + " : " + max_y + " : " + num_of_vertexes);
            int x_disp = (max_x / ((int) Math.sqrt(num_of_vertexes))) / 2;
            int y_disp = (max_y / ((int) Math.sqrt(num_of_vertexes))) / 2;
            max_x = max_x - x_disp * 2;
            max_y = max_y - y_disp * 2;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": CoordinatesCalculation");
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            tmpDir = dirMgr.getTempDir();
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(tmpDir);
            GraphAlgorithm xy_calc = new CoordinatesCalculation();
            xy_calc.setConf(context);
            xy_calc.setParameter(ConstantLabels.MAX_X_COORDINATE, "" + max_x);
            xy_calc.setParameter(ConstantLabels.MAX_Y_COORDINATE, "" + max_y);
            xy_calc.setParameter(ConstantLabels.NUM_OF_VERTEXES, "" + num_of_vertexes);
            xy_calc.setSource(src);
            xy_calc.setDestination(dest);
            xy_calc.setMapperNum(getMapperNum());
            xy_calc.setReducerNum(getReducerNum());
            xy_calc.execute();
            double k = UtilityFunctions.k(max_x, max_y, num_of_vertexes);
            System.out.println("++++++> k = " + k);
            int max_grid_x_index = UtilityFunctions.grid_x_index(max_x - 1, k);
            int max_grid_y_index = UtilityFunctions.grid_y_index(max_y - 1, k);
            System.out.println("++++++> max_grid_x_index = " + max_grid_x_index);
            System.out.println("++++++> max_grid_y_index = " + max_grid_y_index);
            if (temperature == 0) {
                temperature = max_x > max_y ? max_y / 10 : max_x / 10;
            }
            for (int i = 0; i < iterations; i++) {
                System.out.println("++++++> iteration " + i + " started with temperature = " + temperature);
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": ReplusiveForceDisp");
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                GraphAlgorithm repel_force = new RepulsiveForceDisp();
                repel_force.setConf(context);
                repel_force.setParameter(ConstantLabels.MAX_X_COORDINATE, "" + max_x);
                repel_force.setParameter(ConstantLabels.MAX_Y_COORDINATE, "" + max_y);
                repel_force.setParameter(ConstantLabels.NUM_OF_VERTEXES, "" + num_of_vertexes);
                repel_force.setSource(src);
                repel_force.setDestination(dest);
                repel_force.setMapperNum(getMapperNum());
                repel_force.setReducerNum(getReducerNum());
                repel_force.execute();
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": AttractiveForceDisp");
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                GraphAlgorithm attr_force = new AttractiveForceDisp();
                attr_force.setConf(context);
                attr_force.setParameter(ConstantLabels.MAX_X_COORDINATE, "" + max_x);
                attr_force.setParameter(ConstantLabels.MAX_Y_COORDINATE, "" + max_y);
                attr_force.setParameter(ConstantLabels.NUM_OF_VERTEXES, "" + num_of_vertexes);
                attr_force.setSource(src);
                attr_force.setDestination(dest);
                attr_force.setMapperNum(getMapperNum());
                attr_force.setReducerNum(getReducerNum());
                attr_force.execute();
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": DisplacementSummarize");
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                GraphAlgorithm disp_sum = new DisplacementSummarize();
                disp_sum.setConf(context);
                disp_sum.setParameter(ConstantLabels.MAX_X_COORDINATE, "" + max_x);
                disp_sum.setParameter(ConstantLabels.MAX_Y_COORDINATE, "" + max_y);
                disp_sum.setParameter(ConstantLabels.TEMPERATURE, "" + temperature);
                disp_sum.setSource(src);
                disp_sum.setDestination(dest);
                disp_sum.setMapperNum(getMapperNum());
                disp_sum.setReducerNum(getReducerNum());
                disp_sum.execute();
                System.out.println("++++++> iteration " + i + " finished.");
                temperature = UtilityFunctions.cool(temperature, k, iterations - i - 1);
            }
            System.out.println("++++++>" + dirMgr.getSeqNum() + " CoordinatesAdjust");
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            tmpDir = dirMgr.getTempDir();
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(tmpDir);
            GraphAlgorithm xy_adjust = new CoordinatesAdjust();
            xy_adjust.setConf(context);
            xy_adjust.setParameter(ConstantLabels.X_DISP, "" + x_disp);
            xy_adjust.setParameter(ConstantLabels.Y_DISP, "" + y_disp);
            xy_adjust.setSource(src);
            xy_adjust.setDestination(dest);
            xy_adjust.setMapperNum(getMapperNum());
            xy_adjust.setReducerNum(getReducerNum());
            xy_adjust.execute();
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": Textify the result.");
            Transformer textifier = new SequenceFileToTextFileTransformer();
            textifier.setConf(context);
            textifier.setSrcPath(tmpDir);
            textifier.setDestPath(getDestination().getPath());
            textifier.setMapperNum(getMapperNum());
            textifier.setReducerNum(getReducerNum());
            textifier.execute();
            dirMgr.deleteAll();
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        } catch (IllegalAccessException e) {
            throw new ProcessorExecutionException(e);
        }
    }

    public static void main(String[] args) {
        try {
            int res = ToolRunner.run(new GridVariantFRAlgorithm2(), args);
            System.exit(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
