package com.wwyl;

/**
 * @author fyunli
 */
public interface Enums {

	public enum TransactionType {
		TestTransaction, // 0
		InboundBooking, // 1
		OutboundBooking, // 2
		InboundRegister, // 3
		OutboundRegister, // 4
		InboundReceipt, // 5
		OutboundCheck, // 6
		OutboundFreight, // 7
		StockIn, // 8
		StockOut, // 9
		StoreContainer, // 10
		StoreLocation, // 11
		StockAdjust, // 12
		StoreContract, // 13
		StockOwnerChange, // 14
		StockWastage, // 15
		Payment, // 16
		StockRelocation, // 17
		StockTaking, // 18
		AccountChecking, // 19
		ExtraCharge, // 20
		Product, // 21
		T22, // 22
		T23// 23
	}

    public enum LockStatus {
        正常, 密码锁定, 人工锁定
    }

    public enum Sex {
        男, 女
    }

    public enum StoreAreaStatus {
        正常, 维护
    }
    
    public enum StoreAreaRentStatus {
    	散出, 包库
    }

    public enum TallyAreaStatus {
        正常, 维护
    }

    public enum StoreLocationStatus {
        未绑定, 可使用, 使用中, 预留, 维护
    }

    public enum StoreContainerStatus {
        未绑定, 正常, 维修
    }

    public enum TaskType {
        入库预约, 入库通知, 入库清点完毕, 入库货位分派, 上架通知, 上架完毕,
        出库预约, 出库通知, 拣货通知, 拣货完毕, 出库清点完毕,
        延迟支付申请, 延迟支付审批完毕,
        盘点审批申请, 盘点审批完毕, 移位通知, 移位完毕,
        报损审批申请, 报损审批完毕, 报损费用审批申请, 报损费用审批完毕,
        // 以上TaskMode为工作通知，以下为预警提醒
        保管到期预警, 保质期预警, 入库预约到期, 出库预约到期,
        对账单付款到期预警,其他
    }

    public enum TaskMode {
        工作通知, 预警提醒
    }

    public enum TaskStatus {
        未读, 经办, 已读
    }

    public enum BookingStatus {
        未审核, 已受理, 不受理, 已完成, 已取消
    }

    public enum PaymentStatus {
        未付款, 已付款, 延付待审核, 延付已生效, 延付已拒绝, 已作废, 已退款
    }

    public enum StockInStatus {
        已派送, 已清点, 待上架, 上架中, 已上架, 已完成,已作废
    }

    public enum StockOutStatus {
       已派送, 待拣货, 拣货中, 已拣货, 已清点, 已完成,已作废
    }

	public enum StockTakingMode {
		库区盘点,品种盘点,客户盘点,动态盘点
	}

    public enum StockTakingType {
        初盘, 复盘, 终盘
    }

    public enum InboundType {
        常规入库, 转仓入库, 退货入库
    }

    public enum OutboundType {
        常规出库, 转仓出库
    }

    public enum MeasureUnitType {
        重量, 数量, 长度, 体积, 其他
    }

    public enum StockAdjustStatus {
        待审核, 已批准, 已拒绝, 已生效
    }

    public enum ContractStatus {
        未生效, 已生效, 已停用, 已到期, 已退租, 已预签
    }

    public enum OperateType {
        窗口办理, 自动扣款
    }

    public enum CommentType {
        通用, 不受理预约, 取消预约, 延迟预约, 调整缴费, 延迟付款,报损审核,报损费用审核
    }

    public enum PaymentType {
        正常收费, 正常退费, 撤销收费, 撤销退费 // 正常退费、撤销收费为市场向客户付款，调用API是应为负值
    }

    public enum StockOwnerChangeStatus {
        待处理, 已选位, 已完成, 已取消
    }

    public enum StockWastageStatus {
        待处理,待审核报损, 已同意报损, 待审核报损赔偿, 已同意报损赔偿, 被驳回报损赔偿, 已完成, 已作废
    }

    public enum StockRelocationStatus {
        移位中, 完成,撤销
    }

    public enum StockTakingStatus {
    	待盘点,盘点中, 待审核,已批准,待复盘, 已复盘, 已完成,已作废
    }
    
    public enum StevedorePortStatus{
        正常,维修
    }
    
    public enum HandSetStatus{
        正常,维修
    }
    public enum VehicleSource{
    	自备车辆,冷库提供
    }
    public enum SystemConfigKey{
    	报表服务器URL,报表服务器用户名,报表服务器密码,保管期预警提前天数,保质期预警提前天数,是否收取违约金,违约金额度,
    	对账单付款预警提前天数
    }
    public enum PaymentBoundType{
    	入库管理,出库管理
    }
    public enum CustomerGradeStatus{
    	待审核, 已审核
    }
    public enum StockTakeResultStatus{
    	正常,差异,全盘变动
    }
}
