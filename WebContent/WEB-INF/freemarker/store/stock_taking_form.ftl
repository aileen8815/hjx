<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-taking">盘点</a> -
    <#if stockTaking.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundBooking-form" action="${base}/store/stock-taking/${stockTaking.id!}/save" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="createdBy" value="${stockTaking.createdBy!}">
        <input type="hidden" name="bookTime" value="${stockTaking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="createdTime" value="${stockTaking.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${stockTaking.serialNo}">

        <div class="row">
            <div class="col-md-offset-2 col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">库区:</label>

                    <div class="col-md-8">
                      	<select id="storeAreaId" name="storeAreaId" data-parsley-required="true" class="form-control">
                      		<option value="">请选择库区</option>
                            <#list storeAreas as storeArea>
                                <option value="${storeArea.id}"
                                        <#if storeArea.id == stockTaking.storeArea.id>selected</#if>>${storeArea.code} ${storeArea.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>

         <div class="col-md-offset-2 col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">盘点人:</label>

                    <div class="col-md-8">
                        <select id="operators" name="stockTakingOperator.id" class="form-control"  data-parsley-required="true">
                            <option value="">请选择盘点人</option>
                            <#list operators as operator>
                                <option value="${operator.id}"
                                        <#if operator.id == stockTaking.stockTakingOperator.id>selected</#if>>  ${operator.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
 
            <div class="col-md-offset-2  col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">盘点开始时间:</label>

                    <div class="col-md-8">
                               <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00',minDate:'(.now)'})"   name="startTime"
                               id="startTime"
                               value="<#if stockTaking.startTime?exists>${stockTaking.startTime?string('yyyy-MM-dd HH:00')}</#if>"
                               placeholder="盘点开始时间" data-parsley-required="true">
                    </div>
                </div>
            </div>
        
        <div class="col-md-offset-2 col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">盘点备注:</label>
					<div class="col-md-8">
					<input type="text" class="form-control" name="remark" id="remark" value="${stockTaking.remark!}">
                    </div>
				</div>
            </div>
        
        <div class="row">
            <div class="col-md-10">
                <div class="form-group">
                    <div class="col-md-offset-5 col-md-3">
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
