package com.easyplan.constraint;

import ai.timefold.solver.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScore;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.common.LoadBalance;
import com.easyplan.domain.Job;

import static ai.timefold.solver.core.api.score.stream.ConstraintCollectors.loadBalance;


public class JobShopConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                productionLineMatch(factory),
                penalizeLoopedJobs(factory),
                balanceProductionLine(factory),

        };
    }

    protected Constraint productionLineMatch(ConstraintFactory factory) {
        Constraint constraint = factory.forEach(Job.class)
                .filter(job -> job.getProductionLine() != null && !job.getAvailableProductionLines().contains(job.getProductionLine()))
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Unavailable production line");
        return constraint;
    }

    Constraint penalizeLoopedJobs(ConstraintFactory factory) {
        return factory.forEach(Job.class)
                .filter(Job::isLooped)
                .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_MEDIUM)
                .asConstraint("Job has looped shadow variables.");
    }

    Constraint balanceProductionLine(ConstraintFactory factory) {

            return factory.forEach(Job.class)
                    .groupBy(loadBalance(Job::getProductionLine))
                    .penalizeBigDecimal(HardMediumSoftBigDecimalScore.ONE_SOFT, LoadBalance::unfairness)
                    .asConstraint("fairAssignmentCountPerTeam");


    }

}
