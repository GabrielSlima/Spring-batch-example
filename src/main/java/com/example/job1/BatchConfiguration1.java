package com.example.job1;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;

import org.springframework.batch.item.database.JdbcBatchItemWriter;

import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableBatchProcessing
@Import(value = {Job1Steps.class})
public class BatchConfiguration1 extends DefaultBatchConfigurer {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Override
	  public void setDataSource(DataSource dataSource) {
	    // initialize will use a Map based JobRepository (instead of database)
	  }
	
	@Bean
	public Job job1 (Step step1, Step step2DeleteFiles) {
		return jobBuilderFactory.get("JOB1")
			.validator(validator1())
			.start(step1)
			.build();
	}

	private JobParametersValidator validator1() {
		
		return new JobParametersValidator () {

			@Override
			public void validate(JobParameters parameters) throws JobParametersInvalidException {
				//CHECK IF FILE EXISTS ETC.
				
			}
			
		};
	}
}
