package com.gatdsen.manager;

import com.gatdsen.manager.run.config.RunConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestGroupMatchmaking {
    private final RunConfiguration runConfig;
    private final Run run;
    private final Manager manager;

    private boolean completed = false;
    private final Object lock = new Object();

    static class TestExample{
    private RunConfiguration config;

    public TestExample(RunConfiguration config) {
        this.config = config;
    }

}

    public TestGroupMatchmaking(TestExample testSet) {
        this.runConfig = testSet.config;
        manager = Manager.getManager();
        run = manager.startRun(testSet.config);
        synchronized (lock){
            run.addCompletionListener(run ->{
                completed = true;
                synchronized (lock) {
                    lock.notify();
                }
            });
        }
    }


    @Parameterized.Parameters
    public static Collection<TestExample> data() {
        return new ArrayList<>();
    }

    @Test
    public void test(){
        //ToDo implement
        Assert.assertTrue(true);
    }



}
