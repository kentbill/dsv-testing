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
                        new ProductionLine(1L, "Line1", LocalDateTime.of(2025, 7, 1, 0, 0)),
                        new ProductionLine(2L, "Line2", LocalDateTime.of(2025, 7, 2, 0, 0))
                ));


        List<Job> jobList = new ArrayList<>();

        Job job01 =  new Job(1, "JobO1",Duration.of(5, ChronoUnit.HOURS), productionLineList,new ArrayList<>());
        jobList.add(job01);

        Job job02 =  new Job(2, "JobO2",Duration.of(8, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job02);

        Job job03 =  new Job(3, "JobO3",Duration.of(9, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job03);

        job03.getPredecessorJobs().add(job01);
        job03.getPredecessorJobs().add(job02);

        Job job04 =  new Job(4, "JobO4",Duration.of(5, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job04);

        Job job05 =  new Job(5, "JobO5",Duration.of(15, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job05);

        Job job06 =  new Job(6, "JobO6",Duration.of(12, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job06);

        job06.getPredecessorJobs().add(job04);
        job06.getPredecessorJobs().add(job05);

        job03.getPredecessorJobs().add(job06);

    /*
        Job job07 =  new Job(7, "JobO7",Duration.of(25, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job07);

        Job job08 =  new Job(8, "JobO8",Duration.of(15, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job08);

        Job job09 =  new Job(9, "JobO9",Duration.of(20, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job09);

        job09.getPredecessorJobs().add(job07);
        job09.getPredecessorJobs().add(job08);

        Job job10 =  new Job(10, "Job10",Duration.of(20, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job10);

        Job job11 =  new Job(11, "Job11",Duration.of(10, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job11);

        Job job12 =  new Job(12, "Job12",Duration.of(10, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job12);

        job12.getPredecessorJobs().add(job10);
        job12.getPredecessorJobs().add(job11);


        Job job13 =  new Job(13, "job13",Duration.of(20, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job13);

        Job job14 =  new Job(14, "job14",Duration.of(10, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job14);

        Job job15 =  new Job(15, "job15",Duration.of(10, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job15);

        job15.getPredecessorJobs().add(job13);
        job15.getPredecessorJobs().add(job14);

        Job job16 =  new Job(16, "job16",Duration.of(20, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job16);

        Job job17 =  new Job(17, "job17",Duration.of(10, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job17);

        Job job18 =  new Job(18, "job18",Duration.of(10, ChronoUnit.HOURS), lineGroup2, new ArrayList<>());
        jobList.add(job18);

        job18.getPredecessorJobs().add(job16);
        job18.getPredecessorJobs().add(job17);


        Job job19 =  new Job(19, "job19",Duration.of(20, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job19);

        Job job20 =  new Job(20, "job20",Duration.of(10, ChronoUnit.HOURS), lineGroup2, new ArrayList<>());
        jobList.add(job20);

        Job job21 =  new Job(21, "job21",Duration.of(10, ChronoUnit.HOURS), lineGroup2, new ArrayList<>());
        jobList.add(job21);

        job21.getPredecessorJobs().add(job19);
        job21.getPredecessorJobs().add(job20);


        Job job22 =  new Job(22, "job22",Duration.of(20, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job22);

        Job job23 =  new Job(23, "job23",Duration.of(10, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job23);

        Job job24 =  new Job(24, "job24",Duration.of(10, ChronoUnit.HOURS), lineGroup2, new ArrayList<>());
        jobList.add(job24);

        job24.getPredecessorJobs().add(job22);
        job24.getPredecessorJobs().add(job23);


        Job job25 =  new Job(25, "job25",Duration.of(20, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job25);

        Job job26 =  new Job(26, "job26",Duration.of(10, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job26);

        Job job27 =  new Job(27, "job27",Duration.of(10, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job27);

        job27.getPredecessorJobs().add(job26);
        job26.getPredecessorJobs().add(job25);

        Job job28 =  new Job(28, "job28",Duration.of(20, ChronoUnit.HOURS), lineGroup1, new ArrayList<>());
        jobList.add(job28);

        Job job29 =  new Job(29, "job29",Duration.of(10, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job29);

        Job job30=  new Job(30, "job30",Duration.of(10, ChronoUnit.HOURS), lineGroup2, new ArrayList<>());
        jobList.add(job30);

        job30.getPredecessorJobs().add(job29);
        job29.getPredecessorJobs().add(job28);


        Job job31 =  new Job(31, "job31",Duration.of(20, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job31);

        Job job32 =  new Job(32, "job32",Duration.of(10, ChronoUnit.HOURS), lineGroup2, new ArrayList<>());
        jobList.add(job32);

        Job job33=  new Job(33, "job33",Duration.of(10, ChronoUnit.HOURS), productionLineList, new ArrayList<>());
        jobList.add(job33);

        job33.getPredecessorJobs().add(job32);
        job32.getPredecessorJobs().add(job31);
        */

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

            String jobInLine = productionLine + ":";


            for(Job job : productionLine.getJobList()) {
                jobInLine += String.format("%s:[%s/%s],",job.toString(),  job.getStartTime(), job.getEndTime() );
            }

            logger.info(jobInLine);
        }
    }

}
