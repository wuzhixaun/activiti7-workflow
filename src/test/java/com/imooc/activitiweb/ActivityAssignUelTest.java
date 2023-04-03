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
public class ActivityAssignUelTest {


    public String processKey = "myevection1";
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
                .name("出差申请-uel")
                .addClasspathResource("resources/bpmn/evection-uel.bpmn")
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


        Map<String, Object> map = new HashMap<>();
        map.put("assignee0", "bajie");
        map.put("assignee1", "李经理");
        map.put("assignee2", "吴总经理");
        map.put("assignee3", "bajie");
        // 3.根据流程定义id启动流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, map);


        // 4.输出
        System.out.println("流程定义id" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id" + processInstance.getId());
        System.out.println("当前活动id" + processInstance.getActivityId());

    }

}
