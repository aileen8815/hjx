package com.wwyl.controller.finance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.finance.StandingBookDaily;
import com.wwyl.entity.settings.Customer;
import com.wwyl.service.finance.StandingBookDailyService;
import com.wwyl.service.settings.CustomerService;

/**
 * @author hehao
 */
@Controller
@RequestMapping("finance/standing-book-daily")
public class StandingBookDailyController extends BaseController {
    @Resource
    StandingBookDailyService standingBookDailyService;
    @Resource
    CustomerService customerService;

    @RequestMapping(value = "/createStandingBookDaily", method = RequestMethod.GET)
    @ResponseBody
    public String createStandingBookDaily() {
        standingBookDailyService.createStandingBookDaily();
        return "SUCCESS!";
    }

    @RequestMapping(value = "/accounting", method = RequestMethod.GET)
    public String accounting(ModelMap model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customerlist", customers);
        return "/finance/standing-book-daily-accounting";
    }

    @RequestMapping(value = "/search-accounting", method = RequestMethod.GET)
    @ResponseBody
    public List<StandingBookDaily> searchAccounting(Date startDate, Date endDate, Long customerId) {
        Date rollingDate = DateUtils.addDays(startDate, -1);
        List<StandingBookDaily> result = standingBookDailyService.findAccountingByDateRangeAndCustomerId(
                startDate, endDate, customerId, rollingDate);
        return result;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public String summary(ModelMap model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customerlist", customers);
        return "/finance/standing-book-daily-summary";
    }

    @RequestMapping(value = "/search-summary", method = RequestMethod.GET)
    @ResponseBody
    public List<StandingBookDaily> searchSummary(Date startDate, Date endDate, Long customerId) {
        Date rollingDate = DateUtils.addDays(startDate, -1);
        List<StandingBookDaily> result = standingBookDailyService.findSummaryByDateRangeAndCustomerId(
                startDate, endDate, customerId, rollingDate);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/billing", method = RequestMethod.GET)
    public String billing(ModelMap model, Date startDate, Date endDate, Long customerId) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customerlist", customers);
        if (startDate != null && endDate != null && customerId != null) {
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("customerId", customerId);
            StandingBookDaily standingBookDaily = standingBookDailyService
                    .findFinanceBillingByDateRangeAndCustomerId(startDate, endDate, customerId);
            if (standingBookDaily != null) {
                model.addAttribute("billing", standingBookDaily);
            }
        }
        return "/finance/standing-book-daily-billing";
    }


    @RequestMapping(value = "/create-again", method = RequestMethod.GET)
    public String createAgain(ModelMap model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "/finance/create_standing_book_daily_again";
    }
    
    @RequestMapping(value = "/create-all-customer", method = RequestMethod.GET)
    public String createAllCustomer(ModelMap model) {
        return "/finance/create_standing_book_daily_all_customer";
    }

    @RequestMapping(value = "/create-again-standing-book-daily", method = RequestMethod.GET)
    public String createAgainStandingBookDaily(Long customerId, Date startDate, ModelMap model) {
        Customer customer = customerService.findOne(customerId);
        List<Date> dates = new ArrayList<Date>();
        int dayCount = dayCount(startDate, new Date());
        for (int i = 0; i < dayCount; i++) {
            dates.add(DateUtils.addDays(startDate, i));
        }
        standingBookDailyService.createAgainCustomerAndDate(customer, dates);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(startDate);
        model.addAttribute("messge", "开始日期：" + dateString + "至昨天，重新生成客户 " + customer.getName().toString() + " 的台帐信息成功！");
        return "/succeed";
    }
    
    @RequestMapping(value = "/create-again-standing-book-daily-all-customer", method = RequestMethod.GET)
    public String createAgainStandingBookDailyAllCustomer(Date startDate, Date endDate, ModelMap model) {
        List<Date> dates = new ArrayList<Date>();
        int dayCount = dayCount(startDate, endDate);
        for (int i = 0; i < dayCount; i++) {
            dates.add(DateUtils.addDays(startDate, i));
        }
        standingBookDailyService.createAllCustomerAndDate(dates);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(startDate);
        String endDateString = formatter.format(startDate);
        model.addAttribute("messge", "开始日期：" + dateString + "至结束日期，重新生成所有客户 的台帐信息成功！");
        return "/succeed";
    }
    

    private int dayCount(Date start, Date end) {
        int result = 0;
        for (Date d = start; !DateUtils.isSameDay(end, d); d = DateUtils.addDays(d, 1)) {
            result++;
        }
        return result;
    }

}
