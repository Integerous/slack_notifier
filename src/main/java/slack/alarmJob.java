package slack;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class alarmJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        BotController botController = new BotController();
        botController.runSlackBot();
    }
}
