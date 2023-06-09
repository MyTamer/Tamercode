package ca.sqlpower.architect.swingui;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import junit.framework.TestCase;
import ca.sqlpower.sqlobject.SQLRelationship;
import ca.sqlpower.sqlobject.SQLTable;

public class TestBasicRelationshipUI extends TestCase {

    Relationship rel;

    PlayPen pp;

    BasicRelationshipUI relUI;

    TablePane tp2;

    protected void setUp() throws Exception {
        super.setUp();
        TestingArchitectSwingSessionContext context = new TestingArchitectSwingSessionContext();
        ArchitectSwingSession session = context.createSession();
        pp = session.getPlayPen();
        SQLTable t1 = new SQLTable(session.getTargetDatabase(), true);
        session.getTargetDatabase().addChild(t1);
        TablePane tp1 = new TablePane(t1, pp.getContentPane());
        pp.addTablePane(tp1, new Point(0, -10));
        SQLTable t2 = new SQLTable(session.getTargetDatabase(), true);
        session.getTargetDatabase().addChild(t2);
        tp2 = new TablePane(t2, pp.getContentPane());
        pp.addTablePane(tp2, new Point(-10, 0));
        SQLRelationship sqlrel = new SQLRelationship();
        sqlrel.attachRelationship(t1, t2, false);
        rel = new Relationship(sqlrel, pp.getContentPane());
        rel.setPkTable(tp1);
        rel.setFkTable(tp2);
        relUI = new IERelationshipUI();
        relUI.installUI(rel);
    }

    public void testComputeBounds() {
        Rectangle2D bounds = rel.getBounds();
        rel.setBounds(122312, 123, 1, 1);
        relUI.computeBounds();
        assertFalse("This should not change the relationship's bounds", rel.getBounds().equals(bounds));
    }
}
