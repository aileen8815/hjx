<#escape x as x?html>
 

<!-- Modal -->
<div class="modal fade" id="add-custoemr-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">添加商品</h4>
            </div>
            <form id="product-form" action="${base}/settings/product/add-product" method="post" onsubmit="return contactjs.saveContact()" class="form-horizontal" data-parsley-validate>
           <div id="selector-body" class="modal-body">
 	<input type="hidden" class="form-control" name="customer" id="customer" value="${customer.id}">
 	<div class="row">
    <div class="col-md-11">
   <div class="form-group">
   <div class="row">
   <#-- <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">编码:</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="code" id="code" value="${product.code!}" data-parsley-required="true"  >
        </div>
        </div>
      </div>-->
     
        
      <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">名称:</label>
        <div class="col-md-8"> 
		<input type="text" class="form-control" name="name" id="name" value="${product.name!}" data-parsley-required="true">
		</div>
      </div>
    </div>
   </div>
   <div class="row">
    <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">商品类别:</label>
        <div class="col-md-8">
       <select id="productCategoryId" name="productCategory.id"   data-parsley-required="true"  class="form-control">
       
       <#list productCategorys as productCategory>
         <option value="${productCategory.id}" <#if productCategory.id == product.productCategory.id>selected</#if>>${productCategory.code}${productCategory.name}</option>
       </#list>
       </select>
        </div>
    	</div>
        </div>
      
      <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">常用包装:</label>
        <div class="col-md-8">
         <select id="commonPackingId" name="commonPacking.id" class="form-control">
       <#list packings as packing>
     	 <option value="${packing.id}" <#if packing.id == product.commonPacking.id>selected</#if>>${packing.name}</option>
	   </#list>
       </select>
        </div>
      </div>
    </div>
    </div>
    <div class="row">
    <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">产地:</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="productionPlace" id="proproductionPlace" value="${product.productionPlace!}">
        </div>
        </div>
         </div>
      <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">规格:</label>
        <div class="col-md-8">
           <input type="text" class="form-control" name="spec" id="prospec" value="${product.spec!}">
        </div>
      </div>
    </div>
    </div>
    <div class="row">
    <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">参考重量:</label>
        <div class="col-md-8">
           <input type="text" class="form-control" name="weight" id="proweight" value="${product.weight}"
                       data-parsley-type="number">
        </div>
         </div>
       </div>
       
       <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">重量单位:</label>
        <div class="col-md-8">
             <select id="commonUnitId" name="commonUnit.id" class="form-control">
       <#list weightMeasureUnits as measureUnit>
       	  
     	 <option value="${measureUnit.id}" >${measureUnit.name}</option>
	    
	   </#list>
       </select>
        </div>
      </div>
    </div>
    </div>
    <div class="row">
    <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">托盘承载件数:</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="bearingCapacity" id="probearingCapacity" value="${product.bearingCapacity}" data-parsley-required="true"
                       data-parsley-type="integer"  data-parsley-gt="0">
        </div>
       </div>
       </div>
      <div class="col-md-6">
      <div class="form-group">
        <label class="col-md-4 control-label">价格参考区间:</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="priceRange" id="propriceRange" value="${product.priceRange!}"  >
        </div>
      </div>
    </div>
    </div>
    <div class="row">
    <div class="col-md-12">
      <div class="form-group">
        <label class="col-md-2 control-label">备注:</label>
        <div class="col-md-10">
          <input type="text" class="form-control" name="remark" id="remark" value="${product.remark!}">
        </div>
       </div>
    </div>
    </div>
  
    </div>
    </div>
    </div>          
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" onclick="getWeightDefault();">保存</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="contactjs.setlinkmanform()">关闭</button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div><!-- /.modal -->


<script language="javascript">
 var contactjs = {
        saveContact: function () {
            if (!$("#product-form").parsley().validate()) {
                return false;
            }
            
            $.ajax({
                url: "${base}/settings/product/add-product?customer=" + $("#customer").val() ,
                data: {"name": $("#name").val().trim(), "productCategory.id": $("#productCategoryId").val(), "commonPacking.id": $("#commonPackingId").val(),
                    "productionPlace": $("#proproductionPlace").val(), "spec": $("#prospec").val(), "weight": $("#proweight").val(), "commonUnit.id": $("#commonUnitId").val(), "bearingCapacity": $("#probearingCapacity").val(),
                    "priceRange": $("#propriceRange").val(),"remark": $("#remark").val()
                },
                type: "POST",
                success: function (data) {
							var product = eval('(' + data + ')');
							if(product.id!=''&&product.id!=null){
									var option = '<option  value='+product.id+'>'+product.code+product.name+'</option>';
		                			$("#productId").append(option);
		                			contactjs.setlinkmanform();
				                    $('#add-custoemr-modal').modal('hide');
		                    }else{
		                    	alert("商品编码已经被注册过");
		                    }
		                   
                }
            })
            
			
            return false;
        },

        setlinkmanform: function () {
				$("#code").val(''); 
				$("#name").val('');
			    $("#proproductionPlace").val(''); 
				$("#prospec").val('');
				$("#proweight").val(''); 
				$("#probearingCapacity").val('');
				$("#propriceRange").val('');
				$("#remark").val('');
                $("#product-form").parsley().reset();

        }
    }
    
	function getWeightDefault()
	{
		var weight = $("#proweight").val();
		
		if((weight.trim() ==''))
		{
			$("#proweight").val('0');
		}
	}
</script>
</#escape>
