<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/Stock-relocation">库存移位</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#store_location_type-toolbar" id="store_location_type-datagrid" 
      data-options="url:'${base}/store/Stock-relocation/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'serialNo',width:100">单号</th>
        <th data-options="field:'customerName',width:100">客户</th>
		<th data-options="field:'operatorTime',width:100,formatter:formatterDate">登记时间</th>
		<th data-options="field:'operatorName',width:100">登记人</th>
        <th data-options="field:'stockRelocationStatus',width:100">状态</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="store_location_type-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
             <a href="${base}/store/Stock-relocation/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a> 
            <button id="store-delete" type="button" class="btn btn-primary" onclick="storelocationtypejs.remove();"><i class="fa fa-trash-o"></i> 删除</button>                  	
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
         <form class="form-inline" operator="form">
        	 
    
        

      		 <div class="form-group">
                <input type="text" class="form-control" style="width:100px;"  name="serialNo" id="serialNo" value=""  placeholder="单号" >
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
      $('#store_location_type-datagrid').datagrid('load',{
          serialNo: $('#serialNo').val(),
 
         
      });
    },
    
    save: function(){
      var customerId = $('#customerId').val();  
      if (customerId!=''){
        location.href = "${base}/store/Stock-relocationsave?customerId="+customerId;
      } else {
        alert('请先选择客户！');
      }
    },

    remove: function(){
      var row = $('#store_location_type-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/store/Stock-relocation" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
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
    var url="${base}/store/Stock-relocation"+row.id;
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
