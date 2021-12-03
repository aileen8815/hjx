package com.wwyl.controller.store;

import javax.annotation.Resource;

import com.wwyl.entity.store.BookInventory;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.store.BookInventoryService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wwyl.controller.BaseController;
import com.wwyl.service.settings.StoreLocationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fyunli
 */
@Controller
public class StoreLocationSelectorController extends BaseController {

    @Resource
    private StoreLocationService storeLocationService;
    @Resource
    private BookInventoryService bookInventoryService;
    @Resource
    private StoreAreaService storeAreaService;

    /**
     * @param customerId           // 需要显示库存的客户
     * @param allowSelectStatus    // 可选择的储位状态，多个以逗号分隔
     * @param showStock            // 显示库区平面，还是库存状态
     * @param selectedLocationCode // 已选择的储位，多个以逗号分隔
     * @param selectNoneStoreContainer     //托盘IDS,需要排除不显示的储位,多个以逗号分隔
     * @param model
     * @return
     */
    @RequestMapping(value = "/store/store-location-selector")
    public String location(ModelMap model, Long customerId, Long areaId, boolean showStock, String allowSelectStatus, String selectedLocationCode,String selectableStoreContainer, String allAreaBlank) {
        //如果没有条件，则默认只显示A1库区（为了解决，库区显示慢问题）
    	if (customerId == null && areaId == null && selectedLocationCode == null && selectableStoreContainer == null && allAreaBlank.equals("1")){
        	areaId = Long.valueOf(2);//A1
        }
    	if (StringUtils.isBlank(allowSelectStatus)) {
            allowSelectStatus = "1,3";//可使用、预留
        }

        List<Long> avaliabeAreaId = new ArrayList<Long>();
        if (customerId != null) {
            List<BookInventory> bookInventorys = bookInventoryService.findBookInventorySpecification(customerId, null, null, null, areaId,null,null);
            if (CollectionUtils.isNotEmpty(bookInventorys)) {
                String storeLocationCode = "";
                Set<String> selectableStoreContainersSet=new HashSet<String>();
                if(StringUtils.isNotBlank(selectableStoreContainer)){
                	String[] selectableStoreContainers=selectableStoreContainer.split(",");
                	for (String storeContainerid : selectableStoreContainers) {
                		selectableStoreContainersSet.add(storeContainerid);
					}
            	}
                for (BookInventory inventory : bookInventorys) {
                	if(!selectableStoreContainersSet.isEmpty()&&
                			!selectableStoreContainersSet.contains(inventory.getStoreContainer().getId().toString())){
                		continue;
                	}
                    storeLocationCode += inventory.getStoreLocationCode() + ",";

                    if (areaId == null) {
                        if (!avaliabeAreaId.contains(inventory.getStoreLocation().getStoreArea().getId())) {
                            avaliabeAreaId.add(inventory.getStoreLocation().getStoreArea().getId());
                        }
                    }
                }
                model.addAttribute("locationCode", storeLocationCode);
            }
        }

        if (areaId != null) {
            avaliabeAreaId.add(areaId);
        }
        Long[] areaIds = new Long[avaliabeAreaId.size()];
        for (int i = 0; i < areaIds.length; i++) {
            areaIds[i] = avaliabeAreaId.get(i);
        }

        model.addAttribute("locationStats", storeLocationService.findLocationStat(areaIds));
        model.addAttribute("showStock", showStock);
        model.addAttribute("allowSelectStatus", allowSelectStatus);
        model.addAttribute("selectedLocationCode", selectedLocationCode);
        model.addAttribute("defaultAreaId", (ArrayUtils.isNotEmpty(areaIds)) ? areaIds[0] : 0);
        model.addAttribute("storeAreas", storeAreaService.findAll());
        model.addAttribute("customerId", customerId);
        model.addAttribute("areaId", areaId);
        model.addAttribute("selectableStoreContainer", selectableStoreContainer);
        return "/store/store_location_selector";
    }

}
