package com.liwei.design.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
    @Autowired
    private MyJobFactory myJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactory(Trigger deleteTrashTrigger, Trigger deleteShareTrigger) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(myJobFactory);
        schedulerFactoryBean.setTriggers(deleteTrashTrigger, deleteShareTrigger);
        return schedulerFactoryBean;
    }

    @Bean("deleteTrashDetail")
    public JobDetail deleteTrashDetail() {
        return JobBuilder.newJob(DeleteTrashTask.class).withIdentity("deleteTrashTask")
                .storeDurably().build();
    }

    @Bean(name = "deleteTrashTrigger")
    public CronTriggerFactoryBean deleteTrashTrigger(JobDetail deleteTrashDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(deleteTrashDetail);
        trigger.setCronExpression("0 0 9 * * ?");
        trigger.setName("deleteTrigger");// trigger的name
        return trigger;

    }

    @Bean("deleteShareDetail")
    public JobDetail deleteShareDetail() {
        return JobBuilder.newJob(DeleteShareTask.class).withIdentity("deleteShareTask")
                .storeDurably().build();
    }

    @Bean(name = "deleteShareTrigger")
    public CronTriggerFactoryBean deleteShareTrigger(JobDetail deleteShareDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(deleteShareDetail);
        trigger.setCronExpression("0 0 9 * * ?");
        trigger.setName("shareTrigger");// trigger的name
        return trigger;

    }
}
