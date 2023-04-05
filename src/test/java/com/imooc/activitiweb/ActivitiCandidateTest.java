package com.imooc.activitiweb;

import com.imooc.activitiweb.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wuzhixuan
 * @date 2023/04/05 22:49
 * @Version 1.0
 */

@SpringBootTest
public class ActivitiCandidateTest {


    public String key = "evection-candidate";


    @Test
    public void testDeployment() {


        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2.获取Repository
        final RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.部署

        final Deployment deployment = repositoryService.createDeployment()
                .name("出差申请-candidate")
                .addClasspathResource("resources/bpmn/evection-candidate.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署ID:" + deployment.getId());

        System.out.println("流程部署name:" + deployment.getName());

    }


    @Test
    public void deleteDeployment() {


        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2.获取Repository
        final RepositoryService repositoryService = processEngine.getRepositoryService();

        // 删除
        repositoryService.deleteDeployment("01c36a44-d30e-11ed-9811-9ad61403dce5",true);
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
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        // 4.输出
        System.out.println("流程定义id" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id" + processInstance.getId());
        System.out.println("当前活动id" + processInstance.getActivityId());

    }


    @Test
    public void findGroupTaskList() {
        // 任务候选人

        String candidateUser = "wangwu";
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        final TaskService taskService = processEngine.getTaskService();

        // 查询组任务
        final List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)
                .list();

        for (Task task : taskList) {

            System.out.println("-----------");
            System.out.println("流程实例id:"+task.getProcessInstanceId());
            System.out.println("任务Id:" + task.getId());
            System.out.println("任务负责人:" + task.getAssignee());

        }
    }


    /**
     * 拾取任务
     */
    @Test
    public void claimTask() {
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.getTaskService
        final TaskService taskService = processEngine.getTaskService();

        // 当前任务的id
        String candidateUser = "wangwu";
        // 查询组任务
        final Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)
                .singleResult();


        if (null != task) {
            taskService.claim(task.getId(), candidateUser);
            System.out.println("taskID:" + task.getId() + "被拾取：" + candidateUser);
        }

    }


    @Test
    public void testAssigneeToGroupTask() {
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.getTaskService
        final TaskService taskService = processEngine.getTaskService();

        // 当前任务的id
        String assignee = "wangwu";
        // 查询组任务
        final Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();


        if (null != task) {
            taskService.setAssignee(task.getId(), null);
            System.out.println("taskID:" + task.getId() + "被归还：");
        }
    }


    @Test
    public void testAssigneeToCandidateUser() {
        // 1.获取ProcessEngine
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.getTaskService
        final TaskService taskService = processEngine.getTaskService();

        // 当前任务的id
        String assignee = "wangwu";
        // 查询组任务
        final Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        String candidateUser = "lisi";

        if (null != task) {
            taskService.setAssignee(task.getId(), candidateUser);
            System.out.println("taskID:" + task.getId() + "交接任务：" + candidateUser);
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

        String taskAssignee = "wuzhixuan";
        // 获取jerry任务一
        final Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(taskAssignee)
                .singleResult();
        System.out.println("流程实例id:" + task.getProcessInstanceId());
        System.out.println("任务ID:" + task.getId());
        System.out.println("任务负责人:" + task.getAssignee());
        System.out.println("任务名称:" + task.getName());


        Map<String, Object> map = new HashMap<>();
        Evection evection = new Evection();
        evection.setNum(2d);
        map.put("evection", evection);
        map.put("assignee0", "bajie");

        taskService.complete(task.getId(), map);

    }
}
