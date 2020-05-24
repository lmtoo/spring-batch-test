package cn.lmtoo.batch.launcher;


import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    @Qualifier("jobLauncherDemoJob")
    private Job jobLauncherDemoJob;

    @Scheduled(fixedDelay = 5000)
    public void scheduler() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobParametersNotFoundException, NoSuchJobException {
        jobOperator.startNextInstance("jobScheduledDemoJob");
    }

    @GetMapping("/job/{msg}")
    public String jobRun1(@PathVariable String msg) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParametersBuilder()
                .addString("msg", msg)
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(jobLauncherDemoJob, parameters);
        return jobExecution.getExitStatus().toString();
    }

    @GetMapping("/job2/{msg}")
    public String jobRun2(@PathVariable String msg) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobInstanceAlreadyExistsException, NoSuchJobException {
        jobOperator.start("jobLauncherDemoJob","msg="+msg);
        return "Job success";
    }
}
