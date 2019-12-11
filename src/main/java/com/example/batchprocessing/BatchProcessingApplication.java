package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication(scanBasePackages = { "com.example" })
@EnableScheduling
public class BatchProcessingApplication {
	
    @Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job1;
    
    @Autowired
    Job job2;
    
    @Autowired
	private ResourceLoader resourceLoader;

	@Value("file:${reprocess.path}*.log")
	private String filesFolder;
	
	public static Resource[] resourceList;
	
	@Autowired
	public BatchProcessingApplication(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BatchProcessingApplication.class, args);
	}
	
    @Scheduled(cron = "0 */1 * * * ?")
    public void executeJob1() throws Exception
    {
    	resourceList = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(filesFolder);
    	/*for(Resource r : resourceList) {
    		r.getFilename();
    	}*/
        JobParameters params = new JobParametersBuilder()
                .addString("importUserJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job1, params);
    }
    
    @Scheduled(cron = "0 */1 * * * ?")
    public void executeJob2() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("importUserJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job2, params);
    }
}
