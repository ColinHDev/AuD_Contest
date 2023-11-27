package com.example.manager;

import com.example.manager.ParallelMultiGameRun;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class TestSubsetK<T> {

    private final TestExample<T> testSet;

    static class TestExample<T> {
        public TestExample(int subsetSize, boolean positive, T[]... sets) {
            this.subsetSize = subsetSize;
            this.positive = positive;
            this.expectedSubsets = new HashSet<>();
            boolean first = true;
            for (T[] cur :
                    sets) {
                ArrayList<T> list = new ArrayList<>();
                Collections.addAll(list, cur);
                if (first) {
                    this.list = list;
                    first = false;
                } else expectedSubsets.add(new HashSet<>(list));
            }
        }

        public boolean positive;
        public List<T> list;
        public int subsetSize;
        public Set<Set<T>> expectedSubsets;
    }

    public TestSubsetK(TestExample<T> testSet) {
        this.testSet = testSet;
    }

    @Parameterized.Parameters
    public static Collection<TestExample<Object>> data() {
        Collection<TestExample<Object>> samples = new ArrayList<>();
        samples.add(new TestExample<>(0, false,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 2, 3}
        ));
        samples.add(new TestExample<>(0, true,
                new Integer[]{1, 2, 3}
        ));
        samples.add(new TestExample<>(1, true,
                new Integer[]{1, 2, 3},
                new Integer[]{1},
                new Integer[]{2},
                new Integer[]{3}
        ));
        samples.add(new TestExample<>(2, true,
                new Integer[]{1, 2, 3},
                new Integer[]{2, 3},
                new Integer[]{1, 2},
                new Integer[]{1, 3}
        ));
        samples.add(new TestExample<>(3, true,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 2, 3}
        ));
        samples.add(new TestExample<>(3, true,
                new String[]{"a", "ab", "abc"},
                new String[]{"a", "ab", "abc"}
        ));
        return samples;
    }

    @Test
    public void test() {
        List<List<T>> actualList = ParallelMultiGameRun.subsetK(testSet.list, testSet.subsetSize);
        HashSet<HashSet<T>> actualSet = new HashSet<>();
        for (List<T> cur : actualList
             ) {
            actualSet.add(new HashSet<>(cur));
        }
        if (testSet.positive)
            Assert.assertEquals("Actual subsets aren't equal to expected subsets:\n",
                    testSet.expectedSubsets, actualSet);
        else
            Assert.assertNotEquals("Actual subsets is equal to expected subsets even tho it shouldn't be:\n",
                    testSet.expectedSubsets, actualSet);
    }


}
