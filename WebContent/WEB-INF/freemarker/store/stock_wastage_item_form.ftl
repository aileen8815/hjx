<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-wastage">报损明细</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <!-- 明细表单 -->
    <form id="stockWastageItem-form" action="${base}/store/stock-wastage/${stockWastageItem.id}/save-item" method="post" class="form-horizontal"
     data-parsley-validate>
        <input type="hidden" name="createdBy" value="${stockWastageItem.createdBy!}">
        <input type="hidden" name="createdTime" value="${stockWastageItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="stockWastage.id" value="${stockWastageId!}">
 		 
        <div class="form-group">
            <label class="col-md-2 control-label">储位:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="locationCode" id="locationCode" data-parsley-required="true"
                       value="${stockWastageItem.storeLocation.code}"
                       readonly>
                <input type="hidden" class="form-control" id="storeLocationId" name="storeLocation.id" value="${stockWastageItem.storeLocation.id}">
            </div>
            <div class="col-md-1">
            	<#if !bookInventory.id?exists>
                <a class="btn btn-info" data-toggle="modal" data-target="#location-selector-modal"><i class="fa fa-ellipsis-v"></i> 选择储位</a>
            	</#if>
            </div>
        </div>
        <div id="modal-item-ext">
            <div class="form-group">
                <label class="col-md-2 control-label">托盘:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" id="storeContainerName" name="storeContainer.label"
                           value="${stockWastageItem.storeContainer.label}" readonly>
                    <input type="hidden" class="form-control" id="storeContainerId" name="storeContainer.id" value="${stockWastageItem.storeContainer.id}">

                </div>
                <div class="col-md-2">

                </div>
            </div>

            <div class="form-group">
                <label class="col-md-2 control-label">商品:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" id="productName" name="productName" readonly value="${stockWastageItem.product.text!}">
                    <input type="hidden" name="product.id" id="productId" value="${stockWastageItem.product.id!}">
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-2 control-label">库存重量:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" name="weightOld" id="weightOld" value="${bookInventory.weight}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">报损重量:</label>

                <div class="col-md-2">
                    <input type="text" class="form-control" name="weight" id="weight" value="${stockWastageItem.weight}" data-parsley-required="true"
                      data-parsley-gt="0" data-parsley-lessto="#weightOld"  data-parsley-type="number">
                </div>
                <div class="col-md-2">
                    <select class="form-control" id="weightMeasureUnit" name="weightMeasureUnit.id">
                        <#list weightMeasureUnits as weightMeasureUnit>
                            <option value="${weightMeasureUnit.id!}"
                                    <#if weightMeasureUnit.id == stockWastageItem.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name} </option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">库存数量:</label>

                <div class="col-md-4">
                    <input type="text" class="form-control" name="amountOld" id="amountOld" value="${bookInventory.amount}" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">报损数量:</label>

                <div class="col-md-2">
                    <input type="text" class="form-control" name="amount" id="amount" value="${stockWastageItem.amount}" data-parsley-required="true"
                           data-parsley-type="integer"  data-parsley-gt="0" data-parsley-lessto="#amountOld" >
                </div>
                <div class="col-md-2">
                    <select class="form-control" id="amountMeasureUnit" name="amountMeasureUnit.id">
                        <#list amountMeasureUnits as amountMeasureUnit>
                            <option value="${amountMeasureUnit.id!}"
                                    <#if amountMeasureUnit.id == stockWastageItem.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name} </option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">报损类型:</label>

                <div class="col-md-4">
                    <select name="stockWastageType.id" id="stockWastageTypeId" class="form-control" data-parsley-required="true">

                        <#list stockWastageTypes as stockWastageType>
                            <option value="${stockWastageType.id!}"
                                    <#if stockWastageType.id == stockWastageItem.stockWastageType.id>selected</#if>>${stockWastageType.name} </option>
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
        $.get('${base}/store/store-location-selector?customerId=${customerId}&allowSelectStatus=2&&showStock=true&selectedLocationCode=${stockWastageItem.storeLocation.code}', function (data) {
            $("#selector-body").html(data);
        });
        <#--$.get('${base}/store/store-location-selector/stock-wastage?customerId=${customerId}', function (data) {
           $("#selector-body").html(data);
       });-->
        function locationNodeClick() {
            var result = $('#location-result');
            $("[rev='" + result.text() + "']").parent().removeClass('stat-selected');

            var ref = $(this).attr('ref');
            var rev = $(this).attr('rev');
            if ($(result).text().indexOf(rev) == -1) {
                $(result).text(rev);
                $(this).parent().addClass('stat-selected');
            } else {
                $(result).text("");
                $(this).parent().removeClass('stat-selected');
            }


            $.ajax({
                url: "${base}/store/stock-wastage/select-storeContainer",
                data: {"storeContainer": ref},
                type: "POST",
                success: function (result) {
                    if (result == '') {
                        $('#stockWastageItem-form')[0].reset()
                    } else {
                        var ar = eval('(' + result + ')');
                        setform(ar);
                    }
                }
            })
        }

        function setform(ar) {
        	$("#storeLocationId").val(ar.storeLocation.id);
            $("#storeContainerId").val(ar.storeContainer.id);
            $("#locationCode").val(ar.storeLocation.code);
            $("#storeContainerName").val(ar.storeContainer.label);
            $("#productId").val(ar.product.id);
            $("#productName").val(ar.product.text);
            $("#weightOld").val(ar.weight);
            $("#weightMeasureUnit").val(ar.weightMeasureUnit.id);
            $("#amountOld").val(ar.amount);
            $("#amountMeasureUnit").val(ar.amountMeasureUnit.id);

        }
        function getData(){
         	if (!$("#stockWastageItem-form").parsley().validate()) {
                return false;
            }
            if($("#weight").val()>$("#weightOld").val()){
        	 alert("报损重量不能大于库存重量");
        	 return false;
        	}
        	if($("#amount").val()>$("#amountOld").val()){
        	alert("报损数量不能大于库存数量");
        	return false;
        	}
        
        }
    </script>

</div>
</#escape>