<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-taking/${stockTaking.id}">盘点单</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <!-- 明细表单 -->
    <form id="stockTakingResult-form" action="${base}/store/stock-taking/save-result" method="post" class="form-horizontal"
     data-parsley-validate>
        <input type="hidden" name="createdBy" value="${stockTakingResult.createdBy!}">
        <input type="hidden" name="createdTime" value="${stockTakingResult.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="stockTaking.id" value="${stockTaking.id!}">
        <input type="hidden" name="id" value="${resultId!}">
 		 
        <div class="form-group">
            <label class="col-md-2 control-label">储位:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="locationCode" id="locationCode" data-parsley-required="true"
                       value="${stockTakingResult.storeLocation.code}"
                       readonly>
                <input type="hidden" class="form-control" id="storeLocationId" name="storeLocation.id" value="${stockTakingResult.storeLocation.id}">
            </div>
            <div class="col-md-4">
            	<#if !resultId?exists>
                <a class="btn btn-info" data-toggle="modal" data-target="#location-selector-modal"  onclick="selectlocation(2)"><i class="fa fa-ellipsis-v"></i> 选择储位</a>
                &nbsp;&nbsp;&nbsp;
                </#if>
                <a href="javascript:void(0)" class="btn btn-info"   onclick="showToStoreLocationGroup()">移动托盘</a>
            	 
            </div>
        </div>
        
        <div class="form-group"  id="toStoreLocationGroup"  <#if !stockTakingResult.toStoreLocation?exists>  style="display:none" </#if>>
            <label class="col-md-2 control-label">目标储位:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="toStoreLocation.code" id="toStoreLocationCode"  
                       value="${stockTakingResult.toStoreLocation.code}">
                <input type="hidden" class="form-control" id="toStoreLocationId" name="toStoreLocation.id" value="${stockTakingResult.toStoreLocation.id}">
            </div>
            <div class="col-md-1">
            	<#if !bookInventory.id?exists>
                <a class="btn btn-info" data-toggle="modal" data-target="#location-selector-modal"  onclick="selectlocation(1)"><i class="fa fa-ellipsis-v"></i> 选择储位</a>
            	</#if>
            </div>
        </div>
        <div id="modal-item-ext">
            <div class="form-group">
                <label class="col-md-2 control-label">托盘标签:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" id="storeContainerLabel" name="storeContainer.label"
                           value="${stockTakingResult.storeContainer.label}" readonly>
                    <input type="hidden" class="form-control" id="storeContainerId" name="storeContainer.id" value="${stockTakingResult.storeContainer.id}">

                </div>
                <div class="col-md-2">

                </div>
            </div>
			
		   <div class="form-group">
            <label class="col-md-2 control-label">客户:</label>
            <div class="col-md-4">
           		<input type="text" class="form-control" id="customerName" name="customerName" readonly value="${stockTakingResult.customer.text!}">
                <input type="hidden" name="customer.id" id="customerId" value="${stockTakingResult.customer.id!}">
            </div>
          </div>
            <div class="form-group">
                <label class="col-md-2 control-label">商品:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" id="productName" name="productName" readonly value="${stockTakingResult.product.text!}">
                    <input type="hidden" name="product.id" id="productId" value="${stockTakingResult.product.id!}">
                </div>
            </div>
			
			<div class="form-group">
                <label class="col-md-2 control-label">库存数量:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" name="amount" id="amountOld" value="${stockTakingResult.amount}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">盘点数量:</label>

                <div class="col-md-2">
                    <input type="text" class="form-control" name="stockTakingAmount" id="amount" value="${stockTakingResult.stockTakingAmount}" data-parsley-required="true"
                           data-parsley-type="integer"  data-parsley-min="0" data-parsley-lessto="#amountOld" >
                </div>
                <div class="col-md-2">
                    <select class="form-control" id="amountMeasureUnit" name="amountMeasureUnit.id">
                        <#list amountMeasureUnits as amountMeasureUnit>
                            <option value="${amountMeasureUnit.id!}"
                                    <#if amountMeasureUnit.id == stockTakingResult.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name} </option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">库存重量:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" name="weight" id="weightOld" value="${stockTakingResult.weight}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">盘点重量:</label>

                <div class="col-md-2">
                    <input type="text" class="form-control" name="stockTakingWeight" id="weight" value="${stockTakingResult.stockTakingWeight}" data-parsley-required="true"
                      data-parsley-min="0" data-parsley-lessto="#weightOld"  data-parsley-type="number">
                </div>
                <div class="col-md-2">
                    <select class="form-control" id="weightMeasureUnit" name="weightMeasureUnit.id">
                        <#list weightMeasureUnits as weightMeasureUnit>
                            <option value="${weightMeasureUnit.id!}"
                                    <#if weightMeasureUnit.id == stockTakingResult.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name} </option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-2 col-md-9">
                    <button  type="submit" class="btn btn-success">保存</button>
                </div>
            </div>
    </form>

    <!-- Modal -->
    <div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog-full modal-location-selector">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">选择储位</h4>
                </div>
                <div id="selector-body" class="modal-body">


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal   -->

    <script>
     function selectlocation(allowSelectStatus){
      	$("#selector-body").html('');
     	$.get('${base}/store/store-location-selector?allowSelectStatus='+allowSelectStatus+'&&selectedLocationCode=${stockTakingResult.storeLocation.code}&areaId=${stockTaking.stockTakingObjectId}', function (data) {
            $("#selector-body").html(data);
        });
	 }
        function locationNodeClick() {
            var result = $('#location-result');
            $("[title='" + result.text() + "']").parent().removeClass('stat-selected');

            var ref = $(this).attr('ref');
            var title = $(this).attr('title');
            if ($(result).text().indexOf(title) == -1) {
                $(result).text(title);
                $(this).parent().addClass('stat-selected');
            } else {
                $(result).text("");
                $(this).parent().removeClass('stat-selected');
            }
			
			if(ref!=''){
	            $.ajax({
	                url: "${base}/store/stock-taking/select-storeContainer",
	                data: {"storeContainer": ref},
	                type: "POST",
	                success: function (result) {
	                    if (result == '') {
	                        $('#stockTakingResult-form')[0].reset()
	                    } else {
	                        var ar = eval('(' + result + ')');
	                        setform(ar);
	                    }
	                }
	            })
           }else{
          		 var title = $("#toStoreLocationCode").val(title);
           }
        }

        function setform(ar) {
        	$("#storeLocationId").val(ar.storeLocation.id);
        	$("#locationCode").val(ar.storeLocation.code);
            $("#storeContainerId").val(ar.storeContainer.id);
            $("#storeContainerLabel").val(ar.storeContainer.label);
            $("#productId").val(ar.product.id);
            $("#productName").val(ar.product.text);
           	$("#customerId").val(ar.customer.id);
            $("#customerName").val(ar.customer.text);
            $("#weightOld").val(ar.weight);
            $("#weightMeasureUnit").val(ar.weightMeasureUnit.id);
            $("#amountOld").val(ar.amount);
            $("#weight").val(ar.weight);
            $("#amount").val(ar.amount);
            $("#amountMeasureUnit").val(ar.amountMeasureUnit.id);

        }
       function showToStoreLocationGroup(){
        	$("#toStoreLocationGroup").show();
       }
        
    </script>

</div>
</#escape>