package com.wwyl.controller;

import javax.annotation.Resource;

import com.wwyl.Enums;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.Task;
import com.wwyl.service.settings.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wwyl.service.settings.StoreAreaService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fyunli
 */
@Controller
public class DashboardController extends BaseController {

	@Resource
	private StoreAreaService storeAreaService;
    @Resource
    private TaskService taskService;

	@RequestMapping("/dashboard")
	public String execute(ModelMap model) {
        // 以根库区为档位统计
        Map<String, List> rootAreaStats = new LinkedHashMap<String, List>();
        List<StoreArea> rootAreas = storeAreaService.findRootStoreAreas();
        for(StoreArea rootArea: rootAreas){
            List areaStats = storeAreaService.findAreaStat(new Long[]{rootArea.getId()});
            if(CollectionUtils.isNotEmpty(areaStats)){
                rootAreaStats.put(rootArea.getName(), areaStats);
            }
        }
		model.addAttribute("rootAreaStats", rootAreaStats);

        List<Task> notices = taskService.findActiveTasksByOperator(ThreadLocalHolder.getCurrentOperator().getId(), Enums.TaskMode.预警提醒);
        List<Task> tasks = taskService.findActiveTasksByOperator(ThreadLocalHolder.getCurrentOperator().getId(), Enums.TaskMode.工作通知);
        model.addAttribute("notices", notices);
        model.addAttribute("tasks", tasks);
		return "/dashboard";
	}

    @RequestMapping("/msg")
    public String msg(ModelMap model) {
        model.addAttribute("message", "This is a test message");
        return "/message";
    }

}
