<#escape x as x?html>
<script src="${base}/assets/easyui/plugins/treegrid-dnd.js"></script>
<header class="panel-heading">
    <a href="${base}/settings/area">行政区划</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <table class="easyui-treegrid" toolbar="#area-toolbar" id="area-treegrid" 
      data-options="
        url: '${base}/settings/area/tree',
        method: 'get',
        fitColumns: 'true',
        rownumbers: false,
        idField: 'id',
        treeField: 'code',
       	onBeforeExpand: openArea,
        onLoadSuccess: function(row){
          $(this).treegrid('enableDnd', row?row.id:null);
          $(this).treegrid('collapseAll');
        },
        onDrop: areajs.dnd
      ">
    <thead>
      <tr>
        <th data-options="
          field:'code',
          fixed:'true'" width="200">编码</th>
        <th data-options="field:'name',fixed:'true'" width="200">名称</th>
        <th data-options="field:'remark'" width="200">备注</th>
      </tr>
    </thead>
  </table>

  <#-- 表格工具条 -->
  <div class="col-md-12" id="area-toolbar">
    <div class="row row-toolbar">
      <div class="col-md-7">
        <div class="m-b-sm">
          <div class="btn-group">
            <a href="${base}/settings/area/new" class="btn btn-primary"><i class="fa fa-plus-square"></i> 新建顶级区域</a>
            <button id="area-edit" type="button" class="btn btn-primary" onclick="areajs.addSub();"><i class="fa fa-plus"></i> 新建下级区域</button>
            <button id="area-edit" type="button" class="btn btn-primary" onclick="areajs.edit();"><i class="fa fa-edit"></i> 编辑</button>
            <button id="area-delete" type="button" class="btn btn-primary" onclick="areajs.remove();"><i class="fa fa-trash-o"></i> 删除</button>
          </div>
        </div>
      </div>
      <div class="col-md-5">
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  var areajs = {
    addSub: function(){
      var row = $('#area-treegrid').treegrid('getSelected');  
      if (row){
        location.href = "${base}/settings/area/new?parentId=" + row.id;
      } else {
        alert('请选择上级区域！');
      }
    },

    edit: function(){
      var row = $('#area-treegrid').treegrid('getSelected');  
      if (row){
        location.href = "${base}/settings/area/" + row.id + "/edit";
      } else {
        alert('请选择要编辑的数据！');
      }
    },
    
    remove: function(){
      var row = $('#area-treegrid').treegrid('getSelected');  
      if (row){
        if(confirm('确实要删除选中数据吗？')){
          location.href = "${base}/settings/area/" + row.id + "/delete";
        }
      }else{
        alert('请选择要删除的数据！');
      }
    },
    
    dnd: function(targetRow,sourceRow,point){
      var url = "${base}/settings/area/" + sourceRow.id + "/organise?parentId=" + targetRow.id + "&point=" + point;
      $.get(url, function(data){
        // do noting
      });
    }
  }; 
  
  function openArea(row){
  	
  	var url = "${base}/settings/area/tree?id="+row.id;  
        $("#area-treegrid").treegrid("options").url = url;  
        
        return true;      
  }
</script>
</#escape>
