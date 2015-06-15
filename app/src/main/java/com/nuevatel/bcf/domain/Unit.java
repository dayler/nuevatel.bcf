package com.nuevatel.bcf.domain;

import com.nuevatel.common.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Created by asalazar on 6/5/15.
 */
public class Unit {

    private String name;

    private List<Integer> regexIds = new ArrayList<>();

    private Map<Integer, Pair<Date, Date>> timespanMap = new HashMap<>();

    public Unit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addRegexId(Integer id) {
        if (id == null) {
            return;
        }

        regexIds.add(id);
    }

    public List<Integer> getRegexIds() {
        return Collections.unmodifiableList(regexIds);
    }

    public void addTimespan(Integer regexId, Date startTimestamp, Date endTimestamp) {
        if (regexId == null) {
            return;
        }

        Pair<Date, Date> timespan = addTimespanNewIfItNotExists(regexId);
        timespan.setFirst(startTimestamp);
        timespan.setSecond(endTimestamp);
    }

    public Date getStartTimestamp(Integer regexId) {
        Pair<Date, Date>timespan = timespanMap.get(regexId);
        return timespan == null ? null : timespan.getFirst();
    }

    private Pair<Date, Date> addTimespanNewIfItNotExists(Integer regexId) {
        Pair<Date, Date>timespan = timespanMap.get(regexId);
        if (timespan == null) {
            timespan = new Pair<>(null, null);
            timespanMap.put(regexId, timespan);
        }
        return timespan;
    }

    public Date getEndTimestamp(Integer regexId) {
        Pair<Date, Date>timespan = timespanMap.get(regexId);
        return timespan == null ? null : timespan.getSecond();
    }
}
