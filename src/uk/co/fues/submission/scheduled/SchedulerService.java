package uk.co.fues.submission.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import uk.co.fues.submission.classifier.ModelBuilder;

/**
 * Scheduler for handling jobs
 */
@Service
public class SchedulerService {

	Logger log = LoggerFactory.getLogger(getClass());

	//@Autowired
	//@Qualifier("modelBuilder")
	ModelBuilder model;

	@Scheduled(fixedRate=3600000) // an hour between invocations
	public void doSchedule() {
		log.debug("Start schedule");		
		model.job();
		log.debug("End schedule");
	}
	
	public static void main(String [] args) {
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	    SchedulerService runner = ctx.getBean(SchedulerService.class);
	    runner.model = new ModelBuilder();
	    ctx.getBeanDefinitionCount();
	    String [] beans = ctx.getBeanDefinitionNames();
	    runner.doSchedule();
	}

}
