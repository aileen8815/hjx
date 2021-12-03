<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/settings/store-area">仓间库区</a> -
    <#if storeArea.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="department-form" action="${base}/settings/store-area/${storeArea.id!}" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${storeArea.createdBy!}">
        <input type="hidden" name="createdTime" value="${storeArea.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <#if storeArea.parent?exists>
            <div class="form-group">
                <label class="col-md-2 control-label">上级仓间:</label>

                <div class="col-md-4">
                    <input type="hidden" name="parent.id" value="${storeArea.parent.id!}">
                    <input type="text" class="form-control" value="${storeArea.parent.code!} ${storeArea.parent.name!}" disabled>
                </div>
            </div>
        </#if>
        <div class="form-group">
            <label class="col-md-2 control-label">编码:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="code" id="code" value="${storeArea.code!}" data-parsley-required="true">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">名称:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="name" id="name" value="${storeArea.name!}" data-parsley-required="true">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">建筑面积:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="contructArea" id="contructArea" value="${storeArea.contructArea!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">使用面积:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="useArea" id="useArea" value="${storeArea.useArea!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">温度条件:</label>

            <div class="col-md-4">
                <input type="text" class="form-control" name="temperatureCondition" id="temperatureCondition" value="${storeArea.temperatureCondition!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">仓间状态:</label>

            <div class="col-md-4">
                <select id="storeAreaStatus" name="storeAreaStatus" class="form-control">
                    <#list storeAreaStatusList as storeAreaStatus>
                        <option value="${storeAreaStatus}"
                                <#if storeAreaStatus == storeArea.storeAreaStatus>selected</#if>>${storeAreaStatus}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">租赁状态:</label>

            <div class="col-md-4">
                <select id="storeAreaRentStatus" name="storeAreaRentStatus" class="form-control">
                    <#list storeAreaRentStatusList as storeAreaRentStatus>
                        <option value="${storeAreaRentStatus}"
                                <#if storeAreaRentStatus == storeArea.storeAreaRentStatus>selected</#if>>${storeAreaRentStatus}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">仓间布局:</label>

            <div class="col-md-4">
                <select id="layoutCorriderLine" name="layoutCorriderLine" class="form-control">
                    <option value="3" <#if (storeArea.layoutCorriderLine == 3)>selected</#if>>左三右四</option>
                    <option value="4" <#if (storeArea.layoutCorriderLine == 4)>selected</#if>>左四右三</option>
                    <option value="5" <#if (storeArea.layoutCorriderLine == 5)>selected</#if>>左五右六</option>
                    <option value="6" <#if (storeArea.layoutCorriderLine == 6)>selected</#if>>左六右五</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">仓管员:</label>

            <div class="col-md-4">
                <#list operators as operator>
                    <div class="col-md-3">
                        <label class="checkbox-inline"><input type="checkbox" name="operatorIds" value="${operator.id}"
                            <#list storeArea.storeAreaAssignees as storeAreaAssignee>
                                                              <#if storeAreaAssignee.operator.id==operator.id>checked</#if>
                            </#list>>
                        ${operator.name}
                        </label>
                    </div>
                </#list>
            </div>
        </div>

    <#--<div class="form-group">
      <label class="col-md-2 control-label">可存放商品:</label>
      <div class="col-md-4">
        
        <ul id="products-tree"></ul>
        <input type="hidden" name="storeproducts" id="storeproducts">
       </div>
    </div>-->

        <div class="form-group">
            <div class="col-md-offset-2 col-md-4">
                <button type="submit" class="btn btn-primary" onclick="setProducts()">保存</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
    <#-- 加载商品树数据 -->
        var treeul = $('#products-tree');
        treeul.tree({
            url: '${base}/settings/product/tree',
            checkbox: true,
            <#if storeArea.products?has_content>
            <#-- 绑定已有商品 -->
                onLoadSuccess: function () {
                    <#list storeArea.products as product>
                        var node = treeul.tree('find', '${product.id!}');
                        treeul.tree('check', node.target);
                    </#list>
                },
            </#if>
            method: 'get'
        });
    });

    <#-- 将选择商品设置到hidden#storeproducts -->
    function setProducts() {
        var nodes = $('#products-tree').tree('getChecked');
        var storeproducts = '';
        for (var i = 0; i < nodes.length; i++) {
            if (storeproducts != '') {
                storeproducts += ',';
            }
            storeproducts += nodes[i].id;
        }
        $('#storeproducts').val(storeproducts);
    }
</script>
</#escape>