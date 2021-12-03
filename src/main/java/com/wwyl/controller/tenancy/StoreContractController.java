package com.wwyl.controller.tenancy;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.tenancy.StoreContract;
import com.wwyl.entity.tenancy.StoreContractFeeItem;
import com.wwyl.entity.tenancy.StoreContractLocationItem;
import com.wwyl.entity.tenancy.StoreContractPolicyItem;
import com.wwyl.Enums.ContractStatus;
import com.wwyl.Enums.OperateType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.service.ce.CEFeeItemService;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.tenancy.StoreContractService;

/**
 * @author pixf
 */

@Controller
@RequestMapping("/tenancy/store-contract")
public class StoreContractController extends BaseController {
	
	@Resource
	private CustomerService customerService;
	@Resource
	private StoreContractService storeContractService;
	@Resource
	SerialNumberService serialNumberService;	
	@Resource
	StoreLocationService storeLocationService;
	@Resource	
	CEFeeItemService ceFeeItemService;
	@Resource
	MeasureUnitService measureUnitService;
	@Resource
	PaymentService paymentService;
	@Resource
	private StoreAreaService storeAreaService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("status", ContractStatus.values());
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findAll());
		return indexPage;
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "customerId", defaultValue = "") String customerId,ContractStatus status,String storeAreaId) {
		Page<StoreContract> storeContracts = storeContractService.findStoreContractBySerialNoLike(page, rows, customerId, status, storeAreaId);
		return toEasyUiDatagridResult(storeContracts);
	}	
	
	@RequestMapping(value = "getstoreArea", method = RequestMethod.GET)
	@ResponseBody
	public StoreArea getProduct(@RequestParam(value = "id", defaultValue = "") Long id) {
		StoreArea storeArea= storeAreaService.findOne(id);
		return storeArea;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(HttpServletRequest request, HttpSession session, ModelMap model) {
		setModelMap(model);
		return formPage;
	}	
	
	private void setModelMap(ModelMap model) {
		model.addAttribute("status", ContractStatus.values());
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findAll());
	}	
	
	@RequestMapping(method = RequestMethod.POST)
	public String create(
			@ModelAttribute("storeContract") @Valid StoreContract storeContract,
			BindingResult result,
			   ModelMap model) {

		storeContract.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StoreContract));
		storeContract.setSignedDate(new Date());
		storeContract.setStatus(ContractStatus.未生效);
		storeContractService.save(storeContract);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";		
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeContractService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
		model.addAttribute("_method", "put");
		setModelMap(model);
		model.addAttribute("storeContract", storeContractService.findOne(id));
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(
			@ModelAttribute("storeContract") @Valid StoreContract storeContract,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return formPage;
		}
		storeContract.setSignedDate(new Date());
		storeContractService.save(storeContract);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";		
	}
	
	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		StoreContract storeContract = storeContractService.findOne(id);
		model.addAttribute("storeContract", storeContract);
		model.addAttribute("storeContractFeeItems", storeContract.getContractFeeItems());
		model.addAttribute("storeContractPolicyItems", storeContract.getContractPolicyItems());		
		return viewPage;
	}	
	
	@RequestMapping(value = "/{id}/start-contract", method = RequestMethod.GET)
	public String startContract(@PathVariable Long id, ModelMap model) {
		StoreContract storeContract = storeContractService.findOne(id);
		storeContractService.startContract(storeContract);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";
	}
	
	@RequestMapping(value = "/{id}/copy-contract", method = RequestMethod.GET)
	public String copyContract(@PathVariable Long id, ModelMap model) {
		StoreContract storeContract1 = storeContractService.findOne(id);
		StoreContract storeContract2 = new StoreContract();
		storeContractService.copyContract(storeContract1, storeContract2);
		return "redirect:/tenancy/store-contract/" + storeContract2.getId().toString() + "/view";
	}
	
	@RequestMapping(value = "/{id}/stop-contract", method = RequestMethod.GET)
	public String stopContract(@PathVariable Long id, ModelMap model) {
		StoreContract storeContract = storeContractService.findOne(id);
		storeContractService.stopContract(storeContract);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";
	}
	
	//查询待缴费入库单
	@RequestMapping(value="/carryover",method = RequestMethod.GET)
	public String carryover(ModelMap model) {
		return "/tenancy/store_contract_carryover";
	}
	
	//查询待缴费入库单
	@RequestMapping(value="/start-carryover",method = RequestMethod.GET)
	public String startCarryover(@RequestParam(value = "datestr", defaultValue = "") String datestr,
			ModelMap model) throws ParseException {
		System.out.println("datestr: " + datestr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(datestr);		
		storeContractService.carryoverContract(date);
		return "/tenancy/store_contract_carryover";
	}	
	
	//=================================StoreContractFeeItem================================================================
	@RequestMapping(value = "/index-fee-item", method = RequestMethod.GET)
	public String indexFeeitem(@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);
		model.put("storeContract", storeContract);
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_fee_item";	
	}	
	
	@RequestMapping(value = "/{id}/edit-fee-item", method = RequestMethod.GET)
	public String editFeeItem(@PathVariable Long id,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		setFeeItemModelMap(model);
		model.addAttribute("storeContractFeeItem", storeContractService.findOneFeeItem(id));
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_fee_item_form";
	}
	
	private void setFeeItemModelMap(ModelMap model) {
		model.addAttribute("feeItems", ceFeeItemService.findAll());
		model.addAttribute("operateTypes", OperateType.values());
	}	
	
	@RequestMapping(value = "/fee-items", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreContractFeeItem> feeItems(
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo) {
		List<StoreContractFeeItem> storeContractFeeItemList = storeContractService.findStoreContractFeeItems(serialNo);
		return  storeContractFeeItemList;
	}
	
	@RequestMapping(value = "/new-fee-item", method = RequestMethod.GET)
	public String newFeeItem(
			@ModelAttribute("storeContractFeeItem") @Valid StoreContractFeeItem storeContractFeeItem,
			BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		setFeeItemModelMap(model);
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_fee_item_form";
	}	
	
	@RequestMapping(value = "/save-fee-item", method = RequestMethod.POST)
	public String addFeeItem(
			@ModelAttribute("storeContractFeeItem") @Valid StoreContractFeeItem storeContractFeeItem,
			BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);
		storeContractFeeItem.setStoreContract(storeContract);
		storeContractFeeItem.setEhargeEndDate(storeContract.getChargeDate());
		storeContractService.saveFeeItem(storeContractFeeItem);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";			
	}	
	
	@RequestMapping(value = "/{id}/save-fee-item", method = RequestMethod.POST)
	public String updateFeeItem(@ModelAttribute("storeContractFeeItem") @Valid StoreContractFeeItem storeContractFeeItem, BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		storeContractFeeItem.setEhargeEndDate(storeContractFeeItem.getStoreContract().getChargeDate());
		storeContractService.saveFeeItem(storeContractFeeItem);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContractFeeItem.getStoreContract().getId().toString() + "/view";			
	}	
	
	@RequestMapping(value = "/{id}/delete-fee-item", method = RequestMethod.GET)
	public String deleteFeeItem(@PathVariable Long id, @RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);			
		storeContractService.deleteFeeItem(id);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";			
	}	
		
	//=================================StoreContractPolicyItem================================================================
	@RequestMapping(value = "/index-policy-item", method = RequestMethod.GET)
	public String indexPolicyItem(@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);
		model.put("storeContract", storeContract);
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_policy_item";	
	}	
	
	@RequestMapping(value = "/{id}/edit-policy-item", method = RequestMethod.GET)
	public String editPolicyItem(@PathVariable Long id,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		setPolicyItemModelMap(model);
		model.addAttribute("storeContractPolicyItem", storeContractService.findOnePolicyItem(id));
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_policy_item_form";
	}
	
	private void setPolicyItemModelMap(ModelMap model) {
		model.addAttribute("feeItems", ceFeeItemService.findAll());
		model.addAttribute("measureUnits", measureUnitService.findAll());
	}	
	
	@RequestMapping(value = "/policy-items", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreContractPolicyItem> policyItems(
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo) {
		List<StoreContractPolicyItem> storeContractPolicyItemList = storeContractService.findStoreContractPolicyItems(serialNo);
		return  storeContractPolicyItemList;
	}
	
	@RequestMapping(value = "/new-policy-item", method = RequestMethod.GET)
	public String newFeeItem(
			@ModelAttribute("storeContractPolicyItem") @Valid StoreContractPolicyItem storeContractPolicyItem,
			BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		setPolicyItemModelMap(model);
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_policy_item_form";
	}	
	
	@RequestMapping(value = "/save-policy-item", method = RequestMethod.POST)
	public String addPolicyItem(
			@ModelAttribute("storeContractPolicyItem") @Valid StoreContractPolicyItem storeContractPolicyItem,
			BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);
		storeContractPolicyItem.setStoreContract(storeContract);
		storeContractService.savePolicyItem(storeContractPolicyItem);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";		
	}	
	
	@RequestMapping(value = "/{id}/save-policy-item", method = RequestMethod.POST)
	public String updatePolicyItem(@ModelAttribute("storeContractPolicyItem") @Valid StoreContractPolicyItem storeContractPolicyItem, BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		storeContractService.savePolicyItem(storeContractPolicyItem);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContractPolicyItem.getStoreContract().getId().toString() + "/view";	
	}	
	
	@RequestMapping(value = "/{id}/delete-policy-item", method = RequestMethod.GET)
	public String deletePolicyItem(@PathVariable Long id, @RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);	
		storeContractService.deletePolicyItem(id);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";	
	}	
	
	//========================================StoreContractLocationItem=============================================================
	private void setLocationItemModelMap(ModelMap model) {
		model.addAttribute("storeLocations", storeLocationService.findAll());
	}		
	
	@RequestMapping(value = "/index-location-item", method = RequestMethod.GET)
	public String indexLocationItem(@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);
		model.put("storeContract", storeContract);
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_location_item";	
	}		
	
	@RequestMapping(value = "/{id}/edit-location-item", method = RequestMethod.GET)
	public String editLocationItem(@PathVariable Long id,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		setLocationItemModelMap(model);
		model.addAttribute("storeContractLocationItem", storeContractService.findOneLocationItem(id));
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_location_item_form";
	}	
	
	@RequestMapping(value = "/location-items", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreContractLocationItem> locationItems(
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo) {
		List<StoreContractLocationItem> storeContractLocationItemList = storeContractService.findStoreContractLocationItems(serialNo);
		return  storeContractLocationItemList;
	}	
	
	@RequestMapping(value = "/new-location-item", method = RequestMethod.GET)
	public String newLocationItem(
			@ModelAttribute("storeContractLocationItem") @Valid StoreContractLocationItem storeContractLocationItem,
			BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		setLocationItemModelMap(model);
		model.put("serialNo", serialNo);
		return "/tenancy/store_contract_location_item_form";
	}		
	
	@RequestMapping(value = "/save-location-item", method = RequestMethod.POST)
	public String addLocationItem(
			@ModelAttribute("storeContractLocationItem") @Valid StoreContractLocationItem storeContractLocationItem,
			BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			ModelMap model) {
		StoreContract storeContract = storeContractService.findBySerialNo(serialNo);
		storeContractLocationItem.setStoreContract(storeContract);
		storeContractService.saveLocationItem(storeContractLocationItem);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + storeContract.getId().toString() + "/view";
	}		
	
	@RequestMapping(value = "/{id}/save-location-item", method = RequestMethod.POST)
	public String updateLocationItem(@ModelAttribute("storeContractLocationItem") @Valid StoreContractLocationItem storeContractLocationItem, BindingResult result,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		storeContractService.saveLocationItem(storeContractLocationItem);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/index-location-item?serialNo=" + serialNo;	
	}	
	
	@RequestMapping(value = "/{id}/delete-location-item", method = RequestMethod.GET)
	public String deleteLocationItem(@PathVariable Long id, @RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		
		storeContractService.deleteLocationItem(id);
		model.put("serialNo", serialNo);
		return "redirect:/tenancy/store-contract/" + id.toString() + "/view";		
	}	
		
	//====================================================退租====================================================
	@RequestMapping(value = "/eviction", method = RequestMethod.GET)
	public String  selectCustomer(ModelMap model){
		model.addAttribute("customers", customerService.findAll());
		return  "/tenancy/store_contract_eviction_form";
	}
	
	@RequestMapping(value = "/select-customer", method = RequestMethod.POST)
	@ResponseBody
	public  List<StoreContract>  selectCustomer(Long customerId){
		List<StoreContract> storeContracts =storeContractService.findValidContractByCustomer(customerId) ;
		return  storeContracts;
	}
	
	@RequestMapping(value = "/select-store-contract", method = RequestMethod.GET)
	public  String  selectStoreContract(Long customerId,Long  storeContractId,ModelMap model){
		List<StoreContractFeeItem> storeContractFeeItems =storeContractService. findFeeItemByStoreContractId(storeContractId) ;
		StoreContract storeContract=storeContractService.findOne(storeContractId);
		model.addAttribute("storeContractFeeItems", storeContractFeeItems);
		model.addAttribute("customerid", customerId);
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("storeContract", storeContract);
		List<StoreContract> storeContracts =storeContractService.findValidContractByCustomer(customerId) ;
		model.addAttribute("storeContracts", storeContracts);
		return  "/tenancy/store_contract_eviction_form";
	}
	
	@RequestMapping(value = "/save-to-payment", method = RequestMethod.POST)
	public  String  saveToPayment(Long storecantractId,String[] feeitem, String[] money,ModelMap model){
		List<PaymentItem>  paymentItemList=storeContractService.settlement(feeitem,money);
		Long  paymentid=storeContractService.evictionContract(storecantractId,paymentItemList);
		StoreContract storeContract=storeContractService.findOne(storecantractId);
	    List<PaymentItem>  paymentItems=paymentService.findPaymentItems(paymentid);
		model.addAttribute("storeContract", storeContract);
		model.addAttribute("paymentItems", paymentItems);
		model.addAttribute("paymentId", paymentid);
		return  "/tenancy/store_contract_payment_view";
	}
	
	
	
	@RequestMapping(value="{id}/payment" ,method = RequestMethod.POST)
	public String payment(@PathVariable Long id,String aCardMac, String sMemberCode,String password) {
		paymentService.payment(new Long[]{id},aCardMac,sMemberCode,password);
		return "redirect:/tenancy/store-contract";
	}
	
	//===========================================撤销退租====================================================
	@RequestMapping(value = "/cancelEviction", method = RequestMethod.GET)
	public String  selectEvictionCustomer(ModelMap model){
		model.addAttribute("customers", customerService.findAll());
		return  "/tenancy/store_contract_cancel_eviction_form";
	}		
	
	@RequestMapping(value = "/select-eviction-customer", method = RequestMethod.POST)
	@ResponseBody
	public  List<StoreContract> selectEvictionCustomer(Long customerId){
		List<StoreContract> storeContracts =storeContractService.findEvictionContractByCustomer(customerId) ;
		return  storeContracts;
	}	
	
	@RequestMapping(value = "/select-eviction-contract", method = RequestMethod.GET)
	public  String  selectEvictionContract(Long customerId,Long  storeContractId,ModelMap model){
		
		StoreContract storeContract = storeContractService.findOne(storeContractId);
		Payment payment = paymentService.findPaymentByContractId(storeContract.getClass().getSimpleName(), storeContractId);	
		List<PaymentItem> paymentFeeItems = new ArrayList(payment.getPaymentItems());			
		model.addAttribute("paymentFeeItems", paymentFeeItems);
		model.addAttribute("customerid", customerId);
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("storeContract", storeContract);
		model.addAttribute("paymentId", payment.getId());
		List<StoreContract> storeContracts =storeContractService.findEvictionContractByCustomer(customerId) ;
		model.addAttribute("storeContracts", storeContracts);
	
		return  "/tenancy/store_contract_cancel_eviction_form";
	}	
	
	@RequestMapping(value = "/cancel-payment", method = RequestMethod.POST)
	public String cancelPayment(Long paymentId, ModelMap model){
		Long storecantractId = paymentService.findOne(paymentId).getPaymentObjectId();
		storeContractService.cancelEviction(storecantractId, paymentId);
		return "redirect:/tenancy/store-contract/" + storecantractId.toString() + "/view";
	}	
	
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity exists(String oldCode,String contractNo) {
		if (StringUtils.equals(oldCode, contractNo)) {
	            return new ResponseEntity(HttpStatus.OK);
	    }

		if (storeContractService.exists(contractNo)) {
			 return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

			
		} 
			return new ResponseEntity(HttpStatus.OK);
	}
}

