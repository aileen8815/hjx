<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/security/role">角色</a> -
    <#if role.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="role-form" action="${base}/security/role/${role.id!}" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${role.createdBy!}">
        <input type="hidden" name="createdTime" value="${role.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">

        <div class="form-group">
            <label class="col-md-2 control-label">编码:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="code" id="code" value="${role.code!}" data-parsley-required="true">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">名称:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="name" id="name" value="${role.name!}" data-parsley-required="true">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">说明:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="remark" id="remark" value="${role.remark!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">授予权限:</label>

            <div class="col-md-4">
            <#-- 权限树 -->
                <ul id="ztree" class="ztree"    style="background-color:white;overflow:auto;"></ul>
                <input type="hidden" name="perms" id="perms">
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-offset-2 col-md-4">
                <button type="submit" class="btn btn-primary" onclick="setPerms()">保存</button>
            </div>
        </div>
    </form>
</div>
<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript">
   
      
    var zTree;  
    var zTreeNodes;
    $(function(){  
        $.ajax({  
            async : true,  
            cache:false,  
            type: 'get', 
            dataType : "text",   
            url: '${base}/security/permission/tree', 
            error: function () {//请求失败处理函数  
                alert('请求失败');  
            },  
            success:function(data){ //请求成功后处理函数。    
             	var arr = eval(data);  
			 	zTreeNodes = arr;
 				var setting = {
				check: {
					enable: true
				},
				data: {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "parent",
						rootPId: null
					}
				}
			}
      
      		 $.fn.zTree.init($("#ztree"), setting, zTreeNodes);
      		  var treeObj = $.fn.zTree.getZTreeObj("ztree");
			  <#if role.permissions?has_content>
			    <#list role.permissions as permission>
		                        var node = treeObj.getNodesByParam("id", '${permission.id!}', null);
		                        treeObj.expandNode(node[0], true, true,false);//展开选中的节点
		                        treeObj.checkNode(node[0], true, false);
		        </#list>
		       </#if>
                
            }  
        });  
 
 	
      
     
 
    });  
   
   
   
   <#--   $(function () {
  		var treeul = $('#perms-tree');
        treeul.Ztree({
            url: '${base}/security/permission/tree',
            checkbox: true,
            <#if role.permissions?has_content>
  
                onLoadSuccess: function () {
                    <#list role.permissions as permission>
                        var node = treeul.tree('find', '${permission.id!}');
                        treeul.tree('check', node.target);
                    </#list>
                },
            </#if>
            method: 'get'
        });
    }); -->

    <#-- 将选择权限设置到hidden#perms -->
    function setPerms() {
        
          var perms = [];
		  var treeObj = $.fn.zTree.getZTreeObj("ztree");
			var nodes = treeObj.getCheckedNodes(true);
		 
		  for(var i=0;i<nodes.length;i++){
		   		  perms.push(nodes[i].id);
		  }
          $('#perms').val(perms);
    }
    
</script>
</#escape>
