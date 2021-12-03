<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/customer-outbound-booking">出库预约</a> - 
    <#if outboundBooking.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="outboundBooking-form" action="${base}/customer/customer-outbound-booking/${outboundBooking.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="createdBy" value="${outboundBooking.createdBy!}">
    <input type="hidden" name="createdTime" value="${outboundBooking.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" name="bookTime" value="${outboundBooking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${outboundBooking.serialNo}"  >
  	<input type="hidden" name="bookingStatus"   value="${outboundBooking.bookingStatus}" >
  	<input type="hidden" name="customer.id"  id="customerId" value="${outboundBooking.customer.id}" >
  	<input type="hidden" name="bookingMethod.id"   value="${outboundBooking.bookingMethod.id}" >
  	<input type="hidden" class="form-control" name="productCheck" id="productCheck" value="${productCheck}" >
  <#--预约单信息 -->
  
 <div class="row">
 <div class="col-md-11 row">
 
  <div class="col-md-6">
     <div class="form-group">
      <label class="col-md-4 control-label">出库时间:</label>
      <div class="col-md-8">
      <input type="text" class="form-control Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"   name="applyOutboundTime" id="applyOutboundTime" value="<#if outboundBooking
      .applyOutboundTime?exists>${outboundBooking.applyOutboundTime?string('yyyy-MM-dd HH:mm:ss')}</#if>"   placeholder="申请出库时间"  data-parsley-required="true">
      </div>
    </div>
    </div>
    
  <div class="col-md-6">
    <div class="form-group" >
      <label class="col-md-4 control-label">预约仓间:</label>
      <div class="col-md-8">
      <select id="storeAreaId" name="storeArea.id" class="form-control"  data-parsley-required="true">
       <option value="">请选择仓间</option>
       <#list storeAreas as storeArea>
     	 <option value="${storeArea.id}" <#if storeArea.id == outboundBooking.storeArea.id>selected</#if>>${storeArea.name}</option>
	   </#list>
       </select>
      </div>
 	</div>
 	</div>

 
	<div class="col-md-12"  id="showcontactinfo" >
	         <div class="form-group"  >
	              <label class="col-md-2 control-label" >联系人列表:</label>
	             	<div class="col-md-10" id="cancatlist">
	             	 
	             	<#list contactSet as contact>
     	 			<input type=radio name=contact.id   value="${contact.id}"	 <#if contact.id == outboundBooking.contact.id>checked</#if> onclick="setval()" >${contact.contactinfo}<a href="javascript:void(0)"  onclick="contactjs.editlinkman(${contact.id})" >编辑</a><a href="javascript:void(0)"  onclick="contactjs.dellinkman(${contact.id})" >   删除</a></br>
	  				</#list>
	            	<a href="javascript:void(0)"   data-toggle="modal" data-target="#location-selector-modal" >添加联系人</a></br>
	            
	             		
	             	</div>
	          </div> 
	 </div>
	 

 <div class="col-md-6">
    <div class="form-group">
      <label class="col-md-4 control-label">车辆类型:</label>
      <div class="col-md-8">
      <select id="vehicleTypeId" name="vehicleType.id" class="form-control">
       <option value="">请选择车辆类型</option>
       <#list vehicleTypes as vehicleType>
     	 <option value="${vehicleType.id}" <#if vehicleType.id == outboundBooking.vehicleType.id>selected</#if>>${vehicleType.name}</option>
	   </#list>
       </select>
      </div>
    </div>
    </div >
 
 <div class="col-md-6">
    <div class="form-group">
      <label class="col-md-4 control-label">车辆数目:</label>
      <div class="col-md-8"> 
        <input type="text" class="form-control" name="vehicleAmount" id="vehicleAmount" value="${outboundBooking.vehicleAmount}"    data-parsley-type="integer"  data-parsley-min="0">
      </div>
    </div>
   </div>
    
 <div class="col-md-6">
    <div class="form-group">
      <label class="col-md-4 control-label">车牌号:</label>
      <div class="col-md-8">
        <input type="text" class="form-control" name="vehicleNumbers" id="vehicleNumbers" value="${outboundBooking.vehicleNumbers}" >
      </div>
    </div>
  </div>

 <div class="col-md-12">
  <div class="form-group"  >
      <label class="col-md-2 control-label">客户留言:</label>
      <div class="col-md-10">
        <textarea class="form-control" name="note" id="note" rows="2"></textarea>
      </div>
  </div>
  </div>
   <#--商品详细 -->
 
  <#-- JEasyUI DataGrid 显示数据 -->
   <div class="col-md-offset-2 col-md-10">
  <table class="easyui-datagrid"  id="outbound-booking-datagrid" 
      data-options="url:'${base}/customer/customer-outbound-booking/bookinventory-list?outboundbookingId=${outboundBooking.id}&&customerId=${outboundBooking.customer.id}',method:'get',rownumbers:true,singleSelect:false,selectOnCheck:true,checkOnSelect:false,
      pagination:false,fitColumns:true,collapsible:false,onLoadSuccess:loadSuccess">
    <thead>
     <tr>
     	<th data-options="field:'batchProduct',checkbox:true"></th>
		<th data-options="field:'productName',width:100">商品</th>
		<th data-options="field:'amount',width:80">库存数量</th>
        <th data-options="field:'outboundAmount',width:100,formatter: rowformater">出库数量</th>
        <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
        <th data-options="field:'totalStoreContainer',width:100">库存托数</th>
        <th data-options="field:'storeContainerCount',formatter: storeContainerCount,width:80">预计出库托数</th>

        <th data-options="field:'weight',width:90, formatter:fixedformater">库存重量</th>
        <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>
        <th data-options="field:'inboundRegisterSerialNo',width:150">批次</th> 
     </tr>
    </thead>
  </table>
 </div>
 
            <div class="col-md-12">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-3">
                          <button type="button" class="btn btn-primary" onclick="getData()">保存</button>
                    </div>
                </div>
            </div>
 
 
 
    </div>
   </div>
  </form>
