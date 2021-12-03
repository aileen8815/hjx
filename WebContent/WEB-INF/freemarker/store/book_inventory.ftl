<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/book-inventory">库存查询</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#book-inventory-toolbar" id="book-inventory-datagrid" 
      data-options="
        url:'${base}/store/book-inventory/list',
        method:'get',
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        fitColumns:true,
        collapsible:false">
    <thead>
      <tr>
        <th data-options="field:'customeName',width:60,fixed:true">客户</th>
        <th data-options="field:'productName',width:70,formatter: rowformater">商品名称</th>
        <th data-options="field:'storeLocationCode',width:120,fixed:true">储位编码</th>
        <th data-options="field:'storeContainerLabel',width:120,fixed:true">托盘标签</th>
        <th data-options="field:'amount',width:50,fixed:true">数量</th>
        <th data-options="field:'amountMeasureUnitName',width:60,fixed:true">数量单位</th>
        <th data-options="field:'weight',width:50,fixed:true">重量</th>
        <th data-options="field:'weightMeasureUnitName',width:60,fixed:true">重量单位</th>
        <th data-options="field:'packingName',width:60,fixed:true">包装</th>
        <th data-options="field:'quanlityGuaranteePeriod',width:80,fixed:true">保质期</th>
        <th data-options="field:'storeDuration',width:60,fixed:true">预管时间</th> 
        <th data-options="field:'qualified',width:80,fixed:true,formatter: rowforQualified">是否合格</th>
        <th data-options="field:'productDetail',width:120,fixed:true">多品明细</th>          
      </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="book-inventory-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/store/book-inventory/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 录入</a>
             <button id="book-inventory-edit" type="button" class="btn btn-primary" onclick="bookinventoryjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="book-inventory-delete" type="button" class="btn btn-primary" onclick="bookinventoryjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-10">
        <div style="text-align:right">
          <form class="form-inline" id="book-inventory-form">
            <div class="form-group">
                <label class="sr-only" for="customerId">客户</label>
                <select class="form-control" id="customerId" name="customerId"  onchange="selectedCustomer(this.value)"   style="width:160px;text-align:left;margin-right:6px;">
                    <option value="">选择客户</option>
                    <#list customers as c>
                    <option value="${c.id!}">${c.text!!}</opton>
                    </#list>
                </select>
            </div>           
            <div class="form-group">
                <label class="sr-only" for="storeAreaId">选择库区</label>
                <select class="form-control" id="storeAreaId" name="storeAreaId" style="width:160px;text-align:left;margin-right:6px;">
                    <option value="">选择库区</option>
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
            
            <div class="form-group">
                <label class="sr-only" for="storeContainerId">选择托盘</label>
                <div class="col-md-6">
                        <input type="text" class="form-control" name="storeContainerId" id="storeContainerId"
                               placeholder="请输入托盘号" onfocus="javascript:if(this.value=='请输入托盘号')this.value='';">
                    </div>
            </div>        
            
            <div class="form-group">
                <label class="sr-only" for="storeAreaId">选择商品</label>
                <select class="form-control" id="productId" name="productId" style="width:220px;text-align:left;margin-right:6px;">
                    <option value="">请选择商品</option>
                   
                </select>
            </div>  
                      
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="bookinventoryjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <#--
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul book-inventory="menu" class="dropdown-menu search-plus">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><i class="fa fa-search-plus"></i> 高级查询</a></li>
                </ul>
                -->
            </div>
          </form>
        </div>
         
      </div>
    </div>
  </div>
</div>
<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>
<script type="text/javascript">
  var bookinventoryjs = {
    search: function(){  
      $('#book-inventory-datagrid').datagrid('load',{
          storeAreaId: $('#storeAreaId').val(),
          productId: $('#productId').val(),
          customerId: $('#customerId').val(),
          storeContainerId:$('#storeContainerId').val()
      });
    },
    
    edit: function(){
      var row = $('#book-inventory-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/store/book-inventory/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#book-inventory-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/book-inventory/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
  
  	function rowforQualified(value,row,index){
  		if(value){
  		return "合格";
  		}
  		return "不合格";
  	}
   function rowformater(value,row,index){
    var url="${base}/store/book-inventory/view?id="+row.id;
 	 return "<a href='"+url+"' >"+value+"</a>";
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
  
  <#--
	$(function () {
    $('#productName').ztreeSelect({
        hiddenInput: '#productId',
        url: "${base}/settings/product/ztree"
    });
	});-->
</script>

</#escape>
