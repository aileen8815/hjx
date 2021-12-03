package com.wwyl.controller.settings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.wwyl.Enums;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.security.Operator;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.Task;
import com.wwyl.service.settings.TaskService;

/**
 * @author fyunli
 */
@Controller
public class TaskController extends BaseController {

    @Resource
    private TaskService taskService;

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, ModelMap modelMap) {
        Task task = taskService.findOne(id);
        // 阅后即焚
        taskService.updateTaskStatus(id, Enums.TaskStatus.已读);
        modelMap.addAttribute("task", task);
        return "/task/task_view";
    }

    @RequestMapping(value = "/task/index", method = RequestMethod.GET)
    public String index(
            @RequestParam(value = "taskMode", defaultValue = "工作通知") String taskMode,
            ModelMap modelMap) {
        modelMap.addAttribute("taskMode", taskMode);
        return "/task/task_list";
    }

    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(value = "taskMode", defaultValue = "工作通知") String taskMode,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows) {
        Page<Task> tasks = taskService.findTasksByOperator(ThreadLocalHolder.getCurrentOperator().getId(), Enums.TaskMode.valueOf(taskMode), page, rows);
        return this.toEasyUiDatagridResult(tasks);
    }

    @RequestMapping(value = "/ajax/notice")
    public String ajaxNotice(ModelMap model) {
        List<Task> notices;
        if (ThreadLocalHolder.getCurrentOperator() != null) {
            notices = taskService.findActiveTasksByOperator(ThreadLocalHolder.getCurrentOperator().getId(), Enums.TaskMode.预警提醒);
        } else {
            notices = new ArrayList<Task>();
        }
        model.addAttribute("notices", notices);
        return "/ajax/notice";
    }

    @RequestMapping(value = "/ajax/task")
    public String ajaxTask(ModelMap model) {
        List<Task> tasks;
        if (ThreadLocalHolder.getCurrentOperator() != null) {
            tasks = taskService.findActiveTasksByOperator(ThreadLocalHolder.getCurrentOperator().getId(), Enums.TaskMode.工作通知);
        } else {
            tasks = new ArrayList<Task>();
        }
        model.addAttribute("tasks", tasks);
        return "/ajax/task";
    }

    @RequestMapping("/task/mock")
    public String mock(ModelMap modelMap) {
        Task task = new Task();
        task.setContent("content");
        task.setSubject("mock task");
        task.setTaskMode(Enums.TaskMode.工作通知);
        task.setTaskStatus(Enums.TaskStatus.未读);
        task.setTaskTime(new Date());
        task.setTaskType(Enums.TaskType.其他);
        task.setUrl("http://www.baidu.com");// 结合对应业务单填入URL，如入库单对应URL
        taskService.assignTask(task, ThreadLocalHolder.getCurrentOperator());
        modelMap.addAttribute("message", "任务分派成功");
        return "/message";
    }

    @RequestMapping("/task/mock-multi")
    public String mockMulti(ModelMap modelMap) {
        Task task = new Task();
        task.setContent("content");
        task.setSubject("mock notice");
        task.setTaskMode(Enums.TaskMode.预警提醒);
        task.setTaskStatus(Enums.TaskStatus.未读);
        task.setTaskTime(new Date());
        task.setTaskType(Enums.TaskType.其他);
        task.setUrl("http://www.baidu.com");// 结合对应业务单填入URL，如入库单对应URL
        taskService.assignTask(task, "2");
        modelMap.addAttribute("message", "任务分派成功");
        return "/message";
    }

    @RequestMapping(value = "/task/clear", produces = "application/json")
    @ResponseBody
    public Map<String, Object> clear(Long[] ids) {
        taskService.updateTaskStatus(ids, Enums.TaskStatus.已读);
        return this.printMessage(0, "标记为已读成功");
    }

    @RequestMapping(value = "/task/delete", produces = "application/json")
    @ResponseBody
    public Map<String, Object> delete(Long[] ids) {
        taskService.delete(ids);
        return this.printMessage(0, "删除成功");
    }

    @RequestMapping(value = "/task/clear-all", produces = "application/json")
    @ResponseBody
    public Map<String, Object> clearAll(String taskMode) {
        taskService.clearAllTasks(Enums.TaskMode.valueOf(taskMode));
        return this.printMessage(0, "标记为已读成功");
    }

}
