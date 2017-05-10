package com.cluster.scheduler;

import com.cluster.job.HelloJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class DemoScheduler implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DemoScheduler.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @Override
    public void run(String...args) throws Exception {

        Scheduler scheduler = schedulerFactory.getScheduler();

        Map<JobDetail, Trigger> jobTriggerMap = getJobDetailTriggerMap();

        jobTriggerMap.forEach((job, trigger) -> {
            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                LOG.info(e.getMessage());
            }
        });
    }

    private Map<JobDetail, Trigger> getJobDetailTriggerMap() {
        Map<JobDetail, Trigger> jobTriggerMap = new HashMap<>();

        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1", "group1")
                .usingJobData("message", "AA").build();
        Trigger trigger1 = newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("0/20 * * * * ?")).build();
        jobTriggerMap.put(job1, trigger1);

        JobDetail job2 = newJob(HelloJob.class).withIdentity("job2", "group1")
                .usingJobData("message", "BB").build();
        Trigger trigger2 = newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(cronSchedule("0/5 * * * * ?")).build();
        jobTriggerMap.put(job2, trigger2);

        JobDetail job3 = newJob(HelloJob.class).withIdentity("job3", "group1")
                .usingJobData("message", "CC").build();
        Trigger trigger3 = newTrigger().withIdentity("trigger3", "group1")
                .withSchedule(cronSchedule("0/10 * * * * ?")).build();
        jobTriggerMap.put(job3, trigger3);


        return jobTriggerMap;
    }

}
