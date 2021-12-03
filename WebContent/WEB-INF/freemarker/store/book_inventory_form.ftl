<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/book-inventory">库存初始化</a> -
    <#if bookInventory.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="bookInventory-form" action="${base}/store/book-inventory/${bookInventory.id!}" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${bookInventory.createdBy!}">
        <input type="hidden" name="createdTime" value="${bookInventory.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="stockIn.id" id="stockIn.id" value="${bookInventory.stockIn.id}">
        <input type="hidden" class="form-control" name="inboundRegisterSerialNo" id="inboundRegisterSerialNo" value="${bookInventory.inboundRegisterSerialNo}">
        <input type="hidden" class="form-control" name="storeLocationOld" id="storeLocationOld" value="${bookInventory.storeLocation.code}">
        
        <input type="hidden" class="form-control" name="stockInOperator.id" id="stockInOperator.id" value="${bookInventory.stockInOperator.id}">
		<div class="row">
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">托盘:</label>

                <div class="col-md-8">
                 
                  <input type="text" class="form-control" id="storeContainerId"   name="storeContainer.id"  value="${bookInventory.storeContainer.id}"  data-parsley-required="true"/>
                     
                </div>
            </div>
        </div>

        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">储位:</label>

                <div class="col-md-8">
                    <input type="text" class="form-control" name="storeLocation" id="storeLocation" value="${bookInventory.storeLocation.code}"
                           data-parsley-required="true" readonly>
                </div>

            </div>
        </div>
        <div class="col-md-2">
            <a class="btn btn-info" data-toggle="modal" data-target="#location-selector-modal"><i class="fa fa-ellipsis-v"></i> 选择储位</a>
        </div>
		</div>
		<div class="row">
        <div class="col-md-5">
            <div class="form-group">

                <label class="col-md-4 control-label">客户:</label>

                <div class="col-md-8">
                    <select id="customerId" name="customer.id" class="form-control"   onchange="selectedCustomer(this.value)"    data-parsley-required="true">
                        <option value="">请选择客户</option>
                        <#list customers as customer>
                            <option value="${customer.id}" <#if customer.id == bookInventory.customer.id>selected</#if>>${customer.text!}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">商品:</label>

                <div class="col-md-8">

                   <select id="productId" name="product.id" class="form-control"  onchange="selectedproduct(this.value)" data-parsley-required="true">
                 	<option value="">请选择商品</option>
					<#list products as product>
                        <option value="${product.id}"
                                <#if product.id == bookInventory.product.id>selected</#if>>${product.code}${product.name}</option>
                    </#list>
                </select>
                </div>
            </div>
        </div>
	 	</div>
	 	<div class="row">
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">数量:</label>

                <div class="col-md-5">
                    <input type="text" class="form-control" name="amount" id="amount" value="${bookInventory.amount}" data-parsley-required="true"
                     onchange="selectedproduct(this.value)"       data-parsley-type="integer" data-parsley-gt="0">
                </div>
                <div class="col-md-3">
                    <select id="amountMeasureUnitId" name="amountMeasureUnit.id" class="form-control">

                        <#list amountMeasureUnits as amountMeasureUnit>
                            <option value="${amountMeasureUnit.id}"
                                    <#if amountMeasureUnit.id == bookInventory.amountMeasureUnit.id>selected</#if>>${amountMeasureUnit.name}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">重量:</label>

                <div class="col-md-5">
                    <input type="text" class="form-control" name="weight" id="weight" value="${bookInventory.weight}" data-parsley-required="true"
                           data-parsley-type="number" data-parsley-gt="0">
                </div>
                <div class="col-md-3">
                    <select id="weightMeasureUnitId" name="weightMeasureUnit.id" class="form-control">
                        <#list weightMeasureUnits as weightMeasureUnit>
                            <option value="${weightMeasureUnit.id}"
                                    <#if weightMeasureUnit.id == bookInventory.weightMeasureUnit.id>selected</#if>>${weightMeasureUnit.name}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
		</div>
		
		<div class="row"    <#if bookInventory.id?exists> style="display:none"</#if>>
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">上架时间:</label>

                <div class="col-md-8">
                    <input type="text" class="form-control" name="stockInTime" id="stockInTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
              		value="<#if bookInventory.stockInTime?exists>${bookInventory.stockInTime?string('yyyy-MM-dd HH:mm')}</#if>"  data-parsley-required="true">
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">结算时间:</label>

                <div class="col-md-8">
                    <input type="text" class="form-control" name="settledTime" id="settledTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
              		value="<#if bookInventory.settledTime?exists>${bookInventory.settledTime?string('yyyy-MM-dd HH:mm')}</#if>"  data-parsley-required="true">
                </div>
            </div>
        </div>
	</div>
	
		 <div class="row">
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">规格:</label>

                <div class="col-md-8">
                    <input type="text" class="form-control" name="spec" id="spec" value="${bookInventory.spec}">
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">包装:</label>

                <div class="col-md-8">
                    <select id="packingId" name="packing.id" class="form-control">
                        <option value="">请选择包装类型</option>
                        <#list packings as packing>
                            <option value="${packing.id}" <#if packing.id == bookInventory.packing.id>selected</#if>>${packing.name}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
        </div>
         <div class="row">
    	<div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">产地:</label>

                <div class="col-md-8">
                    <input type="text" class="form-control" name="productionPlace" id="productionPlace" value="${bookInventory.productionPlace!}">
                </div>
            </div>
        </div>
        
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">预期保管时间:</label>

                <div class="col-md-8">
                    <div class="input-group">
                        <input type="text" class="form-control" name="storeDuration" id="storeDuration" value="${bookInventory.storeDuration}"
                               data-parsley-type="integer" data-parsley-min="0">
                        <span class="input-group-addon">天</span>
                    </div>
                </div>
            </div>
        </div>
        </div>
         <div class="row">
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">保质期:</label>

                <div class="col-md-8">
                    <div class="input-group">
                        <input type="text" class="form-control" name="quanlityGuaranteePeriod" id="qualityGuaranteePeriod"
                               value="${bookInventory.quanlityGuaranteePeriod}" data-parsley-type="integer" data-parsley-min="0">
                        <span class="input-group-addon">天</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">生产日期:</label>

                <div class="col-md-8">
                    <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" onClick="WdatePicker()" name="productionDate"
                           id="productionDate" value="<#if bookInventory.productionDate?exists>${bookInventory.productionDate?string('yyyy-MM-dd')}</#if>"
                           placeholder="生产日期">
                </div>
            </div>
        </div>
		</div>
		 <div class="row">
        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">商品状态:</label>

                <div class="col-md-8">
                    <select id="productStatusId" name="productStatus.id" class="form-control">

                        <#list productStatus as status>
                            <option value="${status.id}" <#if status.id == bookInventory.productStatus.id>selected</#if>>${status.name}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>

        <div class="col-md-5">
            <div class="form-group">
                <label class="col-md-4 control-label">是否合格:</label>

                <div class="col-md-8">
                    <input type="radio" name="qualified" checked value="true"  <#if  bookInventory.qualified>checked</#if>>合格
                    <input type="radio" name="qualified" value="false"  <#if  bookInventory.id?exists&&!bookInventory.qualified>checked</#if>>不合格
                </div>
            </div>
        </div>
		</div>
		
	
        <div class="col-md-10">
            <div class="form-group">
                <div class="col-md-offset-2 col-md-3">
                    <button type="submit" class="btn btn-primary">保存</button>
                </div>
            </div>
        </div>
    </form>
