package jfreerails.move;

import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.KEY;
import jfreerails.world.train.ImmutableSchedule;

/**
 * This Move changes a train's schedule.
 * 
 * @author Luke Lindsay
 * 
 */
public class ChangeTrainScheduleMove extends ChangeItemInListMove {

    private static final long serialVersionUID = 3691043187930052149L;

    public ChangeTrainScheduleMove(int id, ImmutableSchedule before, ImmutableSchedule after, FreerailsPrincipal p) {
        super(KEY.TRAIN_SCHEDULES, id, before, after, p);
    }
}
