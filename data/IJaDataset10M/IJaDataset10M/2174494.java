package org.dllearner.tools.ore.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import javax.swing.JPanel;
import org.dllearner.core.EvaluatedDescription;
import org.dllearner.core.owl.Individual;
import org.dllearner.core.owl.NamedClass;
import org.dllearner.learningproblems.EvaluatedDescriptionClass;
import org.dllearner.tools.ore.LearningManager;
import org.dllearner.tools.ore.ui.rendering.ManchesterSyntaxRenderer;
import org.dllearner.tools.protege.IndividualPoint;

/**
 * This class draws the graphical coverage of a learned concept.
 * 
 * @author Christian Koetteritzsch
 * 
 */
public class GraphicalCoveragePanel extends JPanel implements MouseMotionListener {

    private static final long serialVersionUID = 855436961912515267L;

    private static final int HEIGHT = 200;

    private static final int WIDTH = 200;

    private static final int ELLIPSE_X_AXIS = 30;

    private static final int ELLIPSE_Y_AXIS = 30;

    private static final int MAX_NUMBER_OF_INDIVIDUAL_POINTS = 20;

    private static final int SUBSTRING_SIZE = 25;

    private static final int SPACE_SIZE = 7;

    private static final int MAX_RANDOM_NUMBER = 300;

    private Ellipse2D oldConcept;

    private Ellipse2D newConcept;

    private EvaluatedDescription eval;

    private NamedClass concept;

    private String baseURI;

    private Map<String, String> prefixes;

    private String conceptNew;

    private final Vector<IndividualPoint> posCovIndVector;

    private final Vector<IndividualPoint> posNotCovIndVector;

    private final Vector<IndividualPoint> additionalIndividuals;

    private final Vector<IndividualPoint> points;

    private final Vector<String> conceptVector;

    private int shiftOldConcept;

    private int shiftNewConcept;

    private int shiftNewConceptX;

    private int shiftCovered;

    private int coveredIndividualSize;

    private int additionalIndividualSize;

    private int x1;

    private int x2;

    private int y1;

    private int y2;

    private final Random random;

    private final Color darkGreen;

    private final Color darkRed;

    private String coverageString = "";

    private String coversAdditionalString = "";

    /**
	 * 
	 * This is the constructor for the GraphicalCoveragePanel.
	 * 
	 * @param desc
	 *            EvaluatedDescription
	 * @param m
	 *            DLLearnerModel
	 * @param concept
	 *            String
	 * @param p
	 *            MoreDetailForSuggestedConceptsPanel
	 */
    public GraphicalCoveragePanel(String concept) {
        this.setPreferredSize(new Dimension(640, 260));
        darkGreen = new Color(0, 100, 0);
        darkRed = new Color(205, 0, 0);
        random = new Random();
        conceptNew = concept;
        conceptVector = new Vector<String>();
        posCovIndVector = new Vector<IndividualPoint>();
        posNotCovIndVector = new Vector<IndividualPoint>();
        additionalIndividuals = new Vector<IndividualPoint>();
        points = new Vector<IndividualPoint>();
        oldConcept = new Ellipse2D.Double(ELLIPSE_X_AXIS, ELLIPSE_Y_AXIS, WIDTH, HEIGHT);
        newConcept = new Ellipse2D.Double(0, 0, 0, 0);
        addMouseMotionListener(this);
    }

