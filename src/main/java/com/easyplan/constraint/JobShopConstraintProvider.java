package com.easyplan.constraint;

import ai.timefold.solver.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import com.easyplan.domain.Job;


public class JobShopConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                productionLineMatch(factory),
                penalizeLoopedJobs(factory)
        };
    }

    protected Constraint productionLineMatch(ConstraintFactory factory) {
        Constraint constraint = factory.forEach(Job.class)
                .filter(job -> job.getProductionLine() != null && !job.getAvailableProductionLines().contains(job.getProductionLine()))
                .penalize(HardMediumSoftLongScore.ONE_HARD)
                .asConstraint("Unavailable production line");
        return constraint;
    }

    Constraint penalizeLoopedJobs(ConstraintFactory factory) {
        return factory.forEach(Job.class)
                .filter(Job::isLooped)
                .penalize(HardMediumSoftLongScore.ONE_MEDIUM)
                .asConstraint("Job has looped shadow variables.");
    }

}
