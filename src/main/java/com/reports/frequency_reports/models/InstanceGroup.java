package com.reports.frequency_reports.models;

import java.util.ArrayList;
import java.util.List;

public class InstanceGroup {
    private List<Instance> instances = new ArrayList<>();
    private int groupNumber;

    public InstanceGroup(Instance instance, int groupNumber) {
        this.instances.add(instance);
        this.groupNumber = groupNumber;
    }

    public List<Instance> getInstances() {
        return this.instances;
    }

    public void addInstance(Instance instance) {
    this.instances.add(instance);
    }

    public int getGroupNumber() {
        return this.groupNumber;
    }
}
