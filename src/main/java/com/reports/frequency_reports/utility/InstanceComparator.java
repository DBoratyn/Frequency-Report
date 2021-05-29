package com.reports.frequency_reports.utility;

import com.reports.frequency_reports.models.Instance;

import java.util.Comparator;

public class InstanceComparator  implements Comparator<Instance> {
    private String type = "count";

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compare(Instance o1, Instance o2) {
        if (this.type.equals("length")) {
            if (o1.getLengthOfWord().compareTo(o2.getLengthOfWord()) == 0) {
                return (o1.getWord().compareTo(o2.getWord()));
            }
            return (o1.getLengthOfWord().compareTo(o2.getLengthOfWord()));
        }
        return o1.getSimilarityCountObject().compareTo(o2.getSimilarityCountObject());
    }
}
