<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-wastage">报损单</a> -
    <#if stockWastage.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundBooking-form" action="${base}/store/stock-wastage/${stockWastage.id!}/save" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="createdBy" value="${stockWastage.createdBy!}">
        <input type="hidden" name="bookTime" value="${stockWastage.bookTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="createdTime" value="${stockWastage.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${stockWastage.serialNo}">

        <div class="row">
            <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">客户:</label>

                    <div class="col-md-8">
                        <select id="customerId" name="customer.id" class="form-control" onchange="selectedCustomer(this.value)" data-parsley-required="true">
                            <option value="">请选择客户</option>
                            <#list customers as customer>
                                <option value="${customer.id}"
                                        <#if customer.id == stockWastage.customer.id>selected</#if>>${customer.text!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">报损时间:</label>

                    <div class="col-md-8">
                               <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00',minDate:'(.now)'})"    name="inputTime"
                               id="inputTime"
                               value="<#if stockWastage.inputTime?exists>${stockWastage.inputTime?string('yyyy-MM-dd HH:00')}</#if>"
                               placeholder="报损时间" data-parsley-required="true">
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-10">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-3">
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>



<script type="text/javascript">

 

   

</script>
</#escape>
