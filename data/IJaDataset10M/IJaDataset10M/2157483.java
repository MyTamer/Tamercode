package ca.sqlpower.architect.swingui.action;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import ca.sqlpower.architect.ArchitectVersion;
import ca.sqlpower.architect.swingui.ASUtils;
import ca.sqlpower.architect.swingui.ArchitectFrame;
import ca.sqlpower.architect.swingui.ArchitectSwingSession;
import ca.sqlpower.architect.swingui.PlayPen;
import ca.sqlpower.architect.swingui.PlayPenComponent;
import ca.sqlpower.architect.swingui.PlayPenContentPane;
import ca.sqlpower.swingui.SPSUtils;
import ca.sqlpower.util.MonitorableImpl;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class ExportPlaypenToPDFAction extends ProgressAction {

    private static final Logger logger = Logger.getLogger(ExportPlaypenToPDFAction.class);

    private static final String FILE_KEY = "FILE_KEY";

    private static int OUTSIDE_PADDING = 10;

    /**
     * A snapshot of the play pen. This play pen will look exactly like the play
     * pen the last time this action was called. This play pen will also be 
     * disconnected from the model in terms of listeners so it cannot change
     * the model, just draw it.
     * 
     * <p>
     * Note that the play pen this variable is pointing at may change during
     * export if the play pen is exported again while it is currently exporting.
     */
    private PlayPen playPen;

    /**
     * Creates an action that exports the session's relational playpen to a PDF
     * file.
     * 
     * @param session
     *            The session that owns this action.
     * @param playPen
     *            The playpen to export. Could be the relational playpen or one
     *            of the OLAP playpens.
     */
    public ExportPlaypenToPDFAction(ArchitectSwingSession session, PlayPen playPen) {
        super(session, playPen, Messages.getString("ExportPlaypenToPDFAction.name"), Messages.getString("ExportPlaypenToPDFAction.description"), null);
    }

    /**
     * Creates an action that exports the frame's current session's relational playpen to a PDF
     * file.
     * 
     * @param frame
     *            The ArchitectFrame that owns the action
     */
    public ExportPlaypenToPDFAction(ArchitectFrame frame) {
        super(frame, Messages.getString("ExportPlaypenToPDFAction.name"), Messages.getString("ExportPlaypenToPDFAction.description"), null);
    }

    /**
     *  When an action is performed on this it pops up the save dialog
     *  and requests a file to save to. When it gets that it draws the
     *  playpen to a PDF file on a seperate thread.
     */
    public boolean setup(MonitorableImpl monitor, Map<String, Object> properties) {
        monitor.setStarted(true);
        JFileChooser chooser = new JFileChooser(getSession().getRecentMenu().getMostRecentFile());
        chooser.addChoosableFileFilter(SPSUtils.PDF_FILE_FILTER);
        monitor.setJobSize(getPlaypen().getContentPane().getChildren().size());
        File file = null;
        while (true) {
            int response = chooser.showSaveDialog(getPlaypen());
            if (response != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            file = chooser.getSelectedFile();
            String fileName = file.getName();
            if (!fileName.endsWith(".pdf")) {
                file = new File(file.getPath() + ".pdf");
            }
            if (file.exists()) {
                response = JOptionPane.showConfirmDialog(null, Messages.getString("ExportPlaypenToPDFAction.fileAlreadyExists", file.getPath()), Messages.getString("ExportPlaypenToPDFAction.fileAlreadyExistsDialogTitle"), JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    break;
                }
            } else {
                break;
            }
        }
        logger.debug("Saving to file: " + file.getName() + "(" + file.getPath() + ")");
        properties.put(FILE_KEY, file);
        playPen = new PlayPen(getSession(), getPlaypen());
        playPen.destroy();
        return true;
    }

    @Override
    public void cleanUp(MonitorableImpl monitor) {
    }

    @Override
    public void doStuff(MonitorableImpl monitor, Map<String, Object> properties) {
        logger.debug("Creating PDF of playpen: " + getPlaypen());
        PlayPen pp = playPen;
        final int width = pp.getBounds().width + 2 * OUTSIDE_PADDING;
        final int height = pp.getBounds().height + 2 * OUTSIDE_PADDING;
        final Rectangle ppSize = new Rectangle(width, height);
        OutputStream out = null;
        Document d = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream((File) properties.get(FILE_KEY)));
            d = new Document(ppSize);
            d.addTitle(Messages.getString("ExportPlaypenToPDFAction.PdfTitle"));
            d.addAuthor(System.getProperty("user.name"));
            d.addCreator(Messages.getString("ExportPlaypenToPDFAction.powerArchitectVersion") + ArchitectVersion.APP_FULL_VERSION);
            PdfWriter writer = PdfWriter.getInstance(d, out);
            d.open();
            PdfContentByte cb = writer.getDirectContent();
            Graphics2D g = cb.createGraphicsShapes(width, height);
            g.translate(OUTSIDE_PADDING, OUTSIDE_PADDING);
            PlayPenContentPane contentPane = pp.getContentPane();
            for (int i = 0; i < contentPane.getChildren().size(); i++) {
                PlayPenComponent ppc = contentPane.getChildren().get(i);
                if (logger.isDebugEnabled()) {
                    logger.debug("Painting component " + ppc);
                }
                g.translate(ppc.getLocation().x, ppc.getLocation().y);
                Font gFont = g.getFont();
                ppc.paint(g);
                g.setFont(gFont);
                g.translate(-ppc.getLocation().x, -ppc.getLocation().y);
                monitor.setProgress(i);
            }
            pp.paintComponent(g);
            g.dispose();
        } catch (Exception ex) {
            ASUtils.showExceptionDialog(getSession(), Messages.getString("ExportPlaypenToPDFAction.couldNotExportPlaypen"), ex);
        } finally {
            if (d != null) {
                try {
                    d.close();
                } catch (Exception ex) {
                    ASUtils.showExceptionDialog(getSession(), Messages.getString("ExportPlaypenToPDFAction.couldNotCloseDocument"), ex);
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    ASUtils.showExceptionDialog(getSession(), Messages.getString("ExportPlaypenToPDFAction.couldNotClosePdfFile"), ex);
                }
            }
        }
    }

    @Override
    public String getDialogMessage() {
        return Messages.getString("ExportPlaypenToPDFAction.creatingPdf");
    }

    @Override
    public String getButtonText() {
        return Messages.getString("ExportPlaypenToPDFAction.runInBackgroundOption");
    }
}
