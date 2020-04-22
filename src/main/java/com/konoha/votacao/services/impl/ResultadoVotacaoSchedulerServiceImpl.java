package com.konoha.votacao.services.impl;

import java.time.LocalDateTime;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.konoha.votacao.exceptions.NotFoundException;
import com.konoha.votacao.mappers.VotoMapper;
import com.konoha.votacao.modelo.Pauta;
import com.konoha.votacao.schedule.ResultadoVotacaoTask;
import com.konoha.votacao.services.AsyncMessageService;
import com.konoha.votacao.services.ResultadoVotacaoSchedulerService;
import com.konoha.votacao.services.VotoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResultadoVotacaoSchedulerServiceImpl implements ResultadoVotacaoSchedulerService {

	// mantém um mapa com a relação dos uuids e dos schedulers
	private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();
	
	private final TaskScheduler taskRegistrar;
	private final AsyncMessageService asyncMessageService;
	private final VotoService votoService;
	private final VotoMapper votoMapper;
	
	public ResultadoVotacaoSchedulerServiceImpl(TaskScheduler taskRegistrar, AsyncMessageService asyncMessageService,
			@Lazy VotoService votoService, VotoMapper votoMapper) {
		super();
		this.taskRegistrar = taskRegistrar;
		this.asyncMessageService = asyncMessageService;
		this.votoService = votoService;
		this.votoMapper = votoMapper;
	}

	/**
	 * Escalona uma tarefa, neste caso o envio de uma mensagem para o
	 * serviço de mensageria.
	 * 
	 * @return
	 */
	@Override
	public UUID scheduleTask(Pauta pauta) {

		UUID taskUUID = UUID.randomUUID();
		
		Runnable task = new ResultadoVotacaoTask(taskUUID, pauta, asyncMessageService, votoService, votoMapper);
		
		LocalDateTime dataFechamento = pauta.getDataFechamento();
		String cronString = String.format("0 %s %s %s %s ?", 
				dataFechamento.getMinute(),
				dataFechamento.getHour(),
				dataFechamento.getDayOfMonth(),
				dataFechamento.getMonth().getValue());
		
		log.info("Task agendada para fechamento de sessão - CRON STRING: {} - ", cronString, dataFechamento);
		
		ScheduledFuture<?> future =  taskRegistrar.schedule(task, new CronTrigger(cronString));
		scheduledTasks.put(taskUUID, future);
		return taskUUID;
	}
	
	/**
	 * Remove uma tarefa do escalonador de tarefas.
	 * 
	 * @param taskUUID
	 */
	@Override
	public void removeTask(UUID taskUUID) {
		if(scheduledTasks.containsKey(taskUUID)) {
			ScheduledFuture<?> future = scheduledTasks.get(taskUUID);
			log.info("");
			future.cancel(true);
		}else {
			throw new NotFoundException("Tarefa não encontrada nas tarefas escalonáveis.");
		}
	}
		
}
