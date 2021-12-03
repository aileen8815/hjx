<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-register/${inboundRegisterId}">入库登记单</a> -
    <#if inboundRegisterItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundRegisterItem-form" action="${base}/store/inbound-register/${inboundRegisterItem.id!}/save-item" method="post" class="form-horizontal"
          data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${inboundRegisterItem.createdBy!}">
        <input type="hidden" name="createdTime" value="${inboundRegisterItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="inboundRegister.id" id="inboundRegister.id" value="${inboundRegisterId}">
	<div class="row">
     <div class="col-md-11 row">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">商品:</label>

                    <div class="col-md-7">
                        <select id="productId" name="product.id" class="form-control" onchange="selectedproduct()" data-parsley-required="true">
                            <option value="">请选择商品</option>
                            <#list products as product>
                                <option value="${product.id}"
                                        <#if product.id == inboundRegisterItem.product.id>selected</#if>>${product.name}</option>
                            </#list>
                        </select>
                   
                    </div>
                     <div class="col-md-2">
                     
                       <a class="btn btn-info"  data-toggle="modal" data-target="#add-custoemr-modal"><i class="fa fa-ellipsis-v"></i> 添加商品</a>
                     
                     </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                <!--    <label class="col-md-5 control-label">包装:</label> -->
					
                    <div class="col-md-7">
                    <input type="hidden" name="packing.id" id="packingId" value="${inboundRegisterItem.packing.id}"/>
                        
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">数量:</label>

                    <div class="col-md-4">
                        <input type="text" class="form-control" name="amount" id="amount" value="${inboundRegisterItem.amount}"
                               onchange="selectedproduct()" data-parsley-required="true" data-parsley-type="integer" data-parsley-gt="0">
                    </div>
                    <div class="col-md-3">
                        <select id="amountMeasureUnitId" name="amountMeasureUnit.id" class="form-control">

                            <#list amountMeasureUnits as amountMeasureUnit>
                                <option value="${amountMeasureUnit.id}"
                                        <#if amountMeasureUnit.id == inboundRegisterItem.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">重量:</label>

                    <div class="col-md-4">
                        <input type="text" class="form-control" name="weight" id="weight" value="${inboundRegisterItem.weight}"
                               data-parsley-required="true" data-parsley-type="number" data-parsley-gt="0">
                    </div>
                    <div class="col-md-3">
                        <select id="weightMeasureUnitId" name="weightMeasureUnit.id" class="form-control">
                            <#list weightMeasureUnits as weightMeasureUnit>
                                <option value="${weightMeasureUnit.id}"
                                        <#if weightMeasureUnit.id == inboundRegisterItem.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
        	 <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">仓间:</label>

                    <div class="col-md-7">
                         <select id="storeAreaId" name="storeArea.id" class="form-control">
                                    <#list storeAreas as storeArea>
                                        <#if !storeArea.parent?exists>
                                            <option value="${storeArea.id}" <#if storeArea.id == inboundRegisterItem.storeArea.id>selected</#if>>${storeArea.code} ${storeArea.name}</option>
                                            <#list storeArea.children as area>
                                                <option value="${area.id}" <#if area.id == inboundRegisterItem.storeArea.id>selected</#if>>&nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                            </#list>
                                        </#if>
                                    </#list>
                          </select>
                    </div>
                </div>
            </div>
           
             <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">预计托盘数:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="storeContainerCount" id="storeContainerCount"
                               value="${inboundRegisterItem.storeContainerCount}"
                               data-parsley-required="true" data-parsley-type="integer" data-parsley-gt="0">
                    </div>
                </div>
            </div>
            
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">产地:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="productionPlace" id="productionPlace"
                               value="${inboundRegisterItem.productionPlace!}">
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-5 control-label">规格:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="spec" id="spec" value="${inboundRegisterItem.spec}">
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
        	<div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">生产日期:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" onClick="WdatePicker()"
                               name="productionDate" id="productionDate"
                               value="<#if inboundRegisterItem.productionDate?exists>${inboundRegisterItem.productionDate?string('yyyy-MM-dd')}</#if>"
                               placeholder="生产日期">
                    </div>
                </div>
            </div>
            <div class="col-md-6">
            	
            	<div class="form-group">
                    <label class="col-md-5 control-label">预期保管时间:</label>

                    <div class="col-md-7">
                        <div class="input-group">
                            <input type="text" class="form-control" name="storeDuration" id="storeDuration" value="${inboundRegisterItem.storeDuration}"
                                   data-parsley-type="integer" data-parsley-min="0">
                            <span class="input-group-addon">天</span>
                        </div>
                    </div>
                </div>
               
            </div>
            
        </div>
        <div class="row">
        	<div class="col-md-6">
           		<div class="form-group">
                    <label class="col-md-3 control-label">保质期:</label>

                    <div class="col-md-7">
                        <div class="input-group">
                            <input type="text" class="form-control" name="qualityGuaranteePeriod" id="qualityGuaranteePeriod"
                                   value="${inboundRegisterItem.qualityGuaranteePeriod}" data-parsley-type="integer" data-parsley-min="0">
                            <span class="input-group-addon">天</span>
                        </div>
                    </div>
                </div>
           		
            </div>
            <div class="col-md-6">
            	
            	<div class="form-group">
            	<label class="col-md-5 control-label">多品明细:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="productDetail" id="productDetail" value="${inboundRegisterItem.productDetail}">
                    </div>
                </div>
               
            </div> 
        </div>
        
        <div class="row">
            <div class="col-md-9">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-3">
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </div>
        </div>
      </div>
     </div>
    </form>
</div>
<#--
<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>-->
<#include "/store/add_product_modal.ftl">
<script>
    function selectedproduct() {
        var productId = $("#productId").val();
        if (productId == '') {
            return;
        }

        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" + productId,
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');

                $("#packingId").val(ar.commonPacking.id);
                $("#spec").val(ar.spec);
                $("#productionPlace").val(ar.productionPlace);
                var amount = $("#amount").val();

                var reg = "^[1-9]*[1-9][0-9]*$";
                if (amount.match(reg) != null) {
                    var count = 0;
                    if (amount % ar.bearingCapacity > 0) {
                        count = 1
                    }
                    $("#storeContainerCount").val((amount - amount % ar.bearingCapacity) / ar.bearingCapacity + count);

                    $("#weight").val((amount * ar.weight).toFixed(2));
                }
            }
        })
    }
</script>
</#escape>
