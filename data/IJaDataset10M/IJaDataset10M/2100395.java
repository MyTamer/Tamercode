package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.ScreenAnnotation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Retrieve the highest-resolution elevations available for the current elevation model, drawing them from the server if
 * necessary. Shift-click on the globe to retrieve the elevation of a location.
 * <p/>
 * Note: The {@link gov.nasa.worldwind.terrain.HighResolutionTerrain} class may be more appropriate to your needs than
 * this example.
 *
 * @author tag
 * @version $Id: GetBestElevations.java 1 2011-07-16 23:22:47Z dcollins $
 * @see gov.nasa.worldwind.terrain.HighResolutionTerrain
 */
public class GetBestElevations extends ApplicationTemplate {

    public static class AppFrame extends ApplicationTemplate.AppFrame {

        /**
         * Retrieve elevations for a specified list of locations. The elevations returned are the best currently
         * available for the dataset and the area bounding the locations. Since the necessary elevation data might not
         * be in memory at the time of the call, this method iterates until the necessary elevation data is in memory
         * and can be used to determine the locations' elevations.
         * <p/>
         * The locations must have a bounding sector, so more than one location is required. If the bounding region is
         * large, a huge amount of data must be retrieved from the server. Be aware that you are overriding the system's
         * resolution selection mechanism by requesting the highest resolution data, which could easily be gigabytes.
         * Requesting data for a large region will take a long time, will dump a lot of data into the local disk cache,
         * and may cause the server to throttle your access.
         *
         * @param locations a list of locations to determine elevations for
         *
         * @return the resolution actually achieved.
         */
        public double[] getBestElevations(List<LatLon> locations) {
            Globe globe = this.getWwd().getModel().getGlobe();
            Sector sector = Sector.boundingSector(locations);
            double[] elevations = new double[locations.size()];
            double targetResolution = globe.getElevationModel().getBestResolution(sector);
            double actualResolution = Double.MAX_VALUE;
            while (actualResolution > targetResolution) {
                actualResolution = globe.getElevations(sector, locations, targetResolution, elevations);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return elevations;
        }

        public AppFrame() {
            super(true, true, false);
            final ScreenAnnotation annotation = new ScreenAnnotation("Shift-click to select a location", new Point(100, 50));
            AnnotationLayer layer = new AnnotationLayer();
            layer.addAnnotation(annotation);
            insertBeforeCompass(this.getWwd(), layer);
            this.getWwd().getInputHandler().addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent mouseEvent) {
                    if ((mouseEvent.getModifiers() & ActionEvent.SHIFT_MASK) == 0) return;
                    mouseEvent.consume();
                    final Position pos = getWwd().getCurrentPosition();
                    if (pos == null) return;
                    annotation.setText(String.format("Elevation = "));
                    getWwd().redraw();
                    Thread t = new Thread(new Runnable() {

                        public void run() {
                            List<LatLon> locations = Arrays.asList(pos, pos.add(LatLon.fromDegrees(0.00001, 0.00001)));
                            final double[] elevations = getBestElevations(locations);
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    annotation.setText(String.format("Elevation = %d m", (int) elevations[0]));
                                    getWwd().redraw();
                                }
                            });
                        }
                    });
                    t.start();
                }

                public void mouseEntered(MouseEvent mouseEvent) {
                }

                public void mouseExited(MouseEvent mouseEvent) {
                }

                public void mousePressed(MouseEvent mouseEvent) {
                }

                public void mouseReleased(MouseEvent mouseEvent) {
                }
            });
        }
    }

    public static void main(String[] args) {
        ApplicationTemplate.start("World Wind Get Best Elevations", AppFrame.class);
    }
}