    public void initManchesterSyntax(String baseURI, Map<String, String> prefixes) {
        this.baseURI = baseURI;
        this.prefixes = prefixes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, 400, 400);
        g.clearRect(320, 130, 320, 50);
        Graphics2D g2D;
        g2D = (Graphics2D) g;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2D.setColor(Color.BLACK);
        if (concept == null && LearningManager.getInstance().getCurrentClass2Describe() != null) {
            g2D.drawString(ManchesterSyntaxRenderer.renderSimple(LearningManager.getInstance().getCurrentClass2Describe()), 320, 10);
        } else if (concept != null) {
            g2D.drawString(concept.toManchesterSyntaxString(baseURI, prefixes), 320, 10);
        }
        g2D.setColor(Color.black);
        int p = 30;
        g2D.setColor(darkGreen);
        Ellipse2D circlePoint = new Ellipse2D.Double(315 - 1, p - 6, 4, 4);
        g2D.fill(circlePoint);
        g2D.setColor(Color.BLACK);
        g2D.drawString("individuals covered by", 320, p);
        g2D.setColor(Color.ORANGE);
        g2D.fillOval(460, p - 9, 9, 9);
        g2D.setColor(Color.BLACK);
        g2D.drawString("and", 475, p);
        g2D.setColor(Color.YELLOW);
        g2D.fillOval(505, p - 9, 9, 9);
        g2D.setColor(Color.BLACK);
        p = p + 20;
        g2D.drawString("(OK)", 320, p);
        p = p + 20;
        g2D.setColor(darkRed);
        Ellipse2D circlePoint2 = new Ellipse2D.Double(315 - 1, p - 6, 4, 4);
        g2D.fill(circlePoint2);
        g2D.setColor(Color.BLACK);
        g2D.drawString("individuals covered by", 320, p);
        g2D.setColor(Color.ORANGE);
        g2D.fillOval(460, p - 9, 9, 9);
        g2D.setColor(Color.BLACK);
        p = p + 20;
        g2D.drawString("(potential problem)", 320, p);
        p = p + 20;
        g2D.setColor(darkRed);
        Ellipse2D circlePoint3 = new Ellipse2D.Double(315 - 1, p - 6, 4, 4);
        g2D.fill(circlePoint3);
        g2D.setColor(Color.BLACK);
        g2D.drawString("individuals covered by", 320, p);
        g2D.setColor(Color.YELLOW);
        g2D.fillOval(460, p - 9, 9, 9);
        g2D.setColor(Color.BLACK);
        p = p + 20;
        g2D.drawString("(potential problem)", 320, p);
        p = p + 20;
        g2D.drawString(coverageString, 320, p);
        p = p + 20;
        g2D.drawString(coversAdditionalString, 320, p);
        g2D.setColor(Color.YELLOW);
        g2D.fill(oldConcept);
        g2D.fillOval(310, 0, 9, 9);
        g2D.setColor(Color.ORANGE);
        g2D.setComposite(ac);
        g2D.fill(newConcept);
        g2D.setColor(Color.BLACK);
        for (int i = 0; i < posCovIndVector.size(); i++) {
            g2D.setColor(darkGreen);
            g2D.fill(posCovIndVector.get(i).getIndividualPoint());
        }
        for (int i = 0; i < posNotCovIndVector.size(); i++) {
            g2D.setColor(darkRed);
            g2D.fill(posNotCovIndVector.get(i).getIndividualPoint());
        }
        for (int i = 0; i < additionalIndividuals.size(); i++) {
            g2D.setColor(Color.BLACK);
            g2D.fill(additionalIndividuals.get(i).getIndividualPoint());
        }
    }

    @SuppressWarnings(value = { "unused" })
    private void renderPlus() {
        if (eval != null) {
            coveredIndividualSize = ((EvaluatedDescriptionClass) eval).getCoveredInstances().size();
            double newConcepts = ((EvaluatedDescriptionClass) eval).getAddition();
            double oldConcepts = ((EvaluatedDescriptionClass) eval).getCoverage();
            shiftNewConcept = 0;
            shiftOldConcept = 0;
            shiftNewConceptX = 0;
            shiftCovered = 0;
            if (coveredIndividualSize == 0) {
                shiftNewConcept = (int) Math.round(((WIDTH) / 2.0) * newConcepts);
            } else if (additionalIndividualSize != coveredIndividualSize) {
                shiftNewConcept = (int) Math.round(((WIDTH) / 2.0) * (1.0 + (1.0 - oldConcepts)));
                shiftOldConcept = (int) Math.round(((WIDTH) / 2.0) * oldConcepts);
                shiftCovered = (int) Math.round(((WIDTH) / 2.0) * (1 - oldConcepts));
            }
            if (((EvaluatedDescriptionClass) eval).getAddition() != 1.0 && ((EvaluatedDescriptionClass) eval).getCoverage() == 1.0) {
                shiftCovered = (int) Math.round(((WIDTH) / 2.0) * 0.625);
                shiftNewConceptX = shiftCovered;
                shiftNewConcept = 2 * shiftNewConceptX;
            }
        }
        int i = conceptNew.length();
        while (i > 0) {
            int sub = 0;
            String subString = "";
            if (conceptNew.contains(" ")) {
                sub = conceptNew.indexOf(" ");
                subString = conceptNew.substring(0, sub) + " ";
                conceptNew = conceptNew.replace(conceptNew.substring(0, sub + 1), "");
            } else {
                subString = conceptNew;
                conceptNew = "";
            }
            while (sub < SUBSTRING_SIZE) {
                if (conceptNew.length() > 0 && conceptNew.contains(" ")) {
                    sub = conceptNew.indexOf(" ");
                    if (subString.length() + sub < SUBSTRING_SIZE) {
                        subString = subString + conceptNew.substring(0, sub) + " ";
                        conceptNew = conceptNew.replace(conceptNew.substring(0, sub + 1), "");
                        sub = subString.length();
                    } else {
                        break;
                    }
                } else {
                    if (subString.length() + conceptNew.length() > SUBSTRING_SIZE + SPACE_SIZE) {
                        conceptVector.add(subString);
                        subString = conceptNew;
                        conceptNew = "";
                        break;
                    } else {
                        subString = subString + conceptNew;
                        conceptNew = "";
                        break;
                    }
                }
            }
            conceptVector.add(subString);
            i = conceptNew.length();
        }
    }

    public void setNewClassDescription(EvaluatedDescription desc) {
        this.eval = desc;
        boolean hasAdditional = !((EvaluatedDescriptionClass) eval).getAdditionalInstances().isEmpty();
        boolean allPosCovered = ((EvaluatedDescriptionClass) eval).getCoverage() == 1.0;
        if (allPosCovered && hasAdditional) {
            newConcept = new Ellipse2D.Double(ELLIPSE_X_AXIS - 25, ELLIPSE_Y_AXIS - 25, WIDTH + 50, HEIGHT + 50);
        } else if (!allPosCovered && hasAdditional) {
            newConcept = new Ellipse2D.Double(ELLIPSE_X_AXIS + 20, ELLIPSE_Y_AXIS, WIDTH, HEIGHT);
        } else if (allPosCovered && !hasAdditional) {
            newConcept = new Ellipse2D.Double(ELLIPSE_X_AXIS, ELLIPSE_Y_AXIS, WIDTH, HEIGHT);
        } else if (!allPosCovered && !hasAdditional) {
            newConcept = new Ellipse2D.Double(ELLIPSE_X_AXIS + 20, ELLIPSE_Y_AXIS, WIDTH - 20, HEIGHT);
        }
        computeIndividualPoints();
        int coveredInstanceCount = ((EvaluatedDescriptionClass) eval).getCoveredInstances().size();
        int instanceCount = coveredInstanceCount + ((EvaluatedDescriptionClass) eval).getNotCoveredInstances().size();
        int coverage = (int) (((EvaluatedDescriptionClass) eval).getCoverage() * 100);
        int additionalCount = ((EvaluatedDescriptionClass) eval).getAdditionalInstances().size();
        coverageString = "Covers " + coveredInstanceCount + " of " + instanceCount + "(" + coverage + " %) of all instances.";
        coversAdditionalString = "Covers " + additionalCount + " additional instances.";
        getParent().repaint();
    }

    public void setConcept(NamedClass nc) {
        concept = nc;
        getParent().repaint();
    }

    public void clear() {
        newConcept = new Ellipse2D.Double(0, 0, 0, 0);
        posCovIndVector.clear();
        posNotCovIndVector.clear();
        additionalIndividuals.clear();
        points.clear();
        coverageString = "";
        coversAdditionalString = "";
        getParent().repaint();
    }

    public void computeIndividualPoints() {
        posCovIndVector.clear();
        posNotCovIndVector.clear();
        additionalIndividuals.clear();
        points.clear();
        if (eval != null) {
            Set<Individual> posInd = ((EvaluatedDescriptionClass) eval).getCoveredInstances();
            int i = 0;
            double x = random.nextInt(MAX_RANDOM_NUMBER);
            double y = random.nextInt(MAX_RANDOM_NUMBER);
            boolean flag = true;
            for (Individual ind : posInd) {
                flag = true;
                if (i < MAX_NUMBER_OF_INDIVIDUAL_POINTS) {
                    while (flag) {
                        if (newConcept.contains(x, y) && oldConcept.contains(x, y) && !(x >= this.getX1() + this.getShiftCovered() && x <= this.getX2() + this.getShiftCovered() && y >= this.getY1() && y <= this.getY2())) {
                            posCovIndVector.add(new IndividualPoint("*", (int) x, (int) y, ind.toString(), ind, ""));
                            i++;
                            flag = false;
                            x = random.nextInt(MAX_RANDOM_NUMBER);
                            y = random.nextInt(MAX_RANDOM_NUMBER);
                            break;
                        } else {
                            x = random.nextInt(MAX_RANDOM_NUMBER);
                            y = random.nextInt(MAX_RANDOM_NUMBER);
                        }
                    }
                }
            }
            Set<Individual> posNotCovInd = ((EvaluatedDescriptionClass) eval).getAdditionalInstances();
            int j = 0;
            x = random.nextInt(MAX_RANDOM_NUMBER);
            y = random.nextInt(MAX_RANDOM_NUMBER);
            for (Individual ind : posNotCovInd) {
                flag = true;
                if (j < MAX_NUMBER_OF_INDIVIDUAL_POINTS) {
                    while (flag) {
                        if (!oldConcept.contains(x, y) && newConcept.contains(x, y) && !(x >= this.getX1() + this.getShiftNewConcept() && x <= this.getX2() + this.getShiftNewConcept() && y >= this.getY1() && y <= this.getY2()) && !(x >= this.getX1() + this.getShiftNewConceptX() && x <= this.getX2() + this.getShiftNewConceptX() && y >= this.getY1() + this.getShiftNewConcept() && y <= this.getY2() + this.getShiftNewConcept())) {
                            posNotCovIndVector.add(new IndividualPoint("*", (int) x, (int) y, ind.toString(), ind, ""));
                            j++;
                            flag = false;
                            x = random.nextInt(MAX_RANDOM_NUMBER);
                            y = random.nextInt(MAX_RANDOM_NUMBER);
                            break;
                        } else {
                            x = random.nextInt(MAX_RANDOM_NUMBER);
                            y = random.nextInt(MAX_RANDOM_NUMBER);
                        }
                    }
                }
            }
            Set<Individual> notCovInd = ((EvaluatedDescriptionClass) eval).getNotCoveredInstances();
            int k = 0;
            x = random.nextInt(MAX_RANDOM_NUMBER);
            y = random.nextInt(MAX_RANDOM_NUMBER);
            for (Individual ind : notCovInd) {
                flag = true;
                if (k < MAX_NUMBER_OF_INDIVIDUAL_POINTS) {
                    while (flag) {
                        if (oldConcept.contains(x, y) && !newConcept.contains(x, y) && !(x >= this.getX1() - this.getShiftOldConcept() && x <= this.getX2() - this.getShiftOldConcept() && y >= this.getY1() && y <= this.getY2())) {
                            posNotCovIndVector.add(new IndividualPoint("*", (int) x, (int) y, ind.toString(), ind, ""));
                            k++;
                            flag = false;
                            x = random.nextInt(MAX_RANDOM_NUMBER);
                            y = random.nextInt(MAX_RANDOM_NUMBER);
                            break;
                        } else {
                            x = random.nextInt(MAX_RANDOM_NUMBER);
                            y = random.nextInt(MAX_RANDOM_NUMBER);
                        }
                    }
                }
            }
            points.addAll(posCovIndVector);
            points.addAll(posNotCovIndVector);
            points.addAll(additionalIndividuals);
        }
    }

    /**
	 * This method returns a Vector of all individuals that are drawn in the
	 * panel.
	 * 
	 * @return Vector of Individuals
	 */
    public Vector<IndividualPoint> getIndividualVector() {
        return points;
    }

    /**
	 * This method returns the GraphicalCoveragePanel.
	 * 
	 * @return GraphicalCoveragePanel
	 */
    public GraphicalCoveragePanel getGraphicalCoveragePanel() {
        return this;
    }

    /**
	 * Returns the min. x value of the plus.
	 * 
	 * @return int min X Value
	 */
    public int getX1() {
        return x1;
    }

    /**
	 * Returns the max. x value of the plus.
	 * 
	 * @return int max X Value
	 */
    public int getX2() {
        return x2;
    }

    /**
	 * Returns the min. y value of the plus.
	 * 
	 * @return int min Y Value
	 */
    public int getY1() {
        return y1;
    }

    /**
	 * Returns the max. y value of the plus.
	 * 
	 * @return int max Y Value
	 */
    public int getY2() {
        return y2;
    }

    /**
	  * This method returns how much the old concept must be shifted. 
	 * @return shift of the old concept
	 */
    public int getShiftOldConcept() {
        return shiftOldConcept;
    }

    /**
	  * This method returns how much the plus in the middle must be shifted. 
	 * @return shift of the middle plus
	 */
    public int getShiftCovered() {
        return shiftCovered;
    }

    /**
	 * This method returns how much the new concept must be shifted. 
	 * @return shift of the new concept
	 */
    public int getShiftNewConcept() {
        return shiftNewConcept;
    }

    /**
	 * This method returns how much the new concept must be shifted. 
	 * @return shift of the new concept
	 */
    public int getShiftNewConceptX() {
        return shiftNewConceptX;
    }

    /**
	 * Unsets the panel after plugin is closed.
	 */
    public void unsetPanel() {
        this.removeAll();
        eval = null;
    }

    /**
	 * Returns the currently selected evaluated description.
	 * 
	 * @return EvaluatedDescription
	 */
    public EvaluatedDescription getEvaluateddescription() {
        return eval;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double x = e.getPoint().getX();
        double y = e.getPoint().getY();
        String toolTip = null;
        for (IndividualPoint point : points) {
            if (Math.abs(point.getIndividualPoint().getCenterX() - x) <= 8 && Math.abs(point.getIndividualPoint().getCenterY() - y) <= 8) {
                toolTip = point.getIndividualName();
                break;
            }
        }
        setToolTipText(toolTip);
    }
}
