<#escape x as x?html>
<script src="${base}/assets/easyui/plugins/treegrid-dnd.js"></script>
<header class="panel-heading">
    <a href="${base}/settings/product-category">商品目录</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <table class="easyui-treegrid" toolbar="#department-toolbar" id="department-treegrid" 
      data-options="
        url: '${base}/settings/product-category/tree',
        method: 'get',
        fitColumns: 'true',
        rownumbers: true,
        idField: 'id',
        treeField: 'code',
        onLoadSuccess: function(row){
          $(this).treegrid('enableDnd', row?row.id:null);
        },
        onDrop: departmentjs.dnd
      ">
    <thead>
      <tr>
        <th data-options="
          field:'code',
          fixed:'true'" width="200">编码</th>
        <th data-options="field:'name',fixed:'true'" width="200">名称</th>
      </tr>
    </thead>
  </table>

  <#-- 表格工具条 -->
  <div class="col-md-12" id="department-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-7">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/product-category/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建顶级商品</a>
            <button id="department-edit" type="button" class="btn btn-primary" onclick="departmentjs.addSub();"><i class="fa fa-plus"></i> 新建下级商品</button>
            <button id="department-edit" type="button" class="btn btn-primary" onclick="departmentjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="department-delete" type="button" class="btn btn-primary" onclick="departmentjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-5">
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var departmentjs = {
    addSub: function(){
      var row = $('#department-treegrid').treegrid('getSelected');  
      if (row){
        location.href = "${base}/settings/product-category/new?parentId=" + row.id;
      } else {
        alert('请选择上级商品类别！');
      }
    },

    edit: function(){
      var row = $('#department-treegrid').treegrid('getSelected');  
      if (row){
        location.href = "${base}/settings/product-category/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#department-treegrid').treegrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/product-category/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    },
    
    dnd: function(targetRow,sourceRow,point){
      var url = "${base}/settings/product-category/" + sourceRow.id + "/organise?parentId=" + targetRow.id + "&point=" + point;
      $.get(url, function(data){
        // do noting
      });
    }
  }; 
</script>
</#escape>
