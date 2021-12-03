<#escape x as x?html>
<header class="panel-heading">
    <div class="row">
        <div class="col-md-6">
            <h4>
                <a href="${base}/settings/store-area">仓间库区</a>
            </h4>
        </div>
        <div class="col-md-6">
            <div class="btn-group pull-right">
                <a class="btn btn-primary" href="${base}/settings/store-area/${storeArea.id}/edit">编辑库区</a>
            </div>
        </div>
    </div>
</header>

<div class="panel-body main-content-wrapper site-min-height">
    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-2">编号：</div>
                <div class="col-md-4">${storeArea.code!} &nbsp;</div>
                <div class="col-md-2">名称：</div>
                <div class="col-md-4">${storeArea.name!} &nbsp;</div>
                
                <div class="col-md-2">建筑面积：</div>
                <div class="col-md-4">${storeArea.contructArea!} &nbsp;</div>
                <div class="col-md-2">使用面积：</div>
                <div class="col-md-4">${storeArea.useArea!} &nbsp;</div>
               
                <div class="col-md-2">温度条件：</div>
                <div class="col-md-4">${storeArea.temperatureCondition!} &nbsp;</div>
                <div class="col-md-2">使用状态：</div>
                <div class="col-md-4">${storeArea.storeAreaStatus!} &nbsp;</div>
            </div>
            <br/>
            <div class="row">
                <div class="col-md-12">
                    <h4>可存放商品：   <a href="${base}/settings/store-area/${storeArea.id}/add-products"><i class="fa fa-plus-square"></i> 添加商品</a></h4>
                </div>
                <div class="col-md-12">
                    <table class="table table-striped table-advance table-hover">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>商品编码</th>
                            <th>商品</th>
                            <th>参考重量</th>
                            <th>托盘承载件数</th>
                            <th>参考价格区间</th>
                           	<th>常用包装</th>
                            <th>产地</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list  storeArea.products as product>
                            <tr>
                                <td>${item_index + 1}</td>
                                <td>${product.code!} </td>
                                <td>${product.name!}</td>
                                <td>${product.weight!}</td>
                                <td>${product.bearingCapacity!}</td>
                                <td>${product.priceRange!}</td>
                                <td>${product.commonUnit.name!}</td>
                                <td>${product.commonPacking.name!}</td>
                                <td>${product.productionPlace!}</td>
								<td>
                                    <a href="${base}/settings/store-area/del-product?productId=${product.id}&&storeAreaId=${storeArea.id}">删除</a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</#escape>

