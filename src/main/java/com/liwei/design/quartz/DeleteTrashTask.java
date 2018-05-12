package com.liwei.design.quartz;

import com.liwei.design.model.Trash;
import com.liwei.design.repo.TrashRepository;
import com.liwei.design.service.FileService;
import lombok.extern.log4j.Log4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

@Log4j
public class DeleteTrashTask extends QuartzJobBean {
    @Autowired
    private FileService fileService;
    @Autowired
    private TrashRepository dr;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("delete trash task start on " + new Date());
        List<Trash> list = dr.findAllByDateBefore30Days();
        for (Trash delete : list) {
            dr.deleteByPath(delete.getPath());
            fileService.deleteFile(delete.getPath());
        }
    }
}
