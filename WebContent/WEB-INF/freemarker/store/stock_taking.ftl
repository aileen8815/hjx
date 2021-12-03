<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-taking?operate=${operate}">	
    <#if operate==1>
	盘点审核列表
	 <script>
             var pageIndicator = "stock-taking-check";
     </script>
	<#else>
	盘点列表
	</#if></a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#stock-taking-toolbar" id="stock-taking-datagrid" 
      data-options="url:'${base}/store/stock-taking/list?operate=${operate}',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100,formatter: rowformater">盘点单号</th>
        <th data-options="field:'stockTakingOperatorName',width:80">盘点人</th>
		<th data-options="field:'startTime',width:100,formatter:formatterDate">盘点开始时间</th>
		<th data-options="field:'endTime',width:100,formatter:formatterDate">盘点结束时间</th>
		<th data-options="field:'stockTakingMode',width:80">盘点方式</th>
        <th data-options="field:'stockTakingType',width:80">盘点类型</th>
        <th data-options="field:'stockTakingStatus',width:80">盘点状态</th>
        <th data-options="field:'remark',width:160">盘点备注</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="stock-taking-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
             <#if operate!=1>
             <a href="${base}/store/stock-taking/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a> 
             <button id="store-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 作废</button>       	
          	</#if>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
         <form class="form-inline" operator="form">
        	 <#if operate!=1>
  			
        	 <div class="form-group"">
      		 <label class="sr-only" for="a"> 盘点状态</label>
      		 <select id="stockTakingStatus" name="stockTakingStatus" class="form-control" style="width:100px;text-align: left;">
       		 <option value="">盘点状态</option>
  			<#list stockTakingStatus as stockTakingStatu>
     		 <option value="${stockTakingStatu}" >${stockTakingStatu}</option>
	  		 </#list>
     		 </select>
      		</div>
			</#if>
      		 <div class="form-group">
                <input type="text" class="form-control" style="width:200px;"  name="serialNo" id="serialNo" value=""  placeholder="盘点单号" >
  			</div>
            
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
     
            </div>
       
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var storelocationtypejs = {
    search: function(){  
      $('#stock-taking-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
          stockTakingStatus: $('#stockTakingStatus').val(),
        
      });
    },
    
 

    remove: function(){
      var row = $('#stock-taking-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要作废选中数据吗？')){
          if(row.stockTakingStatus!='待盘点'){
            alert('不是待盘点状态不可作废！');
          	return false;
          }
          location.href = "${base}/store/stock-taking/" + row.id + "/remove";
        }
      }else{
        alert('请选择要作废的数据！');
      }
    }
  }; 
  
     function	formatterDate(value){
                    var date = new Date(value);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    var d = date.getDate();
                    var hour = date.getHours();
                    var min = date.getMinutes();
                    var sec = date.getSeconds();
                    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
                    dateFormat(date,'yyyy-mm-dd HH:MM:SS');
                    return date.format('yyyy-mm-dd HH:MM:ss');
    }
        
    function rowformater(value,row,index){
    var url="${base}/store/stock-taking/"+row.id+"?operate=${operate}";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
