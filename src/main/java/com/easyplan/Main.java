package com.easyplan;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import com.easyplan.domain.Job;
import com.easyplan.domain.JobScheduling;
import com.easyplan.domain.ProductionLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    private static final Logger logger =  LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        List<ProductionLine> productionLineList = new ArrayList<>(
                Arrays.asList(
                        new ProductionLine(1L, "Line1"),
                        new ProductionLine(2L, "Line2"),
                        new ProductionLine(3L, "Line3")
        ));


        List<Job> jobList = new ArrayList<>();

        Job job01 =  new Job(1, "JobO1",
                Duration.of(5, ChronoUnit.HOURS),
                productionLineList,
                new ArrayList<>()// predecessorJobs
        );
        jobList.add(job01);

        Job job02 =  new Job(2, "JobO2",
                Duration.of(6, ChronoUnit.HOURS),
                productionLineList,
                new ArrayList<>()// predecessorJobs
        );
        jobList.add(job02);

        Job job03 =  new Job(3, "JobO3",
                Duration.of(7, ChronoUnit.HOURS),
                productionLineList,
                new ArrayList<>()// predecessorJobs
        );
        jobList.add(job03);

        job03.getPredecessorJobs().add(job01);
        job03.getPredecessorJobs().add(job02);

        JobScheduling jobSchedulingProblem = new JobScheduling(1L,  jobList, productionLineList);

        String configFileName = System.getProperty("user.dir") + File.separator + "config" + File.separator + "jobSchedulingSolverConfig.xml";
        File configFile = new File(configFileName);
        // Build the Solver
        SolverFactory<JobScheduling> solverFactory = SolverFactory .createFromXmlFile(configFile);
        Solver<JobScheduling> solver = solverFactory.buildSolver();

        // Solve the problem
        JobScheduling solvedJobScheduling = solver.solve(jobSchedulingProblem);


        for (ProductionLine productionLine : solvedJobScheduling.getProductionLineList()) {
            if(productionLine.getJobList().isEmpty()) continue;

            String jobInLine = productionLine.toString() + ":";


            for(Job job : productionLine.getJobList()) {
                jobInLine += job.toString() + ",";
            }

            logger.info(jobInLine);
        }

    }


}