</div>

 <!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">联系人基本信息</h4>
      </div>
       <form id="linkman-form" action=""  method="post"  onsubmit="return contactjs.saveContact()"  class="form-horizontal" data-parsley-validate>
      <input type="hidden" class="form-control" name="contactid" id="contactid" value="" >
      <div id="selector-body" class="modal-body">
 
       <div class="row"    >
	         <div class=" col-md-12  ">
	         	<div class="form-group">
	         		<div class="row">
	       	 			<div class="col-md-6">
	           		<div class="form-group">
	             	 	<label class="col-md-4 control-label">联系人:</label>
	             	<div class="col-md-8">
	                	<input type="text" class="form-control" name="linkman" id="linkman" value="${contact.linkman!}"  data-parsley-required="true">
	             	</div>
	           </div>
	    </div>
	    <div class="col-md-6">
	           	<div class="form-group">
	              	<label class="col-md-4 control-label">手机:</label>
	              		<div class="col-md-8">
	                 	<input type="text" class="form-control" name="mobile" id="mobile" value="${contact.mobile!}"  data-parsley-required="true">
	             		</div>
	            	</div>
	       		</div>
			</div>
            	
        <div class="row">
       	 <div class="col-md-6">
            <div class="form-group">
              <label class="col-md-4 control-label">传真:</label>
              <div class="col-md-8">
                 <input type="text" class="form-control" name="fax" id="fax" value="${contact.fax!}">
              </div>
            </div>
        </div>
        
        <div class="col-md-6">
			 <div class="form-group">
			      <label class="col-md-4 control-label">电话:</label>
			          <div class="col-md-8">
			         	<input type="text" class="form-control" name="tel" id="tel" value="${contact.tel!}" >
			       </div>
			  </div>
		</div>
   	 </div> 	
   	             	
        <div class="row">
   		<div class="col-md-6">
            <div class="form-group">
              <label class="col-md-4 control-label">电子邮箱:</label>
              <div class="col-md-8">
                 <input type="text" class="form-control" name="email" id="email" value="${contact.email!}"    data-parsley-type="email">
              </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-group">
              <label class="col-md-4 control-label">邮编:</label>
              <div class="col-md-8">
                 <input type="text" class="form-control" name="zip" id="zip" value="${contact.zip!}" 	>
              </div>
            </div>
       </div>
   	 </div> 	
      <div class="row">
       	 <div class="col-md-6">
            <div class="form-group">
              <label class="col-md-4 control-label">地址:</label>
              <div class="col-md-8">
              <textarea class="form-control" name="address" id="address" rows="1"  value="${contact.address!}"></textarea>
              </div>
            </div>
       </div>
   </div>
    
    </div>
      <div class="modal-footer">
        <button type="submit"     class="btn btn-primary">保存</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal"   onclick="contactjs.setlinkmanform()">关闭</button>
      </div>
        </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
	  $('#note').val('${outboundBooking.note}');
 	var contactjs = {
        saveContact: function (){
 		  if($("#linkman").val().trim()==""||$("#mobile").val().trim()==""){
 		  	return false ;
 		  }
 		 if( $("#email").val().trim() != ""){
 		 var myreg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		 if(!myreg.test($("#email").val().trim())){
		 	return false;
		 }
		 }
		$.ajax({
        url: "${base}/customer/contact/"+$("#contactid").val()+"/create-contact",
        data: {"id": $("#contactid").val(),"linkman": $("#linkman").val(),"mobile": $("#mobile").val(),"tel": $("#tel").val(),
        "fax": $("#fax").val(),"zip": $("#zip").val(),"address": $("#address").val(),"email": $("#email").val(),"customerId": $("#customerId").val()
        	  },
        type: "POST",
        success: function(data) {
       			
     	 		if($("#contactid").val()==''){//新建
     	 			var ar=  eval('(' + data + ')');
     				var  t2=$("#cancatlist");
     	 			var  info='<input type=radio name=contact.id id='+ar.id+'  checked value="'+ar.id+'"  >'+ar.contactinfo+'<a href="#"  onclick="contactjs.editlinkman('+ar.id+')" >编辑</a><a href="javascript:void(0)"  onclick="contactjs.dellinkman('+ar.id+')" >   删除</a></br>';
     	 			t2.prepend(info);
     	  		}else{//编辑
   		 			contactjs.successData(data);
         		}
     	  	 	contactjs.setlinkmanform();
     	}
      })
      			$('#location-selector-modal').modal('hide');
      			
 				return false;
	  },
	  
	  setlinkmanform: function (id,linkman,mobile,tel,fax,zip,address,email){
	  	 $("#contactid").val(id);
	  	 $("#linkman").val(linkman);
	  	 $("#mobile").val(mobile); 
	  	 $("#tel").val(tel);
	  	 $("#fax").val(fax);
	  	 $("#zip").val(zip);
	  	 $("#address").val(address);
	  	 $("#email").val(email);
	  	 $("#linkman-form").parsley().reset();
 
	  },
	  
	  successData: function (data){
	      var ar=  eval('(' + data + ')');
     	  var  t2=$("#cancatlist");
     	  t2.text('');
		for(var i=0;i<ar.length;i++){
     	 		var  info='<input type=radio name=contact.id id='+ar[i].id+'  checked value="'+ar[i].id+'"  >'+ar[i].contactinfo+'<a href="javascript:void(0)"  onclick="contactjs.editlinkman('+ar[i].id+')" >编辑</a><a href="javascript:void(0)"  onclick="contactjs.dellinkman('+ar[i].id+')" >   删除</a></br>';
       		    t2.prepend(info);
     	 }
         		t2.append('<a href="javascript:void(0)"   data-toggle="modal" data-target="#location-selector-modal" >添加联系人</a>');
	  },
	  
	  editlinkman: function (id){
	  	 
		$.ajax({
        url: "${base}/customer/contact/booking-edit-contact",
        data: {"id": id,"customerId": $("#customerId").val()},
        type: "POST",
        success: function(data) {
       			var ar=  eval('(' + data + ')');
     	  	 	contactjs.setlinkmanform(ar.id,ar.linkman,ar.mobile,ar.tel,ar.fax,ar.zip,ar.address,ar.email);
     	}
      })
      			$('#location-selector-modal').modal('show');
      			
 	 
	  },
	  
	 dellinkman:function (id){
	  	 
		$.ajax({
        url: "${base}/customer/contact/booking-del-contact",
        data: {"id": id,"customerId": $("#customerId").val()},
        type: "POST",
        success: function(data) {
				contactjs.successData(data);
     	}
      })
	 }
   }
	 function getData(){
		var checkedItems = $('#outbound-booking-datagrid').datagrid('getChecked');
		var ids = [];
		var result=true;
		var reg =  "^[1-9]*[1-9][0-9]*$";
		$.each(checkedItems, function(index, item){
			var outboundAmount=$('#val_'+item.batchProduct).val();
			var storeContainerCount=$('#str_'+item.batchProduct).val();
			ids.push(item.batchProduct+"_"+outboundAmount+"_"+storeContainerCount);
			if(outboundAmount.match(reg)==null||outboundAmount>item.amount||storeContainerCount.match(reg)==null||storeContainerCount>item.totalStoreContainer ){
				result=false;
				return result;
			}
	    });
	     $('#productCheck').val(ids);
	     if(!result){
	    	alert("选择的商品出库数量和出库托数需大于零并且不能大于库存数");
	     }
	 	 
	     if(result){
	     	$("#outboundBooking-form").submit();
	     }
	 }
		 
	function loadSuccess(data){
		if(data){
			$.each(data.rows, function(index, item){
		      <#list outboundBooking.outboundBookingItems as outboundBookingItem>
			      if(item.batchProduct=='${outboundBookingItem.batchProduct}'){
					$('#outbound-booking-datagrid').datagrid('checkRow', index);
				  }
        	  </#list>
		
			});
		}
	}
 
   function selectedproduct(id,value) {
        if (id.trim() == '') {
            return;
        }
        var  batchProduct= id.replace(/val_/, "");
       	var  batchProductArry = batchProduct.split('_');
        $.ajax({
            url: "${base}/customer/customer-outbound-booking/getproduct?id=" + batchProductArry[1],
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');
          		var reg = "^[1-9]*[1-9][0-9]*$";
                if (value.match(reg) != null) {
                    var count = 0;
                    if (value % ar.bearingCapacity > 0) {
                        count = 1
                    }
                  $("#"+"str_"+batchProduct).val((value - value % ar.bearingCapacity) / ar.bearingCapacity + count);

                }
            }
        })
    }
     function rowformater(value,row,index){
  		return   '<input type="text"  name="outboundAmount" id=val_'+row.batchProduct+' style="color:red"   onchange="selectedproduct(this.id,this.value)" data-parsley-type="integer"  value='+value+' >'
  	}
  	
  	function fixedformater(value){
      var  v=value.toFixed(2);
     <!-- console.log(v); -->
      return v;
  	}
  	
  	function storeContainerCount(value,row,index){
  		return   '<input type="text"  name="storeContainerCount" id=str_'+row.batchProduct+' style="color:black" readonly  data-parsley-type="integer"  value='+value+' >'
  	}
  	
    

</script>
</#escape>
