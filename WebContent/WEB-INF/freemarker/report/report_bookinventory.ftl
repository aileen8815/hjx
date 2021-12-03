<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/report/report-bookinventory-index?type=${type}"><#if type==1>库存汇总报表<#else>库存报表</#if></a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <div>
          <form id="bookinventory-form"  target="reportFrame"  action="${reportUrl}/pentaho/api/repos/%3Apublic%3A${prptName}/report"  class="form-horizontal" data-parsley-validate>
        <input type ="hidden" id="output-target" name="output-target" value="pageable/pdf">
		<input type ="hidden" id="userid" name="userid" value="${user}">
      	<input type ="hidden" id="password" name="password" value="${password}">
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="col-md-4 control-label">库区:</label>
                    <div class="col-md-8">
                      	    <select id="storeAreaId" name="storeAreas" class="form-control">
                                     <option value="-1">全部库区</option>
                                    <#list storeAreaList as storeArea>
                                        <#if !storeArea.parent?exists>
                                            <option value="${storeArea.id}" >${storeArea.code} ${storeArea.name}</option>
                                            <#list storeArea.children as area>
                                                <option value="${area.id}" >&nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                            </#list>
                                        </#if>
                                    </#list>
                             </select>
                             <input type ="hidden" id="storeAreaText" name="storeAreaText" value="全部库区">
                    </div>
                </div>
            </div>

         <div class="col-md-4">
                <div class="form-group">
                    <label class="col-md-4 control-label">客户:</label>

                    <div class="col-md-8">
                        <select id="customerId" name="customers" class="form-control"  onchange="selectedCustomer(this.value)" >
                            <option value="-1">全部客户</option>
                            <#list customerList as customer>
                                <option value="${customer.id}" >${customer.text!}</option>
                            </#list>
                        </select>
                         <input type ="hidden" id="customerText" name="customerText" value="全部客户">
                    </div>
                </div>
            </div>
 			
 			
 			<div class="col-md-4">
                <div class="form-group">
                    <label class="col-md-4 control-label">商品:</label>

                    <div class="col-md-8">
                        
                         <select id="productId" name="products" class="form-control"  >
                 		<option value="-1">全部商品</option>
						<#list productList as product>
                        <option value="${product.id}"  >${product.name}</option>
                    	</#list>
                		</select>
                		 <input type ="hidden" id="productText" name="productText" value="全部商品">
                    </div>
                </div>
            </div>  
            </div>
            <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="col-md-4 control-label">时间:</label>

                    <div class="col-md-8">
                               <input type="text" class="form-control Wdate"   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"   name="startDate"
                               id="startDate"  value=""
                               placeholder="报表时间" >
                    </div>
                </div>
            </div>
 
            <div class="col-md-8">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-10">
                        <button type="submit" class="btn btn-primary"   onClick="setReportTypeQuery()" >查询</button> &nbsp;&nbsp;
                        <button type="submit" class="btn btn-primary"  onclick="setExpReportType()">导出Excel</button> 
                    </div>
                </div>
            </div>
        </div>
    </form>
    </div>
    <iframe id="reportFrame" name="reportFrame"   width="100%" height="600" frameborder="0"></iframe>
</div>
<script src="${base}/assets/report/report.js"></script>
<script type="text/javascript">
  
	$(function () {
		$("#startDate").val(getCurrentTime());
	    $("#storeAreaId").bind("change",function(){
	    	var checkText=$("#storeAreaId").find("option:selected").text();
			$("#storeAreaText").val(checkText);					
		});
	    $("#customerId").bind("change",function(){
	    	var checkText=$("#customerId").find("option:selected").text();
			$("#customerText").val(checkText);					
		});
	    $("#productId").bind("change",function(){
	    	var checkText=$("#productId").find("option:selected").text();
			$("#productText").val(checkText);					
		});
	});
	
	function getCurrentTime() {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) ;
    }
 	
 	 
	function selectedCustomer(value) {
        $("#productId").empty();
        $("#productId").prepend('<option  value=-1 >全部商品</option>');
        $("#productText").val("全部商品");
        $('#productId').select2("data", {"id":-1,"text":"全部商品"} );
        $.ajax({
            url: "${base}/settings/customer/customer-products?id=" +value,
            data: '',
            type: "get",
            success: function (data) {
                 var ar = eval('(' + data + ')');
           		 if(ar.length>0){
           		   	for (var i = 0; i < ar.length; i++) {
                	var option = '<option  value='+ar[i].id+'>'+ar[i].code+ar[i].name+'</option>';
                	$("#productId").append(option);
            		}
           		}
         	}
        })	
    }
	
	function setReportTypeQuery(){
		$("#output-target").val('pageable/pdf');
	}
	function setExpReportType(){
		$("#output-target").val('table/excel;page-mode=flow');
	}
</script>
</#escape>
