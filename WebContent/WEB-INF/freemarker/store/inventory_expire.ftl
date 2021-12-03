<#escape x as x?html>
<header class="panel-heading">
 	 <a href="${base}/store/book-inventory/expire">查询到期库存</a>
 
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">

	 <div class="col-md-12" id="store_location_type-toolbar">
        <div class="row row-toolbar">
            <div class="col-md-4">
                <div class="m-b-sm">
                    
                </div>
            </div>
            <div class="col-md-8">
                <div style="text-align:right">
                    <form class="form-inline" operator="form">
                        
                	<div class="form-group">
                    <label class="sr-only" for="a">预警类型</label>
                    <select id="type" name="type" class="form-control" style="width:180px;text-align: left;"  onchange="selecttype()">
                        	<option value="2"    <#if type == 2>selected</#if>>保质期预警</option>
                         	<option value="1"    <#if type == 1>selected</#if>>保管到期预警</option>
                            
                    </select>
                	</div>
                 	</form>
            	</div>
        	</div>
    </div>
</div>
</div>
 
  <div class="row">
    <div class="col-md-12">
        <h4>库存明细： </h4>
    </div>
    <div class="col-md-12">
        <table class="table table-striped table-advance table-hover">
            <thead>
                <tr>
                   <th>#</th>
                   <th>客户</th>
                   <th>商品</th>
                   <th>储位编码</th>
                   <th>托盘标签</th>
                   <th>数量</th>
                   <th>重量</th>
                   <th>包装</th>
                   <th>生产日期（天）</th>
                   <th>保质期（天）</th>
                   <th>上架时间</th>
                   <th>预期保管时间（天）</th>      
                </tr>
            </thead>
            <tbody>
             
             
            <#list bookinventorylist as list>
                
                <tr>
                    <td>${list_index + 1}</td>
                    <td>${list.customeName!}</td>
                    <td>${list.productName!} </td>
                    <td>${list.storeLocationCode} </td>
                    <td>${list.storeContainerLabel} </td>
                   	<td>${list.amount!}${list.amountMeasureUnitName!}</td>
                    <td>${list.weight!}${list.weightMeasureUnitName!}</td>
                    <td>${list.packingName!}</td>
                    <td><#if bookInventory.productionDate?exists>${list.productionDate?if_exists?string("yyyy-MM-dd")} </#if></td>
                    <td>${list.quanlityGuaranteePeriod!}</td>
                    <td><#if bookInventory.productionDate?exists>${list.stockInTime?if_exists?string("yyyy-MM-dd")} </#if></td>
                    <td>${list.storeDuration!}</td>
                          
                    
 
                </tr>
            </#list>
            
          
            </tbody>
        </table>
        <div>
        </div>
    </div>
  </div>
 </div>  
 
  <script type="text/javascript">
	 function selecttype(){
	   var type=$('#type').val();
	  
	 	if(type!=""){
	 		location.href="${base}/store/book-inventory/expire?type="+type;
	 	}
	  
	 }
 </script>
  
</#escape>
