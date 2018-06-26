package com.huawei.cloud.entity;

import java.util.Collections;
import java.util.Map;

/**
 * Created by bWX419359 on 11/22/2016.
 */
public class ServiceTestStatus {
    private boolean isSucceeded;
    //succeed
    private Map<String, Boolean> runTests;

    public ServiceTestStatus() {
    }

    public ServiceTestStatus(boolean isSucceeded, Map<String, Boolean> runTests) {
        this.isSucceeded = isSucceeded;
        this.runTests = runTests;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setSucceeded(boolean succeeded) {
        isSucceeded = succeeded;
    }

    public Map<String, Boolean> getRunTests() {
        return Collections.unmodifiableMap(runTests);
    }

    public void addRunTest(String testName, boolean isTestSucceeded) {
        runTests.put(testName, isTestSucceeded);

        if (!isTestSucceeded) {
            isSucceeded = false;
        }
    }

}