</div>

<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog-full modal-location-selector">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">选择储位</h4>
            </div>
            <div class="modal-body" id="selector-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div><!-- /.modal -->

 
<script>
 
	$(function(){
		 $("#storeContainerId").select2({
            placeholder: "",
            minimumInputLength: 8,
            ajax: { 
                 url: "${base}/store/book-inventory/findstoreContainers?id=${bookInventory.id}",
                dataType: 'json',
                data: function (term, page) {
                    return {
                        label: term   
                    };
                },
                results: function (data, page) {  
                  
                    var realNames = new Array();    
                         for (var i = 0; i < data.length; i++) {
                              realNames.push({id: data[i].id, text: data[i].label});
                       }
                    return {results: realNames};
                }
            },
            initSelection: function(element, callback) {
             
	            var id = $("storeContainerId").val();
				if (id !== "") {
					$.ajax("${base}/settings/store-container/"+id, {
					dataType: "json"
					}).done(function(data) { callback(data); });
				}
			} 
        });
	})
 
    $.get('${base}/store/store-location-selector?selectedLocationCode=${bookInventory.storeLocation.code}&areaId=${bookInventory.storeLocation.storeArea.id}&allowSelectStatus=1', function(data){
        $("#selector-body").html(data);
    });

    function locationNodeClick() {
        var result = $('#location-result');
        $("[title='"+result.text()+"']").parent().removeClass('stat-selected') ; 
        var title = $(this).attr('title');
        
        if ($(result).text().indexOf(title) == -1) {
            $(result).text(title);
            $(this).parent().addClass('stat-selected');
        } else {
            $(result).text('');
            $(this).parent().removeClass('stat-selected');
        }

        $('#storeLocation').val(title);

    }
    
 	function selectedCustomer(value) {
        $("#productId").empty();
        $("#productId").prepend('<option  value= >请选择商品</option>');
       
        if (value == '') {
            $("#productId").empty();
            return;
        }
        $.ajax({
            url: "${base}/settings/customer/customer-products?id=" +value,
            data: '',
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 if(ar.length>0){
           		   	for (var i = 0; i < ar.length; i++) {
                	var option = '<option  value='+ar[i].id+'>'+ar[i].code+ar[i].name+'</option>';
                	$("#productId").append(option);
            		}
           		}
         	}
        })	
    }
   
 	function selectedproduct(productId) {
     	 
     	 
        if (productId == '') {
           return;
        }
        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" +productId,
            data: '',
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 $("#packingId").val(ar.commonPacking.id);
           		 $("#spec").val(ar.spec);
           		  $("#productionPlace").val(ar.productionPlace);
           		 var amount= $("#amount").val();
           		
           		 var reg = "^[1-9]*[1-9][0-9]*$";
           		 if(amount.match(reg)!=null){
           		 	var count=0;
           		 	if(amount%ar.bearingCapacity>0){
           		 		count=1
           		 	}
					$("#storeContainerCount").val((amount-amount%ar.bearingCapacity)/ar.bearingCapacity+count);
					
					$("#weight").val(amount*ar.weight);
           		 }
           	}
        })	
    }  
    
</script>
</#escape>
