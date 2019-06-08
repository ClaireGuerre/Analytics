package com.analytics;

import com.analytics.model.Query;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestAnalytics {

    private Analytics analytics;

    @Before
    public void initialize() {
        analytics = new Analytics();
    }

    @Test
    public void testGetNbDistinctQueries() {
        assertEquals(573697, analytics.getNbDistinctQueries("2015"));
        assertEquals(573697, analytics.getNbDistinctQueries("2015-08"));
        assertEquals(198117, analytics.getNbDistinctQueries("2015-08-03"));
        assertEquals(617, analytics.getNbDistinctQueries("2015-08-01 00:04"));
    }

    @Test
    public void getTopPopularQueries() {
        List<Query> popularQueries2015 = analytics.getTopPopularQueries("2015",3);
        assertEquals(3, popularQueries2015.size());
        assertEquals("http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice",popularQueries2015.get(0).getContent());
        assertEquals("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045",popularQueries2015.get(1).getContent());
        assertEquals("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1",popularQueries2015.get(2).getContent());

        List<Query> popularQueriesAugustScnd = analytics.getTopPopularQueries("2015-08-02",5);
        assertEquals(5, popularQueriesAugustScnd.size());
        assertEquals("http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice",popularQueriesAugustScnd.get(0).getContent());
        assertEquals("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045",popularQueriesAugustScnd.get(1).getContent());
        assertEquals("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1",popularQueriesAugustScnd.get(2).getContent());
        assertEquals("http%3A%2F%2Fjamonkey.com%2F50-organizing-ideas-for-every-room-in-your-house%2F",popularQueriesAugustScnd.get(3).getContent());
        assertEquals("http%3A%2F%2Fsharingis.cool%2F1000-musicians-played-foo-fighters-learn-to-fly-and-it-was-epic",popularQueriesAugustScnd.get(4).getContent());

    }
}
