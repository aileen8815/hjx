<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/permission">系统权限</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <table class="easyui-treegrid"
      data-options="
        url: '${base}/security/permission/tree',
        method: 'get',
        fitColumns: 'true',
        rownumbers: true,
        idField: 'id',
        treeField: 'name'
      ">
    <thead>
      <tr>
        <th data-options="field:'name',fixed:'true'" width="200">名称</th>
        <th data-options="
          field:'code',
          <#-- 演示TreeGrid选中
          formatter:function(value,rowData,rowIndex){
            return '<input type=\'checkbox\' name=\'permission\' value=\'' + rowData.id + '\'> ' + rowData.code;
          },
          -->
          fixed:'true'" width="200">编码</th>
        <th data-options="field:'remark'" width="100">说明</th>
      </tr>
    </thead>
  </table>
</div>
<#-- 演示TreeGrid选中
<script>
function getSelected(){ 
    var idList = "";  
     $("input:checked[name='permission']").each(function(){
        var id = $(this).val();
        alert(id); 
     })
}
</script>
-->
</#escape>
