<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/stock-adjust">库存调整</a> - 
    新建
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="stockAdjust-form" action="${base}/store/stock-adjust/${stockAdjust.id!}" method="post" class="form-horizontal" data-parsley-validate>
    <input type="hidden" name="_method" value="${_method}">
    <input type="hidden" name="createdBy" value="${stockAdjust.createdBy!}">
    <input type="hidden" name="createdTime" value="${stockAdjust.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
    <div class="form-group">
      <label class="col-md-2 control-label">调整类型:</label>
      <div class="col-md-3">
        <select class="form-control" name="stockAdjustType.id" id="stockAdjustTypeId">
          <#list adjustTypes as type>
            <option value="${type.id!}" <#if type.id == stockAdjust.stockAdjustType.id>selected</#if>>${type.name!}</option>
          </#list>
        </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-2 control-label">备注:</label>
      <div class="col-md-6">
        <textarea class="form-control" name="lastName" id="lastName" data-parsley-required="true" rows="4"></textarea>
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-3">
        <button type="submit" class="btn btn-primary">保存</button>
      </div>
    </div>
  </form>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">库存调整</h4>
            </div>
            <div class="modal-body">
            </div>
        </div>
    </div>
</div>
<!-- modal -->
</#escape>
