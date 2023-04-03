package com.imooc.activitiweb;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wuzhixuan
 * @date 2023/04/04 00:16
 * @Version 1.0
 */
@SpringBootTest
public class ActivityListenerTest {


    public String processKey = "testListener";
    /**
     * 流程部署
     */

    @Test
    public void testDeployment() {
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2.获取Repository
        final RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.部署

        final Deployment deployment = repositoryService.createDeployment()
                .name("测试监听器")
                .addClasspathResource("resources/bpmn/testListener.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署ID:" + deployment.getId());

        System.out.println("流程部署name:" + deployment.getName());
    }


    /**
     * 启动一个流程实例
     */
    @Test
    public void testStartProcessUel() {

        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        final RuntimeService runtimeService = processEngine.getRuntimeService();


        // 3.根据流程定义id启动流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);


        // 4.输出
        System.out.println("流程定义id" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id" + processInstance.getId());
        System.out.println("当前活动id" + processInstance.getActivityId());

    }

}
