package com.imooc.activitiweb.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author: wuzhixuan
 * @date 2023/04/04 00:34
 * @Version 1.0
 */
public class TestTaskListener implements TaskListener {


    /**
     * 指定负责人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {

        if (delegateTask.getName().equals("创建申请")) {
            delegateTask.setAssignee("吴志旋");
        } else {
            delegateTask.setAssignee("wuzhixuan");
        }




    }
}
