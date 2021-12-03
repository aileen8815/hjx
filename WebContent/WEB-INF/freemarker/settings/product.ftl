<#escape x as x?html>
<header class="panel-heading">
<script src="${base}/assets/easyui/plugins/treegrid-dnd.js"></script>
    <a href="${base}/settings/product">商品管理</a>
    
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <#-- JEasyUI DataGrid 显示数据 -->
    <table class="easyui-treegrid" toolbar="#product-toolbar" id="product-treegrid" 
      data-options="
        url: '${base}/settings/product/tree',
        method: 'get',
        fitColumns: 'true',
        rownumbers: true,
        idField: 'id',
        treeField: 'code',
        onLoadSuccess: function(row){
          $(this).treegrid('enableDnd', row?row.id:null);
        },
        onDrop: productjs.dnd
      ">
    <thead>
      <tr>
        <th data-options="field:'code', fixed:'true'" width="200">商品编码</th>
        <th data-options="field:'name',fixed:'true'" width="200">商品名称</th>
        <th data-options="field:'commonUnitName',width:100,fixed:true">常用计量单位</th>
        <th data-options="field:'commonPackingName',width:100,fixed:true">常用包装</th>
		<th data-options="field:'priceRange',width:100,fixed:true">参考价格区间</th>
        <th data-options="field:'remark',width:200">备注</th>
      </tr>
    </thead>
  </table>
  

  
  <#-- 表格工具条 -->
  <div class="col-md-12" id="product-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-8">
        <div class="m-b-sm">
          <div class="btn-group">
           	<a href="${base}/settings/product/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建</a>
         <!--   <button id="product-edit" type="button" class="btn btn-primary" onclick="productjs.addSub();"><i class="fa fa-plus"></i> 新建下级商品</button>
            -->
            <button id="product-edit" type="button" class="btn btn-primary" onclick="productjs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="product-delete" type="button" class="btn btn-primary" onclick="productjs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
            
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div style="text-align:right">
          <form class="form-inline" role="form">
            <div class="form-group">
              <label class="sr-only" for="a">名称</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="编码或名称">
            </div>
            <div class="btn-group">
                <button class="btn btn-primary btn-small" onclick="productjs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
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
  var productjs = {
    search: function(){  
      $('#product-treegrid').datagrid('load',{
          name: $('#name').val()
      });
    },
    
	 addSub: function(){
      var row = $('#product-treegrid').treegrid('getSelected');  
      if (row){
        location.href = "${base}/settings/product/new?parentId=" + row.id;
      } else {
        alert('请选择上级部门！');
      }
    },

    edit: function(){
      var row = $('#product-treegrid').treegrid('getSelected');  
      if (row){
        location.href = "${base}/settings/product/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#product-treegrid').treegrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/product/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    },
    
    dnd: function(targetRow,sourceRow,point){
      var url = "${base}/settings/product/" + sourceRow.id + "/organise?parentId=" + targetRow.id + "&point=" + point;
      $.get(url, function(data){
        // do noting
      });
    }
  }; 
</script>

</#escape>
