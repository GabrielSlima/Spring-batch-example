package com.example.job1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import com.example.batchprocessing.BatchProcessingApplication;

class PersonItemProcessor implements ItemProcessor<Person, Person> {

	  private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	  @Override
	  public Person process(final Person person) throws Exception {
	    final String firstName = person.getName().toUpperCase();
	    final String lastName = person.getLastName().toUpperCase();

	    final Person transformedPerson = new Person(firstName, lastName);

	    log.info("Converting (" + person + ") into (" + transformedPerson + ")");

	    return transformedPerson;
	  }

	}

class ConsoleItemWriter<T> implements ItemWriter<T> { 
    @Override
    public void write(List<? extends T> items) throws Exception { 
        System.out.println(items);
    } 
}

public class Job1Steps {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	public static Resource[] resourceList;
	
	//@Value("file:${reprocess.path}*.log")
	//private String filesFolder;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	//@Value("file:${reprocess.path}*.log")
	//private Resource[] inputResources;
	
	@Value("${reprocess.path}")
	private String path;

	private Resource[] resources;
	
	@Autowired
	private StepListener stepListener;
	
	@Bean
	public FlatFileItemReader<Person> reader() throws UnexpectedInputException, ParseException, Exception {	  
		FlatFileItemReader<Person> itemReader = new FlatFileItemReader<Person>();
		itemReader.setLineMapper(new DefaultLineMapper<Person>() {{
			setLineTokenizer(new DelimitedLineTokenizer(";") {{
				setNames(new String[]{"name", "lastName"});
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Person> () {{
				setTargetType(Person.class);
			}});
		}});
	return itemReader;
	}
	
	private LineMapper<Person> lineMapper() {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	    lineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
	    //lineTokenizer.setQuoteCharacter(DelimitedLineTokenizer.DEFAULT_QUOTE_CHARACTER);
	    lineTokenizer.setStrict(false);
	    //lineTokenizer.setNames();

	    BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
	    fieldSetMapper.setTargetType(Person.class);

	    DefaultLineMapper<Person> defaultLineMapper = new DefaultLineMapper<Person>();
	    defaultLineMapper.setLineTokenizer(lineTokenizer);
	    defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	    return defaultLineMapper;
	}
	
	@Bean(name = "multiResourceReader")
	@StepScope
	public MultiResourceItemReader<Person> multiResourceItemReader() throws UnexpectedInputException, ParseException, Exception {
	    MultiResourceItemReader<Person> resourceItemReader = new MultiResourceItemReader<Person>();
	    //resourceItemReader.setResources(BatchProcessingApplication.resourceList == null ? inputResources : BatchProcessingApplication.resourceList);	    
	    resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("file:c:/Users/gslima/Documents/transactionHistory/*.log");
	    resourceItemReader.setResources(resources);
	    resourceItemReader.setDelegate(reader());
	    return resourceItemReader;
	 }

	@Bean("processor")
	public PersonItemProcessor processor() {
	  return new PersonItemProcessor();
	}
	
	@Bean
	public ConsoleItemWriter<Person> writer() 
	{
	    return new ConsoleItemWriter<Person>();
	}
	
	
	public StepListener stepListener() 
	{
	    return stepListener;
	}
	
	@Bean
	public Step step1() throws UnexpectedInputException, ParseException, Exception {
		return stepBuilderFactory
				.get("STEP1").<Person, Person>chunk(4)
				.reader(multiResourceItemReader())
				.writer(writer())
				.processor(processor()).listener(stepListener())
			.build();
	}
	
	@Bean
	public Step step2DeleteFiles() throws UnexpectedInputException, ParseException, Exception {
		FileDeletingTasklet task = new FileDeletingTasklet();
		//task.setResources(BatchProcessingApplication.resourceList == null ? inputResources : BatchProcessingApplication.resourceList);
		task.setResources(resources);
		return stepBuilderFactory
				.get("DeleteFiles")
				.tasklet(task)
				.build();
	}
	
	/*@Bean
	@StepScope
	public Step stepDefineContext() throws UnexpectedInputException, ParseException, Exception {
		SetResourcesAsParameterContext task = new SetResourcesAsParameterContext();
		return stepBuilderFactory
				.get("prepareJobcontext")
				.tasklet(task)
				.build();
	}*/
}
