package com.imooc.activitiweb;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author: wuzhixuan
 * @date 2023/04/02 00:20
 * @Version 1.0
 */
@SpringBootTest
public class ActivitiDemo {


    @Test
    public void testDeployment() {


        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2.获取Repository
        final RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.部署

        final Deployment deployment = repositoryService.createDeployment()
                .name("测试")
                .addClasspathResource("resources/bpmn/test2.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署ID:"+deployment.getId());

        System.out.println("流程部署name:"+deployment.getName());

    }


    /**
     * 启动一个流程实例
     */
    @Test
    public void testStartProcess() {

        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        final RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.根据流程定义id启动流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        // 4.输出
        System.out.println("流程定义id"+processInstance.getProcessDefinitionId());
        System.out.println("流程实例id"+processInstance.getId());
        System.out.println("当前活动id"+processInstance.getActivityId());

    }


    /**
     * 个人任务查询
     */
    @Test
    public void testFindPersonTaskList() {

        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.getTaskService
        final TaskService taskService = processEngine.getTaskService();
        String person = "张三";

        final List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("Process_1")
                .taskAssignee(person)
                .list();


        for (Task task : taskList) {

            System.out.println("流程实例id:" + task.getProcessInstanceId());
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务负责人:" + task.getAssignee());
            System.out.println("任务名称:" + task.getName());
        }


    }


    /**
     * 完成任务
     */
    @Test

    public void completTask() {
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.getTaskService
        final TaskService taskService = processEngine.getTaskService();
        // 完成任务
//        taskService.complete("bb0c41a5-d0af-11ed-8e1f-5202c415eaf9");

        String taskAssignee = "rose";
        // 获取jerry任务一
        final Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process_1")
                .taskAssignee(taskAssignee)
                .singleResult();
        System.out.println("流程实例id:" + task.getProcessInstanceId());
        System.out.println("任务ID:" + task.getId());
        System.out.println("任务负责人:" + task.getAssignee());
        System.out.println("任务名称:" + task.getName());
        taskService.complete(task.getId());

    }


    @Test
    public void deployProcessByZip() {
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取repo
        final RepositoryService repositoryService = processEngine.getRepositoryService();

        // 读取资源包文件，构造inputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("resources/bpmn/test.zip");

        // 用inputStream构造ZIp
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        // 流程部署
        final Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();

        System.out.println("流程部署id:" + deploy.getId());
        System.out.println("流程部署name:" + deploy.getName());


    }

}
