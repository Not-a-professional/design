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
    public SchedulerFactoryBean schedulerFactory(Trigger deleteTrashTriger) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(myJobFactory);
        schedulerFactoryBean.setTriggers(deleteTrashTriger);
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
        trigger.setCronExpression("0/6 * * * * ?");// 表示每隔6秒钟执行一次
        trigger.setName("deleteTrigger");// trigger的name
        return trigger;

    }
}
