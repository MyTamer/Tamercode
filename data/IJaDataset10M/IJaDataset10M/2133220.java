package org.sf.xrime.algorithms.clique.maximal;

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.algorithms.GraphAlgorithm;
import org.sf.xrime.model.Graph;
import org.sf.xrime.utils.SequenceTempDirMgr;

/**
 * This algorithm is used to calculate all maximal weak cliques in a graph (not 
 * necessarily connected). The input is in the form of outgoing adjacency lists.
 * @author xue
 *
 */
public class MaximalWeakCliqueAlgorithm extends GraphAlgorithm {

    /**
   * Default constructor.
   */
    public MaximalWeakCliqueAlgorithm() {
        super();
    }

    @Override
    public void setArguments(String[] params) throws ProcessorExecutionException {
        if (params.length != 2) {
            throw new ProcessorExecutionException("Wrong number of parameters: " + params.length + " instead of 2.");
        }
        Graph src = new Graph(Graph.defaultGraph());
        src.setPath(new Path(params[0]));
        Graph dest = new Graph(Graph.defaultGraph());
        dest.setPath(new Path(params[1]));
        setSource(src);
        setDestination(dest);
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        try {
            if (getSource().getPaths() == null || getSource().getPaths().size() == 0 || getDestination().getPaths() == null || getDestination().getPaths().size() == 0) {
                throw new ProcessorExecutionException("No input and/or output paths specified.");
            }
            String temp_dir_prefix = getDestination().getPath().getParent().toString() + "/mwc_" + System.currentTimeMillis() + "_" + getDestination().getPath().getName() + "_";
            SequenceTempDirMgr dirMgr = new SequenceTempDirMgr(temp_dir_prefix, context);
            dirMgr.setSeqNum(0);
            Path tmpDir;
            Graph src;
            Graph dest;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": WeakNeighborhoodGenerate");
            src = new Graph(Graph.defaultGraph());
            src.setPath(getSource().getPath());
            dest = new Graph(Graph.defaultGraph());
            tmpDir = dirMgr.getTempDir();
            dest.setPath(tmpDir);
            GraphAlgorithm gen_neighbor = new WeakNeighborhoodGenerate();
            gen_neighbor.setConf(context);
            gen_neighbor.setSource(src);
            gen_neighbor.setDestination(dest);
            gen_neighbor.setMapperNum(getMapperNum());
            gen_neighbor.setReducerNum(getReducerNum());
            gen_neighbor.execute();
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": InducedNeighborhoodGenerate");
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            dest = new Graph(Graph.defaultGraph());
            tmpDir = dirMgr.getTempDir();
            dest.setPath(tmpDir);
            GraphAlgorithm gen_induced_nh = new InducedNeighborhoodGenerate();
            gen_induced_nh.setConf(context);
            gen_induced_nh.setSource(src);
            gen_induced_nh.setDestination(dest);
            gen_induced_nh.setMapperNum(getMapperNum());
            gen_induced_nh.setReducerNum(getReducerNum());
            gen_induced_nh.execute();
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": AllMaximalCliquesGenerate");
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(getDestination().getPath());
            GraphAlgorithm all_cliques = new AllMaximalCliquesGenerate();
            all_cliques.setConf(context);
            all_cliques.setSource(src);
            all_cliques.setDestination(dest);
            all_cliques.setMapperNum(getMapperNum());
            all_cliques.setReducerNum(getReducerNum());
            all_cliques.execute();
            dirMgr.deleteAll();
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        } catch (IllegalAccessException e) {
            throw new ProcessorExecutionException(e);
        }
    }

    public static void main(String[] args) {
        try {
            int res = ToolRunner.run(new MaximalWeakCliqueAlgorithm(), args);
            System.exit(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
