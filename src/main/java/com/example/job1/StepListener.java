package com.example.job1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component
public class StepListener implements StepExecutionListener, ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(StepListener.class);

	@Autowired
	private ResourceLoader resourceLoader;
	
	private Resource[] resources;
	private ApplicationContext applicationContexta;
	private String filePattern;
	private MultiResourceItemReader<Person> multiResourceItemReader;
	  
	@Value("${reprocess.path}")
	private String path;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContexta = applicationContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforeStep(StepExecution stepExecution) {
		multiResourceItemReader = (MultiResourceItemReader<Person>) this.applicationContexta.getBean("multiResourceReader");
		try {
			resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("file:c:/Users/gslima/Documents/transactionHistory/*.log");
			multiResourceItemReader.setResources(resources);
		} catch (Exception e) {
			log.info("Não deu amigo");
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)
	            && stepExecution.getStatus().equals(BatchStatus.COMPLETED)
	            && resources.length > 0) {

	      for (Resource resource : resources) {
	        try {
	          Path oldFile = Paths.get(resource.getFile().getAbsolutePath());
	          Path newFile = Paths.get(resource.getFile().getAbsolutePath() + ".processed");
	          Files.copy(oldFile, newFile);
	          new File(resource.getFile().getAbsolutePath()).delete();
	        } catch (IOException ex) {
	          log.info("Encountered problem when trying to remove the processed file(s)");
	        }
	      }
	    }
		

		multiResourceItemReader.setResources(new Resource[0]);
	    return stepExecution.getExitStatus();
	}
	
	 public void setFilePattern(String filePattern) {
		    this.filePattern = filePattern;
	 }
}
