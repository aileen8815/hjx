<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/contact">客户联系人信息</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
  <table class="easyui-datagrid" toolbar="#contact-toolbar" id="contact-datagrid" 
      data-options="url:'${base}/customer/contact/list',method:'get',rownumbers:true,singleSelect:true,pagination:false,fitColumns:true,collapsible:false">
    <thead>
     <tr>
        <th data-options="field:'linkman',width:100">联系人名</th>
        <th data-options="field:'mobile',width:100">手机</th>
		<th data-options="field:'tel',width:100">电话</th>
        <th data-options="field:'fax',width:100">传真</th>
        <th data-options="field:'email',width:100">邮箱</th>
        <th data-options="field:'zip',width:100">邮编</th>
        <th data-options="field:'address',width:100">地址</th>
        
     </tr>
    </thead>
  </table>
  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="contact-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-4">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/customer/contact/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
            <button id="person-edit" type="button" class="btn btn-primary" onclick="contactjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="person-delete" type="button" class="btn btn-primary" onclick="contactjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div style="text-align:right">
          <form class="form-inline" role="form">
   
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var contactjs = {
    search: function(){  
      $('#contact-datagrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
    edit: function(){
      var row = $('#contact-datagrid').datagrid('getSelected');  
      if (row){
        location.href = "${base}/customer/contact/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#contact-datagrid').datagrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/customer/contact/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    }
  }; 
</script>

</#escape>
