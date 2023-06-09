package org.sf.xrime.algorithms.statistics.egoCentric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.sf.xrime.model.edge.AdjVertexEdge;
import org.sf.xrime.model.edge.Edge;
import org.sf.xrime.model.vertex.AdjSetVertex;

/**
 * Emit <A, <B->C>>.
 * @author Cai Bin.
 */
public class EgoCentricReducer extends MapReduceBase implements Reducer<Text, AdjSetVertex, Text, Edge> {

    Text resultKey = new Text();

    Edge resultValue = new Edge();

    @Override
    public void reduce(Text key, Iterator<AdjSetVertex> values, OutputCollector<Text, Edge> output, Reporter reporter) throws IOException {
        List<AdjSetVertex> inputList = new ArrayList<AdjSetVertex>();
        while (values.hasNext()) {
            inputList.add((AdjSetVertex) values.next().clone());
        }
        for (int ii = 0; ii < inputList.size(); ii++) {
            String middle = inputList.get(ii).getId();
            for (int jj = 0; jj < inputList.size(); jj++) {
                Set<AdjVertexEdge> tos = inputList.get(jj).getOpposites();
                for (AdjVertexEdge to : tos) {
                    if (to.getOpposite().compareTo(middle) == 0) {
                        resultValue.setFrom(middle);
                        resultValue.setTo(key.toString());
                        resultKey.set(inputList.get(jj).getId());
                        output.collect(resultKey, resultValue);
                    }
                }
            }
        }
    }
}
