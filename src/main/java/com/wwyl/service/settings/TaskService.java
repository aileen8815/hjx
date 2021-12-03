package com.wwyl.service.settings;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.wwyl.Constants;
import com.wwyl.Enums;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.StockWastageStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.finance.AccountChecking;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.InboundBooking;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.OutboundBooking;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.StockWastage;
import com.wwyl.service.security.SecurityService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.wwyl.Enums.TaskMode;
import com.wwyl.dao.settings.TaskDao;
import com.wwyl.entity.settings.Task;
import com.wwyl.entity.security.Operator;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

	@Resource
	private TaskDao taskDao;
	@Resource
	private SecurityService securityService;
	@Resource
	private CustomerService customerService;

	public Page<Task> findTasksByOperator(Long operatorId, TaskMode taskMode,
			int page, int rows) {
		return taskDao.findTasksByOperator(operatorId, taskMode,
				RepoUtils.buildPageRequest(page, rows));
	}

	public List<Task> findActiveTasksByOperator(final Long operatorId,
			final TaskMode taskMode) {
		return taskDao.findActiveTasksByOperator(operatorId, taskMode);
	}

	public Task findOne(Long id) {
		return taskDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(Task task) {
		taskDao.save(task);
	}

	@Transactional(readOnly = false)
	public void delete(Long[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				if (id != null) {
					taskDao.delete(id);
				}
			}
		}
	}

	/**
	 * 设置单个接收人
	 * 
	 * @param task
	 * @param operator
	 */
	@Transactional(readOnly = false)
	public void assignTask(Task task, Operator operator) {
		Set<Operator> acceptors = new HashSet<Operator>();
		acceptors.add(operator);
		task.setOperator(operator);
		task.setAccepters(acceptors);
		taskDao.save(task);
	}

	/**
	 * 按角色编号设置多个接收人，如上架单等任务
	 * 
	 * @param task
	 * @param acceptRoleCode
	 */
	@Transactional(readOnly = false)
	public void assignTask(Task task, String acceptRoleCode) {
		List<Operator> operators = securityService
				.findOperatorsByRole(acceptRoleCode);
		if (CollectionUtils.isNotEmpty(operators)) {
			task.setAccepters(new HashSet<Operator>(operators));
			taskDao.save(task);
		}
	}

	/**
	 * 更改任务状态
	 * 
	 * @param id
	 * @param taskStatus
	 */
	@Transactional(readOnly = false)
	public void updateTaskStatus(Long id, Enums.TaskStatus taskStatus) {
		taskDao.updateTaskStatus(id, taskStatus);
	}

	/**
	 * 批量更改任务状态
	 * 
	 * @param id
	 * @param taskStatus
	 */
	@Transactional(readOnly = false)
	public void updateTaskStatus(Long id[], Enums.TaskStatus taskStatus) {
		if (ArrayUtils.isNotEmpty(id)) {
			for (Long aid : id) {
				if (aid != null) {
					taskDao.updateTaskStatus(aid, taskStatus);
				}
			}
		}
	}

	/**
	 * 标记某类别所有任务通知为已读
	 * 
	 * @param taskMode
	 */
	@Transactional(readOnly = false)
	public void clearAllTasks(TaskMode taskMode) {
		taskDao.updateTaskStatus(taskMode, Enums.TaskStatus.已读);
	}

	/**
	 * 入库预约通知
	 * 
	 * @param inboundBooking
	 */
	@Transactional(readOnly = false)
	public void assignInboundBookingNotice(InboundBooking inboundBooking) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.入库预约);
		task.setTaskTime(new Date());

		// 新建时只传递customer.id，需要从数据库提取实体
		Customer customer = customerService.findOne(inboundBooking
				.getCustomer().getId());
		task.setSubject(DateFormatUtils.format(
				inboundBooking.getApplyInboundTime(), "yyyy-MM-dd HH:mm")
				+ "  " + customer.getText() + "预约入库");
		task.setUrl("store/inbound-booking/" + inboundBooking.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "预约客户：" + customer.getText();
		content += "<br/>申请入库时间：" + inboundBooking.getApplyInboundTime();
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ inboundBooking.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 入库单通知
	 * 
	 * @param inboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignInboundRegistNotice(InboundRegister inboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.入库通知);
		task.setTaskTime(new Date());

		// 新建时只传递customer.id，需要从数据库提取实体
		Customer customer = customerService.findOne(inboundRegister
				.getCustomer().getId());
		task.setSubject(DateFormatUtils.format(
				inboundRegister.getInboundTime(), "yyyy-MM-dd HH:mm")
				+ "  "
				+ customer.getText() + "登记入库");
		task.setUrl("store/inbound-register/" + inboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "入库登记客户：" + customer.getText();
		content += "<br/>入库时间：" + inboundRegister.getInboundTime();
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ inboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_DISPATCHER);
	}

	/**
	 * 入库单清点完毕
	 * 
	 * @param inboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignInboundReceiptNotice(InboundRegister inboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.入库清点完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + inboundRegister.getSerialNo() + "入库清点完成");
		task.setUrl("store/inbound-register/" + inboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "入库单：" + inboundRegister.getSerialNo() + "清点完成";
		content += "<br/>清点完成时间："
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm");
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ inboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 入库货位分派
	 * 
	 * @param inboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignStoreLocationNotice(InboundRegister inboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.入库货位分派);
		task.setTaskTime(new Date());

		task.setSubject(inboundRegister.getSerialNo() + "入库货位分派");
		task.setUrl("store/inbound-register/" + inboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "入库单：" + inboundRegister.getSerialNo() + "准备分派上架货位";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ inboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 入库上架通知
	 * 
	 * @param inboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignStockInNotice(InboundRegister inboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.上架通知);
		task.setTaskTime(new Date());

		task.setSubject(inboundRegister.getSerialNo() + "上架通知");
		task.setUrl("store/inbound-register/" + inboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		int count = inboundRegister.getStockIns().size();
		String content = "入库单：" + inboundRegister.getSerialNo() + "准备上架，托盘数"
				+ count + "托";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ inboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_KEEPER);
	}

	/**
	 * 入库上架完毕通知
	 * 
	 * @param inboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignStockInEndNotice(InboundRegister inboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.上架完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + inboundRegister.getSerialNo() + "上架完毕");
		task.setUrl("store/inbound-register/" + inboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		int count = inboundRegister.getStockIns().size();
		String content = "入库单：" + inboundRegister.getSerialNo() + "上架完毕，共上架托盘"
				+ count + "托";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ inboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 延迟支付申请
	 * 
	 * @param payment
	 */
	@Transactional(readOnly = false)
	public void assignDelayedFilingNotice(Payment payment) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.延迟支付申请);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + payment.getSerialNo() + "延迟支付申请");
		task.setUrl("finance/payment/" + payment.getId() + "/view");// 结合对应业务单填入URL，如入库单对应URL

		String content = payment.getSettledBy().getName() + "申请延迟支付"
				+ payment.getCustomer().getText() + "客户的款项";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ payment.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_MANAGER);
	}

	/**
	 * 延迟支付审核完毕
	 * 
	 * @param payment
	 */
	@Transactional(readOnly = false)
	public void assignDelayedCheckNotice(Payment payment) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.延迟支付审批完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + payment.getSerialNo() + "延迟支付审批完毕");
		task.setUrl("finance/payment/" + payment.getId() + "/view");

		String result = payment.getPaymentStatus() == PaymentStatus.延付已生效 ? "同意"
				: "拒绝";
		String content = payment.getDelayApprover().getName() + result + "延迟支付"
				+ payment.getCustomer().getText() + "客户的款项";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ payment.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, payment.getSettledBy());
	}

	/**
	 * 出库预约通知
	 * 
	 * @param outboundBooking
	 */
	@Transactional(readOnly = false)
	public void assignOutboundBookingNotice(OutboundBooking outboundBooking) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.出库预约);
		task.setTaskTime(new Date());

		// 新建时只传递customer.id，需要从数据库提取实体
		Customer customer = customerService.findOne(outboundBooking
				.getCustomer().getId());
		task.setSubject(DateFormatUtils.format(
				outboundBooking.getApplyOutboundTime(), "yyyy-MM-dd HH:mm")
				+ "  " + customer.getText() + "预约出库");
		task.setUrl("store/outbound-booking/" + outboundBooking.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "预约客户：" + customer.getText();
		content += "<br/>申请出库时间：" + outboundBooking.getApplyOutboundTime();
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ outboundBooking.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 出库单通知
	 * 
	 * @param outboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignOutboundRegistNotice(OutboundRegister outboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.出库通知);
		task.setTaskTime(new Date());

		// 新建时只传递customer.id，需要从数据库提取实体
		Customer customer = customerService.findOne(outboundRegister
				.getCustomer().getId());
		task.setSubject(DateFormatUtils.format(
				outboundRegister.getOutboundTime(), "yyyy-MM-dd HH:mm")
				+ "  " + customer.getText() + "登记出库");
		task.setUrl("store/outbound-register/" + outboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "出库登记客户：" + customer.getText();
		content += "<br/>出库时间：" + outboundRegister.getOutboundTime();
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ outboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_DISPATCHER);
	}

	/**
	 * 出库拣货通知
	 * 
	 * @param outboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignStockOutNotice(OutboundRegister outboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.拣货通知);
		task.setTaskTime(new Date());

		task.setSubject(outboundRegister.getSerialNo() + "拣货通知");
		task.setUrl("store/outbound-register/" + outboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		int count = outboundRegister.getStockOuts().size();
		String content = "出库单：" + outboundRegister.getSerialNo() + "准备下架，托盘数"
				+ count + "托";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ outboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_KEEPER);
	}

	/**
	 * 出库拣货完毕通知
	 * 
	 * @param outboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignStockOutEndNotice(OutboundRegister outboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.拣货完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + outboundRegister.getSerialNo() + "拣货完毕");
		task.setUrl("store/outbound-register/" + outboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		int count = outboundRegister.getStockOuts().size();
		String content = "出库单：" + outboundRegister.getSerialNo() + "下架完毕，共下架托盘"
				+ count + "托";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ outboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 出库单清点完毕
	 * 
	 * @param outboundRegister
	 */
	@Transactional(readOnly = false)
	public void assignOutboundCheckNotice(OutboundRegister outboundRegister) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.出库清点完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + outboundRegister.getSerialNo() + "出库清点完成");
		task.setUrl("store/outbound-register/" + outboundRegister.getId());// 结合对应业务单填入URL，如入库单对应URL

		String content = "出库单：" + outboundRegister.getSerialNo() + "清点完成";
		content += "<br/>清点完成时间："
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm");
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ outboundRegister.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 
	 * 
	 * @param stockWastage
	 */
	@Transactional(readOnly = false)
	public void assignBreakageNotice(StockWastage stockWastage) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.报损审批申请);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(stockWastage.getInputTime(),
				"yyyy-MM-dd HH:mm")
				+ "  "
				+ stockWastage.getInputOperatorName() + "申请报损审批");
		task.setUrl("/store/stock-wastage/" + stockWastage.getId()
				+ "?operatorType=0");// 结合对应业务单填入URL，如入库单对应URL

		String content = stockWastage.getInputOperatorName() + "申请报损单："
				+ stockWastage.getSerialNo() + "审批";

		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ stockWastage.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_MANAGER);
	}

	/**
	 * 
	 * 
	 * @param stockWastage
	 */
	@Transactional(readOnly = false)
	public void assignBreakageEndNotice(StockWastage stockWastage) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.报损审批完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(stockWastage.getApproveTime(),
				"yyyy-MM-dd HH:mm")
				+ "  "
				+ stockWastage.getSerialNo()
				+ "报损审批完毕");
		task.setUrl("/store/stock-wastage/" + stockWastage.getId()
				+ "?operatorType=0");// 结合对应业务单填入URL，如入库单对应URL
		String result = StockWastageStatus.已同意报损 == stockWastage
				.getStockWastageStatus() ? "同意" : "拒绝";
		String content = stockWastage.getApprover().getName() + "管理员" + result
				+ "报损单：" + stockWastage.getSerialNo() + "审批完毕";
		content += "<br/>审批完成时间："
				+ DateFormatUtils.format(stockWastage.getApproveTime(),
						"yyyy-MM-dd HH:mm");
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ stockWastage.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 
	 * 
	 * @param stockWastage
	 */
	@Transactional(readOnly = false)
	public void assignFeeBreakageNotice(StockWastage stockWastage) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.报损费用审批申请);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")
				+ "  " + stockWastage.getInputOperatorName() + "申请报损费用审批");
		task.setUrl("/store/stock-wastage/" + stockWastage.getId()
				+ "?operatorType=1");// 结合对应业务单填入URL，如入库单对应URL
		String result = StockWastageStatus.已同意报损赔偿 == stockWastage
				.getStockWastageStatus() ? "同意" : "拒绝";
		String content = stockWastage.getInputOperatorName() + "管理员" + result
				+ "报损单：" + stockWastage.getSerialNo() + "费用赔偿审批完毕";
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ stockWastage.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_MANAGER);
	}

	/**
	 * 
	 * 
	 * @param stockWastage
	 */
	@Transactional(readOnly = false)
	public void assignFeeBreakageEndNotice(StockWastage stockWastage) {
		Task task = new Task();
		task.setTaskMode(TaskMode.工作通知);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.报损费用审批完毕);
		task.setTaskTime(new Date());

		task.setSubject(DateFormatUtils.format(
				stockWastage.getFeeApproveTime(), "yyyy-MM-dd HH:mm")
				+ "  "
				+ stockWastage.getSerialNo() + "报损费用审批完毕");
		task.setUrl("/store/stock-wastage/" + stockWastage.getId()
				+ "?operatorType=0");// 结合对应业务单填入URL，如入库单对应URL

		String content = stockWastage.getApprover().getName() + "管理员审批报损单："
				+ stockWastage.getSerialNo() + "费用审批完毕";
		content += "<br/>审批完成时间："
				+ DateFormatUtils.format(stockWastage.getFeeApproveTime(),
						"yyyy-MM-dd HH:mm");
		content += "<br/>业务单据详情: <a href='../" + task.getUrl() + "'>"
				+ stockWastage.getSerialNo() + "</a>";
		task.setContent(content);

		this.assignTask(task, Constants.ROLE_REGISTER);
	}

	/**
	 * 
	 * 
	 * @param 保管到期预警
	 */
	@Transactional(readOnly = false)
	public void assignRetentionPeriodNotice(BookInventory bookInventory) {
		Task task = new Task();
		task.setTaskMode(TaskMode.预警提醒);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.保管到期预警);
		task.setTaskTime(new Date());

		task.setSubject("入库批次为："+bookInventory.getInboundRegisterSerialNo()+"的商品："+bookInventory.getProductName()+"保管快要到期！");
		String content="入库批次为："+bookInventory.getInboundRegisterSerialNo()+"的商品："+bookInventory.getProductName()+"保管快要到期！"+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm");
		/*
		 * //task.setUrl("/store/stock-wastage/" +
		 * stockWastage.getId()+"?operatorType=0");// 结合对应业务单填入URL，如入库单对应URL
		 * 
		 * String content = stockWastage.getApprover().getName()+"管理员审批报损单：" +
		 * stockWastage.getSerialNo()+"费用审批完毕"; content += "<br/>审批完成时间："
		 * +DateFormatUtils.format(stockWastage.getFeeApproveTime(),
		 * "yyyy-MM-dd HH:mm"); content += "<br/>业务单据详情: <a href='../" +
		 * task.getUrl() + "'>" + stockWastage.getSerialNo() + "</a>";
		 * task.setContent(content);
		 */
		task.setContent(content);
		this.assignTask(task, Constants.ROLE_KEEPER);
	}

	/**
	 * 
	 * 
	 * @param 商品变质预警
	 */
	@Transactional(readOnly = false)
	public void assignGuaranteePeriodNotice(BookInventory bookInventory) {
		Task task = new Task();
		task.setTaskMode(TaskMode.预警提醒);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.保质期预警);
		task.setTaskTime(new Date());
		task.setSubject("入库批次为:"+bookInventory.getInboundRegisterSerialNo()+"的商品:"+bookInventory.getProductName()+"保质期快要到期！");
		String content="入库批次为:"+bookInventory.getInboundRegisterSerialNo()+"的商品:"+bookInventory.getProductName()+"保质期快要到期！"+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"); 
		task.setContent(content);
		//task.setUrl("/store/book-inventory/view?id="+bookInventory.getId());// 结合对应业务单填入URL，如入库单对应URL
		  
		 /* String content = stockWastage.getApprover().getName()+"管理员审批报损单：" +
		 * stockWastage.getSerialNo()+"费用审批完毕"; content += "<br/>审批完成时间："
		 * +DateFormatUtils.format(stockWastage.getFeeApproveTime(),
		 * "yyyy-MM-dd HH:mm"); content += "<br/>业务单据详情: <a href='../" +
		 * task.getUrl() + "'>" + stockWastage.getSerialNo() + "</a>";
		 * task.setContent(content);
		 */

		this.assignTask(task, Constants.ROLE_KEEPER);
	}
	

	/**
	 * 
	 * 
	 * @param 对账单到期预警
	 */
	@Transactional(readOnly = false)
	public void assignAccountCheckingPeriodNotice(AccountChecking accountChecking) {
		Task task = new Task();
		task.setTaskMode(TaskMode.预警提醒);
		task.setTaskStatus(Enums.TaskStatus.未读);
		task.setTaskType(Enums.TaskType.对账单付款到期预警);
		task.setTaskTime(new Date());

		task.setSubject("对账单号为："+accountChecking.getSerialNo()+"的客户："+accountChecking.getCustomerName()+" 最后付款期限快要到期！");
		String content="对账单号为："+accountChecking.getSerialNo()+"的客户："+accountChecking.getCustomerName()+" 最后付款期限快要到期！"+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm");
		/*
		 * //task.setUrl("/store/stock-wastage/" +
		 * stockWastage.getId()+"?operatorType=0");// 结合对应业务单填入URL，如入库单对应URL
		 * 
		 * String content = stockWastage.getApprover().getName()+"管理员审批报损单：" +
		 * stockWastage.getSerialNo()+"费用审批完毕"; content += "<br/>审批完成时间："
		 * +DateFormatUtils.format(stockWastage.getFeeApproveTime(),
		 * "yyyy-MM-dd HH:mm"); content += "<br/>业务单据详情: <a href='../" +
		 * task.getUrl() + "'>" + stockWastage.getSerialNo() + "</a>";
		 * task.setContent(content);
		 */
		task.setContent(content);
		this.assignTask(task, Constants.ROLE_KEEPER);
	}
}
