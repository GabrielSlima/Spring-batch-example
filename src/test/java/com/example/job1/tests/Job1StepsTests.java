package com.example.job1.tests;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.jupiter.api.BeforeEach;

import com.example.job1.BatchConfiguration1;
import com.example.job1.Job1Steps;

//@SpringBatchTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= {Job1StepsTests.BatchTestConfig.class})
public class Job1StepsTests {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
    
	@Test
	public void test() throws Exception {
		//JobParameters params = new JobParametersBuilder()
        //        .addString("importUserJob", String.valueOf(System.currentTimeMillis()))
        //        .toJobParameters();
		//JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();

		//jobLauncherTestUtils().launchStep("STEP1", params);
		JobExecution jobExce = jobLauncherTestUtils.launchJob();
		System.out.println(jobExce.getExitStatus().getExitCode());
	}

	@Configuration
	@Import({BatchConfiguration1.class})
	static class BatchTestConfig {

	    @Autowired
	    private Job job1;

	    @Bean
	    JobLauncherTestUtils jobLauncherTestUtils()
	        throws NoSuchJobException {
	      JobLauncherTestUtils jobLauncherTestUtils =
	          new JobLauncherTestUtils();
	      jobLauncherTestUtils.setJob(job1);

	      return jobLauncherTestUtils;
	    }
	  }
}
