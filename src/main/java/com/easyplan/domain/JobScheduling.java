package com.easyplan.domain;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScore;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

import java.util.List;


@PlanningSolution
public class JobScheduling {

    protected Long id;

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "jobRange")
    protected List<Job> jobList;

    @PlanningEntityCollectionProperty
    protected List<ProductionLine> productionLineList;

    @PlanningScore
    protected HardMediumSoftBigDecimalScore score;

    public JobScheduling() {
    }

    public JobScheduling(Long id, List<Job> jobList, List<ProductionLine> productionLineList) {
        this.id = id;
        this.jobList = jobList;
        this.productionLineList = productionLineList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public List<ProductionLine> getProductionLineList() {
        return productionLineList;
    }

    public void setProductionLList(List<ProductionLine> productionLineList) {
        this.productionLineList = productionLineList;
    }

    public HardMediumSoftBigDecimalScore getScore() {
        return score;
    }

    public void setScore(HardMediumSoftBigDecimalScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
