<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/store-area-assignee">库区分派</a> - 
    <#if storeAreaAssignee.id?exists>编辑<#else>新建</#if>
</header>

<div class="panel-body main-content-wrapper site-min-height">
  <form id="storeAreaAssignee-form" action="${base}/store/store-area-assignee/${storeAreaAssignee.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${storeAreaAssignee.createdBy!}" >
    <input type="hidden" name="createdTime" value="${storeAreaAssignee.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
 
    <div class="form-group">
      <label class="col-md-1 control-label">仓管员:</label>
      <div class="col-md-3">
        <select name="operator.id"  class="form-control"   onchange="getstoreoprate(this.value)"  id="operatorId"   data-parsley-required="true">
 			<option value="" >请选择</option>
 			<#list operators as operatorObj>
 				<option value="${operatorObj.id}"    <#if operatorObj.id==operator.id>selected</#if> >${operatorObj.code} ${operatorObj.name}</option>
   			</#list>
    	</select>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-md-1 control-label">仓间:</label>
      <div class="col-md-3">
         <ul id="ztree" class="ztree"    style="background-color:white;overflow:auto;"></ul>
        <input type="hidden" id="storeAreas" name="storeAreas" value=""  >
      </div>
    </div>
   
    <div class="form-group">
      <div class="col-md-offset-1 col-md-3">
        <button type="submit"    onclick="setPerms()"  class="btn btn-primary">保存</button>
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
            url: '${base}/settings/store-area/tree', 
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
			  <#if operator.storeAreaAssignees?has_content>
			    <#list operator.storeAreaAssignees as storeAreaAssignee>
		                        var node = treeObj.getNodesByParam("id", '${storeAreaAssignee.storeArea.id!}', null);
		                        treeObj.expandNode(node[0], true, true,false);//展开选中的节点
		                        treeObj.checkNode(node[0], true, false);
		        </#list>
		       </#if>
                
            }  
        });  
 
 	
      
     
 
    });  
   
 

    <#-- 将选择权限设置到hidden#perms -->
    function setPerms() {
        
          var perms = [];
		  var treeObj = $.fn.zTree.getZTreeObj("ztree");
			var nodes = treeObj.getCheckedNodes(true);
		 
		  for(var i=0;i<nodes.length;i++){
		   		  perms.push(nodes[i].id);
		  }
          $('#storeAreas').val(perms);
    }
    
    function getstoreoprate(value){
    	var treeObj = $.fn.zTree.getZTreeObj("ztree");
    	treeObj.checkAllNodes(false);
    	if(value==''){
    		return;
    	}
		
    	$.ajax({  
            async : true,  
            cache:false,  
            type: 'get', 
            dataType : "text",   
            url: '${base}/store/store-area-assignee/getoprator?id='+value,
            error: function () {//请求失败处理函数  
                alert('请求失败');  
            },  
            success:function(data){ //请求成功后处理函数。    
            var storeAreaAssignees = eval('(' + data + ')');
             
             
 			 
 		 
      		
			   for(var i=0;i<storeAreaAssignees.length;i++){
			   	   var node = treeObj.getNodesByParam("id", storeAreaAssignees[i].storeArea.id, null);
		           treeObj.expandNode(node[0], true, true,false);//展开选中的节点
		           treeObj.checkNode(node[0], true, false);
			   
			   }
		                       
		       
                
            }
        });  
 
    }
</script>
</#escape>

