package com.example.job1;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

//Tasklets são Steps que guardam um algoritmo simples, como deletar um arquivo, é um tipo "GENERICO"
public class FileDeletingTasklet implements Tasklet {

	private Resource[] resources;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		for(Resource resource : resources) {
			File file = resource.getFile();
			boolean deleted = file.delete();
			if(!deleted) {
				throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
			}
		}
		return RepeatStatus.FINISHED;
	}
	
	public void setResources(Resource[] resources) {
		this.resources = resources;
	}
	
	public void afterPropertiesSet() throws Exception {
        Assert.notNull(resources, "directory must be set");
    }
}
