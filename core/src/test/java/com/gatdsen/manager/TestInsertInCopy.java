package com.gatdsen.manager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class TestInsertInCopy<T> {

    private final TestExample<T> testSet;

    static class TestExample<T> {
        private int index;
        private boolean positive;
        private List<T> list;
        private T element;
        private List<T> expected;

        public TestExample(int index, T element, T[] list, T[] expected) {
            this.index = index;
            this.list = new ArrayList<>();
            Collections.addAll(this.list, list);
            this.element = element;
            this.expected = new ArrayList<>();
            Collections.addAll(this.expected, expected);
        }
    }

    public TestInsertInCopy(TestExample<T> testSet) {
        this.testSet = testSet;
    }

    @Parameterized.Parameters
    public static Collection<TestExample<Object>> data() {
        Collection<TestExample<Object>> samples = new ArrayList<>();
        samples.add(new TestExample<>(0, 1,
                new Integer[]{},
                new Integer[]{1}
        ));
        samples.add(new TestExample<>(0, 2,
                new Integer[]{},
                new Integer[]{2}
        ));
        samples.add(new TestExample<>(0, 1,
                new Integer[]{1},
                new Integer[]{1, 1}
        ));
        samples.add(new TestExample<>(1, 1,
                new Integer[]{1},
                new Integer[]{1, 1}
        ));
        samples.add(new TestExample<>(0, 2,
                new Integer[]{1},
                new Integer[]{2, 1}
        ));
        samples.add(new TestExample<>(1, 2,
                new Integer[]{1},
                new Integer[]{1, 2}
        ));
        samples.add(new TestExample<>(0, 4,
                new Integer[]{1, 2, 3},
                new Integer[]{4, 1, 2, 3}
        ));
        samples.add(new TestExample<>(1, 4,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 4, 2, 3}
        ));
        samples.add(new TestExample<>(2, 4,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 2, 4, 3}
        ));
        samples.add(new TestExample<>(3, 4,
                new Integer[]{1, 2, 3},
                new Integer[]{1, 2, 3, 4}
        ));
        return samples;
    }

    @Test
    public void test() {
        int hash = testSet.list.hashCode();
        List<T> actualList = ParallelMultiGameRun.insertInCopy(testSet.list, testSet.element, testSet.index);
        Assert.assertEquals("Actual subsets aren't equal to expected subsets:\n",
                testSet.expected, actualList);
        Assert.assertEquals("Passed list has been altered by the function:\n",
                hash, testSet.list.hashCode());
    }


}
