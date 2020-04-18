package com.konoha.votacao.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 
 * Por default a notação @EnableScheduling usada na @SpringSchedulingApplication usa
 * apenas uma thread para execução das tarefas. Nesta classe é configurado o scheduler
 * para utilizar mais de uma thread.
 * 
 * @author jovani.brasil
 *
 */
public class ScheduleConfig implements SchedulingConfigurer {

	@Value("${thread.pool.size}")
	private int POOL_SIZE;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		
		// gerenciamento de threads, delegação de tarefas e implementa TaskExecutor interface
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		
		scheduler.setPoolSize(POOL_SIZE);
		scheduler.setThreadNamePrefix("votacao-schedule-task-pool-");
		scheduler.initialize();
		
		taskRegistrar.setTaskScheduler(scheduler);
	}
	
}
