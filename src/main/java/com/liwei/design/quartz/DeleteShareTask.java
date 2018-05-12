package com.liwei.design.quartz;

import com.liwei.design.model.Share;
import com.liwei.design.repo.ShareRepository;
import lombok.extern.log4j.Log4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

@Log4j
public class DeleteShareTask extends QuartzJobBean {
    @Autowired
    private ShareRepository sr;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("delete share task start on " + new Date());
        List<Share> list = sr.findAllByExpire();
        for (Share temp : list) {
            sr.deleteByUrl(temp.getPath());
        }
    }
}
