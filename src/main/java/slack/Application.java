package slack;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class Application {

    public static void main(String[] args) {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail job = newJob(alarmJob.class)
                    .withIdentity("alarmJob", Scheduler.DEFAULT_GROUP)
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("triggerName", Scheduler.DEFAULT_GROUP)
                    .withSchedule(cronSchedule("30 * * * * ?"))
                    .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
