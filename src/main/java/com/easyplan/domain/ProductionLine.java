package com.easyplan.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import java.time.LocalDateTime;
import java.util.*;

@PlanningEntity
public class ProductionLine {

    @PlanningId
    private Long id;

    private String code;

    private LocalDateTime startTime;

    @PlanningListVariable( valueRangeProviderRefs = {"jobRange"})
    private List<Job> jobList;


    public ProductionLine() {
    }

    public ProductionLine(long id, String code, LocalDateTime startTime) {
        this.id = id;
        this.code = code;
        this.startTime = startTime;
        jobList = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return jobList.isEmpty() ? this.startTime : jobList.get(jobList.size() - 1).getEndTime();
    }

    public List<Job> getJobList() {
        return jobList;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
