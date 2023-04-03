package com.imooc.activitiweb;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
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
                .name("请假申请")
                .addClasspathResource("resources/bpmn/evection.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署ID:" + deployment.getId());

        System.out.println("流程部署name:" + deployment.getName());

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
        System.out.println("流程定义id" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id" + processInstance.getId());
        System.out.println("当前活动id" + processInstance.getActivityId());

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
        String person = "rose";

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

        String taskAssignee = "Jerry";
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


    @Test
    public void queryProcessDefinition() {
        // 获取引擎
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取service
        final RepositoryService repositoryService = processEngine.getRepositoryService();

        final List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process_1")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        for (ProcessDefinition processDefinition : list) {
            System.out.println("id：" + processDefinition.getId());
            System.out.println("key：" + processDefinition.getKey());
            System.out.println("name：" + processDefinition.getName());
            System.out.println("suspend:" + processDefinition.isSuspended());
            System.out.println("version:" + processDefinition.getVersion());
        }

    }


    /**
     * 删除流程部署信息
     */
    @Test
    public void deleteDeployment() {
        // 获取引擎
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取service
        final RepositoryService repositoryService = processEngine.getRepositoryService();

        String id = "bb9b7032-d16d-11ed-8802-b21d15b421eb";
        // 如果一个实例没有跑完则可以采用级联删除
        repositoryService.deleteDeployment(id, true);
    }


    @Test
    public void getDeployment() throws IOException {
        // 获取引擎
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取service
        final RepositoryService repositoryService = processEngine.getRepositoryService();
        // 查询流程定义信息
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process_1")
                .singleResult();

        final String deploymentId = processDefinition.getDeploymentId();
        final String resourceName = processDefinition.getResourceName();

        final InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);

        File file = new File("/Users/wuzhixuan/Java/project/activiti7-workflow/src/main/resources/resources/uploadfile/ja.bpmn");
        FileOutputStream fileOutputStream = new FileOutputStream(file);


        IOUtils.copy(resourceAsStream, fileOutputStream);


        resourceAsStream.close();
        fileOutputStream.close();
    }


    @Test
    public void findHistoryInfo() {
        // 获取引擎
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取service
        final HistoryService historyService = processEngine.getHistoryService();

        final List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId("284a5e01-d173-11ed-9452-b21d15b421eb")
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println(historicActivityInstance.getActivityType());
            System.out.println(historicActivityInstance.getActivityName());
            System.out.println(historicActivityInstance.getProcessDefinitionId());
            System.out.println(historicActivityInstance.getProcessInstanceId());
            System.out.println("=============");
        }


    }


    /**
     * 全部流程实例的挂起和激活
     */
    @Test
    public void suspendAllProcessInstance() {
        // 获取引擎
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // repositoryService
        final RepositoryService repositoryService = processEngine.getRepositoryService();

        // 查询流程定义
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process_1")
                .singleResult();


        final boolean suspended = processDefinition.isSuspended();
        System.out.println(suspended);

        // 获取流程定义id
        final String id = processDefinition.getId();
        // 如果是挂起改为激活，如果是激活改成挂起
        if (suspended) {
            repositoryService.activateProcessDefinitionById(id, true, null);
            System.out.println("流程定义ID"+id+"已激活");
        } else {
            repositoryService.suspendProcessDefinitionById(id, true, null);
            System.out.println("流程定义ID"+id+"已挂起");
        }

    }


    /**
     * 暂停单个流程实例
     */
    @Test
    public void suspendSingleProcessInstance() {
        // 获取引擎
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // repositoryService
        final RuntimeService runtimeService = processEngine.getRuntimeService();

        // 查询流程定义
        final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("ba5be4a6-d234-11ed-91e0-be3e7e9e5943")
                .singleResult();


        final boolean suspended = processInstance.isSuspended();
        System.out.println(suspended);

        // 获取流程定义id
        final String id = processInstance.getId();
        // 如果是挂起改为激活，如果是激活改成挂起
        if (suspended) {
            runtimeService.activateProcessInstanceById(id);
            System.out.println("流程实例ID"+id+"已激活");
        } else {
            runtimeService.suspendProcessInstanceById(id);
            System.out.println("流程瑟实例ID"+id+"已挂起");
        }

    }


}
