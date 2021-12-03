<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/book-inventory">货品库存详细</a>
</header>

<div class="panel-body main-content-wrapper site-min-height">

  <div class="row">
    <div class="col-md-2">储位编码：</div>
    <div class="col-md-4">${bookInventory.storeLocation.code!} &nbsp;</div>
    <div class="col-md-2">托盘标签：</div>
    <div class="col-md-4">${bookInventory.storeContainer.label!} &nbsp;</div>
    <div class="col-md-2">客   户：</div>
    <div class="col-md-4">${bookInventory.customer.name!} &nbsp;</div>
    <div class="col-md-2">货   品：</div>
    <div class="col-md-4">${bookInventory.product.name!} &nbsp;</div>
    <div class="col-md-2">重量/单位：</div>
    <div class="col-md-4">${bookInventory.weight!}/${bookInventory.weightMeasureUnit.name!} &nbsp;</div>
    <div class="col-md-2">数量/单位：</div>
    <div class="col-md-4">${bookInventory.amount!}/${bookInventory.amountMeasureUnit.name!} &nbsp;</div>
    <div class="col-md-2">规格：</div>
    <div class="col-md-4">${bookInventory.spec!} &nbsp;</div>
    <div class="col-md-2">包装：</div>
    <div class="col-md-4">${bookInventory.packing.name!} &nbsp;</div>
    <div class="col-md-2">产地：</div>
    <div class="col-md-4">${bookInventory.productionPlace!} &nbsp;</div>
    
    <div class="col-md-2">生产日期：</div>
    <div class="col-md-4"> <#if bookInventory.productionDate?exists>${bookInventory.productionDate?if_exists?string("yyyy-MM-dd")} </#if>&nbsp;</div>
    <div class="col-md-2">保质期：</div>
    <div class="col-md-4">${bookInventory.quanlityGuaranteePeriod!} &nbsp;</div>
    <div class="col-md-2">预期保管时间：</div>
    <div class="col-md-4">${bookInventory.storeDuration!} &nbsp;</div>
    <div class="col-md-2">是否合格：</div>
    <div class="col-md-4">${bookInventory.qualified?string('合格','不合格')} &nbsp;</div>
    <div class="col-md-2">商品状态：</div>
    <div class="col-md-4">${bookInventory.productStatus.name!} &nbsp;</div>
    
    <div class="col-md-2">上架人：</div>
    <div class="col-md-4">${bookInventory.stockInOperator.name!} &nbsp;</div>
    <div class="col-md-2">上架时间：</div>
    <div class="col-md-4"><#if bookInventory.stockInTime?exists>${bookInventory.stockInTime?string("yyyy-MM-dd HH:mm")}</#if> &nbsp;</div>
    <div class="col-md-2">入库单号：</div>
    <div class="col-md-4">${bookInventory.inboundRegisterSerialNo} &nbsp;</div>
    
    <div class="col-md-2">结算时间：</div>
    <div class="col-md-4"><#if bookInventory.settledTime?exists>${bookInventory.settledTime?string("yyyy-MM-dd HH:mm")}</#if> &nbsp;</div>
  </div>
  <br/><br/>
  <div class="row">
    <div class="col-md-12">
    <a href="${base}/store/book-inventory/${bookInventory.id}/edit" class="btn btn-primary">修改库存</a>
    </div>
  </div>

</div>
</#escape>