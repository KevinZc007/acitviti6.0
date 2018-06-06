package pers.zc.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@RequestMapping("singleAssignee")  
	public void setSingleAssignee() {  
		
		//根据bpmn文件部署流程  
		repositoryService.createDeployment().addClasspathResource("singleAssignee.bpmn").deploy();
		// 设置User Task1受理人变量
		Map<String, Object> variables = new HashMap<>();
		variables.put("user1", "007");
		//采用key来启动流程定义并设置流程变量，返回流程实例  
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("singleAssignee", variables);  
		String processId = pi.getId();  
		System.out.println("流程创建成功，当前流程实例ID："+processId);
		
		// 注意 这里需要拿007来查询，key-value需要拿value来获取任务
		List<Task> list = taskService.createTaskQuery().taskAssignee("007").list();
		if(list!=null && list.size()>0){ 
            for(org.activiti.engine.task.Task task:list){  
                System.out.println("任务ID："+task.getId());  
                System.out.println("任务的办理人："+task.getAssignee());  
                System.out.println("任务名称："+task.getName());  
                System.out.println("任务的创建时间："+task.getCreateTime());  
                System.out.println("流程实例ID："+task.getProcessInstanceId());  
                System.out.println("#######################################");
            }
        }
		
		// 设置User Task2的受理人变量
		Map<String, Object> variables1 = new HashMap<>();
		variables1.put("user2", "Kevin");
		// 因为007只有一个代办的任务，直接完成任务，并赋值下一个节点的受理人user2为Kevin办理
		taskService.complete(list.get(0).getId(), variables1);
		System.out.println("User Task1被完成了，此时流程已流转到User Task2");
	}
	
	@RequestMapping("multiAssignee")  
	public void setMultiAssignee() {  
		
		//根据bpmn文件部署流程  
		repositoryService.createDeployment().addClasspathResource("MultiAssignee.bpmn").deploy();
		// 设置多个处理人变量 这里设置了三个人
		Map<String, Object> variables = new HashMap<>();
		List<String> userList = new ArrayList<>();
		userList.add("user1");
		userList.add("user2");
		userList.add("user3");
		variables.put("userList", userList);
		//采用key来启动流程定义并设置流程变量，返回流程实例  
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("multiAssigneeProcess", variables);  
		String processId = pi.getId();  
		System.out.println("流程创建成功，当前流程实例ID："+processId);
		
		// 查看user1的任务
		List<Task> list = taskService.createTaskQuery().taskAssignee("user1").list();
		if(list!=null && list.size()>0){ 
            for(org.activiti.engine.task.Task task:list){  
                System.out.println("任务ID："+task.getId());  
                System.out.println("任务的办理人："+task.getAssignee());  
                System.out.println("任务名称："+task.getName());  
                System.out.println("任务的创建时间："+task.getCreateTime());  
                System.out.println("流程实例ID："+task.getProcessInstanceId());  
                System.out.println("#######################################");
            }
        }
		
		// 查看user2的任务
		List<Task> list2 = taskService.createTaskQuery().taskAssignee("user2").list();
		if(list2!=null && list2.size()>0){ 
            for(org.activiti.engine.task.Task task:list2){  
                System.out.println("任务ID："+task.getId());  
                System.out.println("任务的办理人："+task.getAssignee());  
                System.out.println("任务名称："+task.getName());  
                System.out.println("任务的创建时间："+task.getCreateTime());  
                System.out.println("流程实例ID："+task.getProcessInstanceId());  
                System.out.println("#######################################");
            }
        }
				
		// 查看user3的任务
		List<Task> list3 = taskService.createTaskQuery().taskAssignee("user3").list();
		if(list3!=null && list3.size()>0){ 
            for(org.activiti.engine.task.Task task:list3){  
                System.out.println("任务ID："+task.getId());  
                System.out.println("任务的办理人："+task.getAssignee());  
                System.out.println("任务名称："+task.getName());  
                System.out.println("任务的创建时间："+task.getCreateTime());  
                System.out.println("流程实例ID："+task.getProcessInstanceId());  
                System.out.println("#######################################");
            }
        }
		
	}
}
