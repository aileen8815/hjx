<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/customer/grade-approve">客户等级审批</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#customer-toolbar" id="customer-datagrid" 
      data-options="url:'${base}/settings/customer/list',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'sKHDM',width:100,formatter: rowformater">客户编号</th>
        <th data-options="field:'sMemberCode',width:100">卡号</th>
       	<th data-options="field:'name',width:100">客户名称</th>
       	<th data-options="field:'shortName',width:100">客户简称</th>
        <th data-options="field:'mobile',width:150">手机</th>
        <th data-options="field:'credentialsNo',width:150">证件证号</th>
        <th data-options="field:'customerGradeName',width:100">客户等级</th>
        <th data-options="field:'customerGradeStatusName',width:100">审批状态</th>
        <th data-options="field:'approverName',width:100">审批人员</th>
        <th data-options="field:'approveTime',width:150,formatter:formatterDate">审批时间</th>
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="customer-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <button id="person-edit" type="button" class="btn btn-primary" onclick="customerjs.edit();"><i class="fa fa-edit"></i> 审批编辑</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
          <div class="form-group">
      		 <label class="sr-only" for="a"> 审批状态</label>
      		 <select id="customerGradeStatus" name="customerGradeStatus" class="form-control" style="width:150px;text-align: left;">
       		 <option value="">选择审批状态</option>
      		 <#list customerGradeStatuss as customerGradeStatus>
     		 <option value="${customerGradeStatus}" >&nbsp${customerGradeStatus}&nbsp</option>
	  		 </#list>
     		 </select>
      		</div>
            <div class="form-group">
              <label class="sr-only" for="a">名称</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="名称">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="customerjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
                <#--
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle" type="button"><span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu search-plus">
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

<script type="text/javascript">
  var customerjs = {
    search: function(){  
      $('#customer-datagrid').datagrid('load',{
          name: $('#name').val(),
          customerGradeStatus: $('#customerGradeStatus').val()
      });
    },
    
    edit: function(){
      var row = $('#customer-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/settings/customer/" + row.id + "/grade-approve";
      } else {
        alert('请选择要编辑的数据！');
      }
    }
  }; 
  
  function formatterDate(value) {
        if(value !== null)
        {
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
        dateFormat(date, 'yyyy-mm-dd HH:MM:SS');
        return date.format('yyyy-mm-dd HH:MM:ss');
        }else
        {
        return '';
        }
    }
  
      function rowformater(value,row,index){
    var url="${base}/settings/customer/"+row.id+"/grade-approve";
 	 return "<a href='"+url+"' >"+value+"</a>";
  }
</script>

</#escape>
