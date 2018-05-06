package mearns.douglas.haelauncher.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FloatRangeTest {
    @Test
    public void testRangeStartsWithMin() {
        Iterable<Float> result = FloatRange.create(0, 0, 2);
        Iterator<Float> itr = result.iterator();
        assertThat(itr.hasNext(), is(true));
        assertThat(itr.next(), equalTo(0.0f));
    }

    @Test
    public void testRangeEndsWithMax() {
        Iterable<Float> result = FloatRange.create(0, 0, 2);
        Iterator<Float> itr = result.iterator();
        itr.next();
        assertThat(itr.next(), equalTo(2.0f));
    }

    @Test
    public void testRangeSizeMatchesExpected() {
        Iterable<Float> result = FloatRange.create(0, 10, 2);
        int count = 0;
        for (Float f : result) {
            count += 1;
        }
        assertThat(count, equalTo(10+2));
    }

    @Test
    public void testRangeMiddleAsExpected() {
        Iterable<Float> result = FloatRange.create(0, 1, 2);
        Iterator<Float> itr =  result.iterator();
        itr.next();
        assertThat(itr.next(), equalTo(1.0f));
    }

    @Test
    public void testRangeAsExpected1() {
        Iterable<Float> result = FloatRange.create(0, 4, 5);
        List<Float> collectedResults = new LinkedList<>();
        for (Float f : result) {
            collectedResults.add(f);
        }
        assertThat(collectedResults, equalTo(Arrays.asList(0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f)));
    }

    @Test
    public void testRangeAsExpected2() {
        Iterable<Float> result = FloatRange.create(0, 9, 5);
        List<Float> collectedResults = new LinkedList<>();
        for (Float f : result) {
            collectedResults.add(f);
        }
        assertThat(collectedResults, equalTo(Arrays.asList(0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f)));
    }
}

