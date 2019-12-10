package br.com.ericsson.flex.flexBatch.transactionHistory;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBatchTest
@ContextConfiguration(classes=TransactionHistoryJobConfiguration.class)
public class TransactionHistoryReaderTeste {

	 @Autowired
	 private JobLauncherTestUtils jobLauncherTestUtils;

	 //@Autowired
	 //Job transactionHistory;

	@Test
	public void test() throws Exception {
		jobLauncherTestUtils.launchJob();
		jobLauncherTestUtils.launchStep("readTransactionHistoryFile");
	}

}
