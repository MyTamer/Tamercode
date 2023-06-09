package jfreerails.move;

import jfreerails.world.station.StationModel;
import jfreerails.world.top.KEY;
import jfreerails.world.top.MapFixtureFactory;

/**
 * JUnit test.
 * 
 * @author Luke
 * 
 */
public class CompositeMoveTest extends AbstractMoveTestCase {

    final StationModel station1 = new StationModel(1, 1, "station1", 10, 0);

    final StationModel station2 = new StationModel(2, 3, "station2", 10, 0);

    final StationModel station3 = new StationModel(3, 3, "station3", 10, 0);

    final StationModel station4 = new StationModel(4, 4, "station4", 10, 0);

    @Override
    public void testMove() {
        Move[] moves = new Move[4];
        moves[0] = new AddItemToListMove(KEY.STATIONS, 0, station1, MapFixtureFactory.TEST_PRINCIPAL);
        moves[1] = new AddItemToListMove(KEY.STATIONS, 1, station2, MapFixtureFactory.TEST_PRINCIPAL);
        moves[2] = new AddItemToListMove(KEY.STATIONS, 2, station3, MapFixtureFactory.TEST_PRINCIPAL);
        moves[3] = new AddItemToListMove(KEY.STATIONS, 3, station4, MapFixtureFactory.TEST_PRINCIPAL);
        Move compositeMove = new CompositeMove(moves);
        assertSurvivesSerialisation(compositeMove);
        assertTryMoveIsOk(compositeMove);
        assertEquals("The stations should not have been add yet.", 0, getWorld().size(MapFixtureFactory.TEST_PRINCIPAL, KEY.STATIONS));
        assertDoMoveIsOk(compositeMove);
        assertEquals("The stations should have been add now.", 4, getWorld().size(MapFixtureFactory.TEST_PRINCIPAL, KEY.STATIONS));
        assertTryUndoMoveIsOk(compositeMove);
        assertUndoMoveIsOk(compositeMove);
        assertOkButNotRepeatable(compositeMove);
    }
}
