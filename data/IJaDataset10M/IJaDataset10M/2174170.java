package ch.ethz.sg.cuttlefish.misc;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.ethz.sg.cuttlefish.networks.BrowsableNetwork;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class SVGExporter {

    private BrowsableNetwork network;

    private Layout<Vertex, Edge> layout;

    private boolean isDirected = false;

    private Map<Vertex, Integer> index = new HashMap<Vertex, Integer>();

    public SVGExporter(BrowsableNetwork network, Layout<Vertex, Edge> layout) {
        this.network = network;
        this.layout = layout;
        for (Edge e : network.getEdges()) {
            if (network.getSource(e) != null) isDirected = true;
        }
    }

    public void toSVG(File file, int height, int width) {
        try {
            String jsFilename = file.getAbsolutePath() + ".js";
            exportJScript(new File(jsFilename));
            PrintStream p = new PrintStream(file);
            p.println("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width='994px' height='751px' onload=\"init()\" ");
            p.println(" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
            p.println(" <script xlink:href=\"/javascript/svg_tooltip.js\" />");
            p.println(" <style>");
            p.println("    .caption{font-size: 14px;font-family: Georgia, serif;}");
            p.println("    .tooltip{font-size: 12px;}");
            p.println("    .tooltip_bg{fill: white; stroke: black; stroke-width: 1; opacity: 0.85;}");
            p.println(" </style>");
            p.println("  <svg:polyline points='0,0 " + width + ",0 " + width + "," + height + " 0," + height + "' style='stroke:black; fill:none;'/>");
            Map<Integer, Set<Color>> markers;
            Map<Color, Integer> colorId = null;
            if (isDirected) {
                markers = new HashMap<Integer, Set<Color>>();
                colorId = new HashMap<Color, Integer>();
                for (Edge e : network.getEdges()) {
                    Integer refX = (int) (network.getDest(e).getSize() * 8 / 10 + 10);
                    if (!markers.containsKey(refX)) {
                        markers.put(refX, new HashSet<Color>());
                    }
                    if (!colorId.containsKey(e.getColor())) {
                        colorId.put(e.getColor(), colorId.size());
                    }
                    if (!markers.get(refX).contains(e.getColor())) {
                        markers.get(refX).add(e.getColor());
                        String markerColor = "" + e.getColor().getRed() + ',' + e.getColor().getGreen() + ',' + e.getColor().getBlue();
                        String markerName = "arrow_" + (int) network.getDest(e).getSize() + '_' + colorId.get(e.getColor());
                        p.println("   <svg:marker id='" + markerName + "' viewBox='0 0 10 10' refX='" + refX + "' refY='5' markerUnits='userSpaceOnUse' markerWidth='12' markerHeight='12' orient='auto' fill='rgb(" + markerColor + ")'>");
                        p.println("      <svg:path d='M 0 0 L 10 5 L 0 10 z'></svg:path>");
                        p.println("   </svg:marker>");
                    }
                }
            }
            for (Edge e : network.getEdges()) {
                Vertex sourceNode, destNode;
                if (isDirected) {
                    sourceNode = network.getSource(e);
                    destNode = network.getDest(e);
                } else {
                    sourceNode = network.getEndpoints(e).getFirst();
                    destNode = network.getEndpoints(e).getSecond();
                }
                if (sourceNode == destNode) continue;
                int sourceId = index.get(sourceNode);
                int destId = index.get(destNode);
                String edgeName = "edge" + (sourceId < destId ? sourceId : destId) + (sourceId > destId ? sourceId : destId);
                int x1, y1, x2, y2;
                x1 = (int) layout.transform(sourceNode).getX();
                y1 = (int) layout.transform(sourceNode).getY();
                x2 = (int) layout.transform(destNode).getX();
                y2 = (int) layout.transform(destNode).getY();
                p.print("  <svg:line id='" + edgeName + "' x1='" + x1 + "' y1='" + y1 + "' x2='" + x2 + "' y2='" + y2 + "' ");
                if (isDirected) {
                    String markerName = "arrow_" + (int) network.getDest(e).getSize() + '_' + colorId.get(e.getColor());
                    p.print("marker-end='url(#" + markerName + ")' ");
                }
                p.println("style='stroke:rgb(" + e.getColor().getRed() + "," + e.getColor().getGreen() + "," + e.getColor().getBlue() + ");stroke-width:" + e.getWidth() + "'/>");
            }
            for (Vertex n : network.getVertices()) {
                p.println("  <svg:circle id='" + index.get(n) + "' r='" + n.getSize() + "' cx='" + (int) layout.transform(n).getX() + "' cy='" + (int) layout.transform(n).getY() + "' style='fill:rgb(" + n.getFillColor().getRed() + "," + n.getFillColor().getGreen() + "," + n.getFillColor().getBlue() + "); stroke:rgb(" + n.getColor().getRed() + "," + n.getColor().getGreen() + "," + n.getColor().getBlue() + "); stroke-width:" + n.getWidth() + ";'/>");
            }
            for (Vertex n : network.getVertices()) {
                p.println("  <svg:a xlink:href='#' target='_black'>");
                int x = (int) layout.transform(n).getX() + (int) n.getSize() + 2;
                int y = (int) layout.transform(n).getY() + (int) n.getSize() + 2;
                String label = "";
                if (n.getLabel() != null) label = n.getLabel();
                p.println("     <svg:text id='label" + index.get(n) + "' x='" + x + "' y='" + y + "' font-size='12' fill='black'>" + label + "</svg:text>");
                p.println("</svg:a>");
            }
            p.println("<rect class=\"tooltip_bg\" id=\"tooltip_bg\" x=\"0\" y=\"0\" rx=\"4\" ry=\"4\" width=\"55\" height=\"17\" visibility=\"hidden\"/>");
            p.println("<text class=\"tooltip\" id=\"tooltip\" x=\"0\" y=\"0\" visibility=\"hidden\">Tooltip</text>");
            p.println("</svg>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return;
    }

    public void exportJScript(File f) {
        PrintStream p = null;
        try {
            p = new PrintStream(f);
        } catch (FileNotFoundException e1) {
            System.out.println("Error when writing the java script file");
            e1.printStackTrace();
        }
        p.println("var dx,dy;\n  var nodes;\n  var labels;\n  var edges;\n  var selectedNode;");
        p.println("var svgElement;");
        p.println("  function init() {");
        p.println("    labels = new Array();");
        p.println("    edges = new Array();");
        p.println("    nodes = new Array();");
        p.println("var svgsource = document.getElementById('svgsource');");
        p.println("svgElement = getSubDocument(svgsource);");
        int i = 0;
        for (Vertex n : network.getVertices()) {
            index.put(n, i++);
        }
        p.println("    for (i=0;i<=20;i++){ ");
        p.println("             labels[i] = svgElement.getElementById('label' + i.toString())");
        p.println("     edges[i] = new Array();");
        p.println("     nodes[i] = svgElement.getElementById(i);        ");
        p.println("     nodes[i].addEventListener('mousedown', mousedown_listener, false);");
        p.println("     //nodes[i].setAttribute(\"onmousemove\", \"ShowTooltip(evt, 'The amazing blue box\" + i.toString() + \"')\");");
        p.println("     nodes[i].setAttribute(\"onmousemove\", \"ShowTooltip(evt, '\" + descriptions[i] + \"')\");");
        p.println("     nodes[i].setAttribute(\"onmouseout\", \"HideTooltip(evt)\");");
        p.println("    }");
        for (Vertex n : network.getVertices()) {
            p.println("    edges[" + index.get(n) + "] = new Array();");
        }
        for (Vertex n : network.getVertices()) {
            int count = 0;
            for (Edge e : network.getIncidentEdges(n)) {
                int sourceId, destId;
                if (isDirected) {
                    sourceId = index.get(network.getSource(e));
                    destId = index.get(network.getDest(e));
                } else {
                    sourceId = index.get(network.getEndpoints(e).getFirst());
                    destId = index.get(network.getEndpoints(e).getSecond());
                }
                if (sourceId == destId) continue;
                String edgeName = "edge" + (sourceId < destId ? sourceId : destId) + (sourceId > destId ? sourceId : destId);
                p.println("    edges[" + index.get(n) + "][" + count + "] = document.getElementById('" + edgeName + "');");
                count++;
            }
        }
        p.println("  }");
        p.println("  function mousedown_listener(evt)");
        p.println("  {");
        p.println("    selectedNode = evt.target;");
        p.println("    dx = selectedNode.cx.baseVal.value - evt.clientX;");
        p.println("    dy = selectedNode.cy.baseVal.value - evt.clientY;");
        p.println("    svgElement.addEventListener('mousemove', mousemove_listener, true);");
        p.println("    svgElement.addEventListener('mouseup', mouseup_listener, true);");
        p.println("  }");
        p.println("  function mouseup_listener(evt)");
        p.println("  {");
        p.println("    svgElement.removeEventListener('mousemove', mousemove_listener, true);");
        p.println("    svgElement.removeEventListener('mouseup', mouseup_listener, true);");
        p.println("  }");
        p.println("  function mousemove_listener(evt)");
        p.println("  {");
        p.println("    var id = selectedNode.ownerSVGElement.suspendRedraw(1000);");
        p.println("    for(i in edges[selectedNode.id]) {");
        p.println("      var edge = edges[selectedNode.id][i];");
        p.println("      if(edge.x1.baseVal.value == selectedNode.cx.baseVal.value");
        p.println("          && edge.y1.baseVal.value == selectedNode.cy.baseVal.value ) {");
        p.println("        edge.x1.baseVal.value = evt.clientX + dx;");
        p.println("        edge.y1.baseVal.value = evt.clientY + dy;");
        p.println("      } else {");
        p.println("        edge.x2.baseVal.value = evt.clientX + dx;");
        p.println("        edge.y2.baseVal.value = evt.clientY + dy;");
        p.println("      }");
        p.println("    }");
        p.println("    selectedNode.cx.baseVal.value = evt.clientX + dx;");
        p.println("    selectedNode.cy.baseVal.value = evt.clientY + dy;");
        p.println("    labels[selectedNode.id].setAttribute('x', selectedNode.cx.baseVal.value + selectedNode.r.baseVal.value + 2);");
        p.println("    labels[selectedNode.id].setAttribute('y', selectedNode.cy.baseVal.value + selectedNode.r.baseVal.value + 2);");
        p.println("    selectedNode.ownerSVGElement.unsuspendRedraw(id);");
        p.println("  }");
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
