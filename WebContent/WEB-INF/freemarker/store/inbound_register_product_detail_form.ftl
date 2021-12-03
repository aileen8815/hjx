<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-register/${inboundRegisterId}">入库登记单-多品编辑</a> -
    <#if inboundRegisterItem.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundRegisterItem-form" action="${base}/store/inbound-register/${inboundRegisterItem.id!}/save-product-detail" method="post" class="form-horizontal"
          data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${inboundRegisterItem.createdBy!}">
        <input type="hidden" name="createdTime" value="${inboundRegisterItem.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="inboundRegister.id" id="inboundRegister.id" value="${inboundRegisterId}">
	<div class="row">
     <div class="col-md-11 row">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-3 control-label">商品:</label>

                    <div class="col-md-7">
                        <select id="productId" name="product.id" class="form-control" onchange="selectedproduct()" data-parsley-required="true">
                            <option value="">请选择商品</option>
                            <#list products as product>
                                <option value="${product.id}"
                                        <#if product.id == inboundRegisterItem.product.id>selected</#if>>${product.name}</option>
                            </#list>
                        </select>
                   
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
            	
            	<div class="form-group">
            	<label class="col-md-3 control-label">多品明细:</label>

                    <div class="col-md-7">
                        <input type="text" class="form-control" name="productDetail" id="productDetail" value="${inboundRegisterItem.productDetail}">
                    </div>
                </div>
               
            </div> 
        </div>
        
        <div class="row">
            <div class="col-md-9">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-3">
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </div>
        </div>
      </div>
     </div>
    </form>
</div>
<#--
<link rel="stylesheet" href="${base}/assets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/assets/ztree/js/jquery.ztree.select.js"></script>-->
<#include "/store/add_product_modal.ftl">
</#escape>
