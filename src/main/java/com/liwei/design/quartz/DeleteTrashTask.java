package com.liwei.design.quartz;

import com.liwei.design.model.Trash;
import com.liwei.design.repo.TrashRepository;
import com.liwei.design.service.FileService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class DeleteTrashTask extends QuartzJobBean {
    @Autowired
    private FileService fileService;
    @Autowired
    private TrashRepository dr;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("delete trash task start on " + new Date());
        List<Trash> list = dr.findAllByDateBefore30Days();
        for (Trash delete : list) {
            dr.deleteByPath(delete.getPath());
            fileService.deleteFile(delete.getPath());
        }
    }
}
