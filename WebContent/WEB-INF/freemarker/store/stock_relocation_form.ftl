<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/Stock-relocation">库存移位</a> 
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <!-- 明细表单 -->
    <form id="stockRelocation-form" action="${base}/store/Stock-relocation/${stockRelocation.id}/save" method="post" class="form-horizontal" data-parsley-validate>
  
   	 <input type="hidden" name="createdBy" value="${stockRelocation.createdBy!}">
     <input type="hidden" name="createdTime" value="${stockRelocation.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
   
	 	
      <div class="form-group">
        <label class="col-md-2 control-label">原储位:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="fromlocationCode" id="fromlocationCode" data-parsley-required="true"  value="${stockRelocation.fromStoreLocation.code}"  >
        </div>
        <div class="col-md-1">
          <a class="btn btn-info"  href="javascript:void(0)"  onclick="locationNodeClick()"><i class="fa fa-ellipsis-v"></i> 读标签</a>
        </div>
      </div>
      <div id="modal-item-ext">
          <div class="form-group">
            <label class="col-md-2 control-label">托盘:</label>
            <div class="col-md-3">
            <input type="text" class="form-control" id="storeContainerName" name="storeContainer.label" value="${stockRelocation.storeContainer.label}"  readonly>
            <input type="hidden" class="form-control" id="storeContainerId" name="storeContainer.id" value="${stockRelocation.storeContainer.id}">
                
            </div>
            <div class="col-md-2">
               
            </div>
          </div>
			
          <div class="form-group">
            <label class="col-md-2 control-label">客户:</label>
            <div class="col-md-4">
        				<select id="customerId" name="customer.id"  id="customerId"  class="form-control"   data-parsley-required="true">
                            <option value="">请选择客户</option>
                            <#list customers as customer>
                                <option value="${customer.id}"
                                        <#if customer.id == stockRelocation.customer.id>selected</#if>>${customer.text!}</option>
                            </#list>
                        </select>
            </div>
          </div>
          
		<div class="form-group"   <#if !stockRelocation.id?exists> style="display:none"</#if>>
        <label class="col-md-2 control-label">目标储位:</label>
        <div class="col-md-3">
          <input type="text" class="form-control" name="tolocationCode" id="tolocationCode"   >
        </div>
        <div class="col-md-1">
           <a class="btn btn-info"  href="javascript:void(0)"  onclick="locationNodeClick()"><i class="fa fa-ellipsis-v"></i> 读标签</a>
        </div>
      </div>
      
      <div class="form-group">
        <div class="col-md-offset-2 col-md-9">
          <button class="btn btn-success">保存</button>
        </div>
      </div>
    </form>

<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
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
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal   -->

<script>
    $.get('${base}/store/store-location-selector/stock-wastage?customerId=${customerId}', function(data){
        $("#selector-body").html(data);
    });

   function locationNodeClick(){
 	  $.ajax({
        url: "${base}/store/Stock-relocation/select-storelocation",
        data: {"storeLocation":  $('#fromlocationCode').val()},
        type: "POST",
        success: function(result) {
       		 var ar = eval('(' + result + ')');
       		 setform(ar);
        }
      })
    }
    
   function setform (ar){
	 	$("#storeContainerId").val(ar.storeContainer.id);
 		$("#storeContainerName").val(ar.storeContainer.label);
 		$("#customerId").select2("val", ar.customer.id);
    	
   }
</script>

</div>
</#escape>