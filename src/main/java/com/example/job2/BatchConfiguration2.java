package com.example.job2;

import org.springframework.batch.core.Job;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
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

@Configuration
@EnableBatchProcessing
public class BatchConfiguration2 {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job2 (Step step2) {
		return jobBuilderFactory.get("JOB2")
			.validator(validator1())
			.start(step2)
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

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("STEP2")
			.tasklet(new Tasklet() {
				
				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("JOB2");
					return RepeatStatus.FINISHED;
				}
			})
			.build();
	}
}
