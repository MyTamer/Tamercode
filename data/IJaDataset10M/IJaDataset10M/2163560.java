package org.apache.shindig.social.opensocial.jpa.spi;

import static org.junit.Assert.assertEquals;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.model.EnumUtil;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.apache.shindig.social.opensocial.spi.UserId.Type;
import java.util.Set;
import java.util.concurrent.Future;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Test the ActivityServiceDb implementation.
 *
 */
public class ActivityServiceDbTest {

    private static final Set<String> ACTIVITY_ALL_FIELDS = EnumUtil.getEnumStrings(Activity.Field.values());

    private final Activity testActivity = SpiTestUtil.buildTestActivity("1", "john.doe", "yellow", "what a color!");

    private ActivityServiceDb activityServiceDb;

    /** The bootstrap. */
    private SpiDatabaseBootstrap bootstrap;

    @Before
    public void setup() throws Exception {
        EntityManager entityManager = SpiEntityManagerFactory.getEntityManager();
        this.activityServiceDb = new ActivityServiceDb(entityManager);
        this.bootstrap = new SpiDatabaseBootstrap(entityManager);
        this.bootstrap.init();
    }

    @After
    public void tearDown() throws Exception {
        bootstrap.tearDown();
    }

    @Test
    public void getJohnDoeActivityWithAppId1() throws Exception {
        Future<Activity> result = this.activityServiceDb.getActivity(new UserId(Type.userId, "john.doe"), new GroupId(GroupId.Type.self, "@self"), null, ACTIVITY_ALL_FIELDS, "1", SpiTestUtil.DEFAULT_TEST_SECURITY_TOKEN);
        Activity activity = result.get();
        SpiTestUtil.assertActivityEquals(activity, testActivity);
    }

    @Test
    public void getJohnDoeActivities() throws Exception {
        Future<RestfulCollection<Activity>> result = this.activityServiceDb.getActivities(SpiTestUtil.buildUserIds("john.doe"), new GroupId(GroupId.Type.self, "@self"), null, ACTIVITY_ALL_FIELDS, new CollectionOptions(), SpiTestUtil.DEFAULT_TEST_SECURITY_TOKEN);
        RestfulCollection<Activity> activityCollection = result.get();
        assertEquals(1, activityCollection.getTotalResults());
        assertEquals(0, activityCollection.getStartIndex());
        SpiTestUtil.assertActivityEquals(activityCollection.getEntry().get(0), testActivity);
    }

    @Test
    public void getJohnDoeFriendsActivities() throws Exception {
        Future<RestfulCollection<Activity>> result = this.activityServiceDb.getActivities(SpiTestUtil.buildUserIds("john.doe"), new GroupId(GroupId.Type.friends, "@friends"), null, ACTIVITY_ALL_FIELDS, new CollectionOptions(), SpiTestUtil.DEFAULT_TEST_SECURITY_TOKEN);
        RestfulCollection<Activity> activityCollection = result.get();
        assertEquals(2, activityCollection.getTotalResults());
        assertEquals(0, activityCollection.getStartIndex());
    }

    @Test
    public void createNewActivityForJohnDoe() throws Exception {
        final String title = "hi mom!";
        final String body = "and dad.";
        Activity activity = SpiTestUtil.buildTestActivity("2", "john.doe", title, body);
        this.activityServiceDb.createActivity(new UserId(Type.userId, "john.doe"), new GroupId(GroupId.Type.self, "@self"), "2", ACTIVITY_ALL_FIELDS, activity, SpiTestUtil.DEFAULT_TEST_SECURITY_TOKEN);
        Future<RestfulCollection<Activity>> result = this.activityServiceDb.getActivities(SpiTestUtil.buildUserIds("john.doe"), new GroupId(GroupId.Type.self, "@self"), null, ACTIVITY_ALL_FIELDS, new CollectionOptions(), SpiTestUtil.DEFAULT_TEST_SECURITY_TOKEN);
        RestfulCollection<Activity> activityCollection = result.get();
        assertEquals(2, activityCollection.getTotalResults());
        assertEquals(0, activityCollection.getStartIndex());
        activity = activityCollection.getEntry().get(1);
        assertEquals(activity.getTitle(), title);
        assertEquals(activity.getBody(), body);
    }
}
