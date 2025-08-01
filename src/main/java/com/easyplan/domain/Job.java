package com.easyplan.domain;



import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.*;
import ai.timefold.solver.core.preview.api.domain.variable.declarative.ShadowSources;
import ai.timefold.solver.core.preview.api.domain.variable.declarative.ShadowVariableLooped;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@PlanningEntity
public class Job {

    @PlanningId
    private Long id;

    private String parentCode;

    private Integer seq;

    /**
     * the changeover duration from other jobs
     * key - the other job id
     * value - the changeover duration from another job.
     */
    private Map<Long, Duration> changeoverDurationMap;

    private String code;

    private Duration duration;

    private Collection<ProductionLine> availableProductionLines;

    private Collection<Job> predecessorJobs;

    @InverseRelationShadowVariable(sourceVariableName = "jobList")
    private ProductionLine productionLine;

    @PreviousElementShadowVariable(sourceVariableName = "jobList")
    private Job previousJob;

    @NextElementShadowVariable(sourceVariableName = "jobList")
    private Job nextJob;

    @ShadowVariable(supplierName = "startTimeSupplier")
    private LocalDateTime startTime;

    @ShadowVariable(supplierName = "endTimeSupplier")
    private LocalDateTime endTime;

    @ShadowVariableLooped
    public boolean looped;

    private LocalDateTime readyTime;


    public Job() {
    }


    public Job(long id, String code, String parentCode, Integer seq,
               Duration duration, Collection<ProductionLine> availableProductionLines,
               Collection<Job> predecessorJobs, LocalDateTime readyTime,
               Map<Long, Duration> changeoverDurationMap
               ) {
        this.id = id;
        this.code = code;
        this.parentCode = parentCode;
        this.seq = seq;
        this.duration = duration;
        this.availableProductionLines = availableProductionLines;
        this.predecessorJobs = predecessorJobs;
        this.readyTime = readyTime;
        this.changeoverDurationMap = changeoverDurationMap;
    }


    @ShadowSources({"previousJob.endTime",
            "predecessorJobs[].endTime",
            "productionLine"
    })
    public LocalDateTime startTimeSupplier() {
        LocalDateTime startTime = null;

        // get startTime by previous endTime
        if(this.productionLine == null) {
            return null;
        } else {
            if(this.previousJob == null) {
                startTime = this.productionLine.getStartTime();
            } else {
                if(this.previousJob.getEndTime() == null) {
                    return null;
                } else {
                    startTime = this.previousJob.getEndTime();
                    // changeover
                    startTime = startTime.plus(this.getChangeoverDurationMap().get(this.previousJob.getId()));
                }
            }
        }

        if(this.predecessorJobs != null) {
            for(Job predecessorJob : this.predecessorJobs) {
                if(predecessorJob.getEndTime() == null) {
                    continue;
                }
                if(startTime.isBefore(predecessorJob.getEndTime())) {
                    startTime = predecessorJob.getEndTime();
                }
            }
        }

        // 如果readyTime非空，确保startTime不早于readyTime
        if(this.readyTime != null && startTime.isBefore(this.readyTime)) {
            startTime = this.readyTime;
        }

        return startTime;
    }

    @ShadowSources("startTime")
    public LocalDateTime endTimeSupplier() {
        if(this.startTime == null) {
            return null;
        } else {
            return this.startTime.plus(this.duration);
        }
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Collection<ProductionLine> getAvailableProductionLines() {
        return availableProductionLines;
    }

    public void setAvailableProductionLines(Collection<ProductionLine> availableProductionLines) {
        this.availableProductionLines = availableProductionLines;
    }

    public Collection<Job> getPredecessorJobs() {
        return predecessorJobs;
    }

    public void setPredecessorJobs(Collection<Job> predecessorJobs) {
        this.predecessorJobs = predecessorJobs;
    }

    public ProductionLine getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(ProductionLine productionLine) {
        this.productionLine = productionLine;
    }

    public Job getPreviousJob() {
        return previousJob;
    }

    public void setPreviousJob(Job previousJob) {
        this.previousJob = previousJob;
    }

    public Job getNextJob() {
        return nextJob;
    }

    public void setNextJob(Job nextJob) {
        this.nextJob = nextJob;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isLooped() {
        return looped;
    }

    public void setLooped(boolean looped) {
        this.looped = looped;
    }

    public LocalDateTime getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(LocalDateTime readyTime) {
        this.readyTime = readyTime;
    }

    public Map<Long, Duration> getChangeoverDurationMap() {
        return changeoverDurationMap;
    }

    public void setChangeoverDurationMap(Map<Long, Duration> changeoverDurationMap) {
        this.changeoverDurationMap = changeoverDurationMap;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
