package com.gatdsen.manager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class TestPermutations<T> {

    private final TestExample<T> testSet;

    static class TestExample<T> {
        public TestExample(boolean positive, T[]... sets) {
            this.positive = positive;
            this.expected = new HashSet<>();
            boolean first = true;
            for (T[] cur :
                    sets) {
                ArrayList<T> list = new ArrayList<>();
                Collections.addAll(list, cur);
                if (first) {
                    this.list = list;
                    first = false;
                } else expected.add(list);
            }
        }

        public boolean positive;
        public List<T> list;
        public Set<List<T>> expected;
    }

    public TestPermutations(TestExample<T> testSet) {
        this.testSet = testSet;
    }

    @Parameterized.Parameters
    public static Collection<TestExample<Object>> data() {
        Collection<TestExample<Object>> samples = new ArrayList<>();
        samples.add(new TestExample<>(false,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 2, 3}
        ));
        samples.add(new TestExample<>( false,
                new String[]{"a", "ab"},
                new String[]{"a", "b"},
                new String[]{"b", "a"}
        ));
        samples.add(new TestExample<>( true,
                new Integer[]{1},
                new Integer[]{1}
        ));
        samples.add(new TestExample<>( true,
                new Integer[]{}
        ));
        samples.add(new TestExample<>(true,
                new Integer[]{1, 2},
                new Integer[]{1, 2},
                new Integer[]{2, 1}
        ));
        samples.add(new TestExample<>(true,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 2, 3},
                new Integer[]{1, 3, 2},
                new Integer[]{2, 1, 3},
                new Integer[]{2, 3, 1},
                new Integer[]{3, 1, 2},
                new Integer[]{3, 2, 1}
        ));
        samples.add(new TestExample<>( true,
                new String[]{"a", "ab"},
                new String[]{"a", "ab"},
                new String[]{"ab", "a"}
        ));
        return samples;
    }

    @Test
    public void test() {
        List<List<T>> actualList = ParallelMultiGameRun.permutations(testSet.list);
        HashSet<List<T>> actualSet = new HashSet<>(actualList);
        if (testSet.positive)
            Assert.assertEquals("Actual subsets aren't equal to expected subsets:\n",
                    testSet.expected, actualSet);
        else
            Assert.assertNotEquals("Actual subsets is equal to expected subsets even tho it shouldn't be:\n",
                    testSet.expected, actualSet);
    }


}
