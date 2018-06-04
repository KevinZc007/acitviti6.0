package pers.zc.activiti.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zc 2018-06-04
 */
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	@RequestMapping("helloWorld")  
    public void helloWorld() {  
  
        //根据bpmn文件部署流程  
        Deployment deploy = repositoryService.createDeployment()
        									.addClasspathResource("TestProcess.bpmn")
        									.deploy();  
        //获取流程定义  
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();  
        //启动流程定义，返回流程实例  
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());  
        String processId = pi.getId();  
        System.out.println("流程创建成功，当前流程实例ID："+processId);  
          
        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();  
        System.out.println("执行前，任务名称："+task.getName());  
        taskService.complete(task.getId());  
  
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();  
        System.out.println("task为null，任务执行完毕："+task);  
	}
}
