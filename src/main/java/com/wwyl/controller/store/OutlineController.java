package com.wwyl.controller.store;

import javax.annotation.Resource;

import com.wwyl.entity.settings.StoreArea;
import com.wwyl.service.store.InboundBookingService;
import com.wwyl.service.store.OutboundBookingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wwyl.controller.BaseController;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;

import java.util.Date;
import java.util.List;

/**
 * @author fyunli
 */
@Controller
public class OutlineController extends BaseController {

    @Resource
    private StoreAreaService storeAreaService;
    @Resource
    private StoreLocationService storeLocationService;
    @Resource
    private InboundBookingService inboundBookingService;
    @Resource
    private OutboundBookingService outboundBookingService;

    @RequestMapping("/store/outline/area")
    public String area(ModelMap model) {
        model.addAttribute("areaStats", storeAreaService.findAreaStat(null));
        return "/store/outline_area";
    }

    @RequestMapping("/store/outline/locations")
    public String location(ModelMap model, Long[] areaId, String locationCode, boolean showStock, String allowSelectStatus, String selectedLocationCode) {
        model.addAttribute("storeAreas", storeAreaService.findAll());
        model.addAttribute("locationStats", storeLocationService.findLocationStat(areaId));
        model.addAttribute("showStock", showStock);
        model.addAttribute("allowSelectStatus", allowSelectStatus);
        model.addAttribute("selectedLocationCode", selectedLocationCode);
        model.addAttribute("defaultAreaId", (ArrayUtils.isNotEmpty(areaId)) ? areaId[0] : 0);
        model.addAttribute("locationCode", locationCode);
        return "/store/outline_location";
    }

    @RequestMapping("/store/outline/quickview")
    public String quickview(ModelMap model, Long[] areaId, String locationCode, boolean showStock, String allowSelectStatus, String selectedLocationCode) {
        model.addAttribute("storeAreas", storeAreaService.findAll());
        model.addAttribute("locationStats", storeLocationService.findLocationStat(areaId));
        model.addAttribute("showStock", showStock);
        model.addAttribute("allowSelectStatus", allowSelectStatus);
        model.addAttribute("selectedLocationCode", selectedLocationCode);
        model.addAttribute("defaultAreaId", (ArrayUtils.isNotEmpty(areaId)) ? areaId[0] : 0);
        model.addAttribute("locationCode", locationCode);
        return "/store/outline_quickview";
    }

    /**
     * 指定商品可存放库区预览
     *
     * @param modelMap
     * @param productIds
     * @return
     */
    @RequestMapping("/ajax/outline/booking")
    public String product(ModelMap modelMap, Long[] productIds, Date startDate, Date endDate) {
        if (ArrayUtils.isNotEmpty(productIds)) {
            List<StoreArea> storeAreas = storeAreaService.findByProduct(productIds);
            if (CollectionUtils.isNotEmpty(storeAreas)) {
                Long[] areaIds = new Long[storeAreas.size()];
                int i = 0;
                for (StoreArea storeArea : storeAreas) {
                    areaIds[i++] = storeArea.getId();
                }
                modelMap.addAttribute("areaStats", storeAreaService.findAreaStat(areaIds, false));
                modelMap.addAttribute("inboundStats", inboundBookingService.findInboundBookingStat(areaIds, startDate, endDate));
                modelMap.addAttribute("outboundStats", outboundBookingService.findOutboundBookingStat(areaIds, startDate, endDate));
            }
        }

        return "/ajax/outline_booking";
    }


}
