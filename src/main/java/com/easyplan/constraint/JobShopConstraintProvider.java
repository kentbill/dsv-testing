package com.easyplan.constraint;


import ai.timefold.solver.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScore;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.common.LoadBalance;
import com.easyplan.domain.Job;
import com.easyplan.domain.ProductionLine;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static ai.timefold.solver.core.api.score.stream.Joiners.equal;
import static ai.timefold.solver.core.api.score.stream.Joiners.overlapping;


public class JobShopConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                productionLineMatch(factory),
//                continuousProcessing(factory),
                penalizeLoopedJobs(factory),
                balanceProductionLine(factory),
                minimizeMakespan(factory)
        };
    }

    protected Constraint productionLineMatch(ConstraintFactory factory) {
        Constraint constraint = factory.forEach(Job.class)
                .filter(job -> job.getProductionLine() != null && !job.getAvailableProductionLines().contains(job.getProductionLine()))
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Unavailable production line");
        return constraint;
    }


    Constraint continuousProcessing(ConstraintFactory factory) {
        return factory.forEachUniquePair(Job.class,
                        equal(Job::getParentCode)
                        )
                .filter((jobStart, jobEnd) -> {
                    if(jobStart.getProductionLine() == null || jobEnd.getProductionLine() == null ||
                        jobStart.getStartTime() == null || jobEnd.getStartTime() == null ||
                            jobStart.getSeq() == null || jobEnd.getSeq() == null
                    ){
                        return false;
                    }
                    if(jobStart.getSeq() < jobEnd.getSeq()) {
                        return jobStart.getEndTime().isAfter(jobEnd.getStartTime());
                    } else {
                        return jobStart.getStartTime().isBefore(jobEnd.getEndTime()) || jobStart.getStartTime().equals(jobEnd.getEndTime());
                    }
                })
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Wrong sequence");
    }

    Constraint penalizeLoopedJobs(ConstraintFactory factory) {
        return factory.forEach(Job.class)
                .filter(Job::isLooped)
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_MEDIUM)
                .asConstraint("Job has looped shadow variables.");
    }

    Constraint balanceProductionLine(ConstraintFactory factory) {
            return factory.forEach(Job.class)
                    .groupBy(job -> job.getProductionLine(), job -> job.getDuration().toMinutes())
                    .complement(ProductionLine.class, e -> 0L)
                    .groupBy(ConstraintCollectors.loadBalance((productionLine, jobCount) -> productionLine,
                            (productionLine, jobCount) -> jobCount))
                    .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_MEDIUM, LoadBalance::unfairness)
                    .asConstraint("fairAssignmentCountPerTeam");
    }

     Constraint minimizeMakespan(ConstraintFactory factory) {
        return factory.forEach(Job.class)
                .filter(job -> job.getProductionLine() != null &&  job.getEndTime() != null && job.getNextJob() == null)
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_SOFT, job -> {
                    long minutes = Duration.between(job.getProductionLine().getStartTime(), job.getEndTime()).toMinutes();
                    return BigDecimal.valueOf(minutes * minutes);
                })
                .asConstraint("Minimize make span");
    }

     Constraint minimizeMakespan1(ConstraintFactory factory) {
        return factory.forEach(ProductionLine.class)
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_SOFT,
                        productionLine -> BigDecimal.valueOf(Duration.between(productionLine.getStartTime(), productionLine.getEndTime()).toMinutes() *
                                Duration.between(productionLine.getStartTime(), productionLine.getEndTime()).toMinutes()))
                .asConstraint("Minimize make span");
    }

}
