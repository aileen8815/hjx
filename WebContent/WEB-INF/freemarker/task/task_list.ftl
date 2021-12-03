<#escape x as x?html>
<header class="panel-heading">
    <h4><a href="${base}/task/index?taskMode=${taskMode?url('utf-8')}">${taskMode}</a></h4>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <#assign mode = taskMode?url('utf-8')>
    <table class="easyui-datagrid" toolbar="#tasks-toolbar" id="tasks-datagrid"
           data-options="url:'${base}/task/list?taskMode=${mode}',
                method:'get',
                rownumbers:true,
                singleSelect:true,
                pagination:true,
                fitColumns:true,
                selectOnCheck:false,
                checkOnSelect:false,
                singleSelect:0,
                collapsible:false">
        <thead>
        <tr>
            <th data-options="field:'id',checkbox:true">状态</th>
            <th data-options="field:'subject',width:150,fixed:false,formatter:subjectFormatter">标题</th>
            <th data-options="field:'taskType',width:100,fixed:true">类型</th>
            <th data-options="field:'taskTime',width:150,fixed:true">通知时间</th>
            <th data-options="field:'taskStatus',width:100,fixed:true">状态</th>
        </tr>
        </thead>
    </table>
    <!-- 表格工具条 -->
    <div class="col-md-12" id="tasks-toolbar">
        <div class="row row-toolbar">
            <div class="col-md-12">
                <div class="m-b-sm">
                    <div class="btn-group">
                        <button id="batchDeleteBtn" type="button" class="btn btn-primary">
                            <i class="fa fa-trash-o"></i> 删除
                        </button>
                        <button id="batchClearBtn" type="button" class="btn btn-primary">
                            <i class="fa fa-check"></i> 标为已读
                        </button>
                        <button id="clearAllBtn" type="button" class="btn btn-primary">
                            <i class="fa fa-check-circle"></i> 全部标为已读
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        var clearAllUrl = "${base}/task/clear-all?taskMode=${taskMode}";
        $('#clearAllBtn').click(function () {
            $.get(clearAllUrl, function (data) {
                $('#tasks-datagrid').datagrid('reload');
                $.gritter.add({
                    text: '<i class="fa fa-bell"></i> ${taskMode}' + data.message,
                    class_name: 'gritter-light'
                });
            })
        });

        $('#batchClearBtn').click(function () {
            var checkedItems = $('#tasks-datagrid').datagrid('getChecked');
            if (checkedItems.length < 1) {
                alert('请选择${taskMode}');
                return;
            }

            var ids = "";
            $.each(checkedItems, function (index, item) {
                ids += item.id + ",";
            });
            var clearUrl = "${base}/task/clear?ids=" + ids;
            $.get(clearUrl, function (data) {
                $('#tasks-datagrid').datagrid('reload');
                $.gritter.add({
                    text: '${taskMode}' + data.message,
                    class_name: 'gritter-light'
                });
            })
        });

        $('#batchDeleteBtn').click(function () {
            var checkedItems = $('#tasks-datagrid').datagrid('getChecked');
            if (checkedItems.length < 1) {
                alert('请选择${taskMode}');
                return;
            }

            var ids = "";
            $.each(checkedItems, function (index, item) {
                ids += item.id + ",";
            });
            var clearUrl = "${base}/task/delete?ids=" + ids;
            $.get(clearUrl, function (data) {
                $('#tasks-datagrid').datagrid('reload');
                $.gritter.add({
                    text: '${taskMode}' + data.message,
                    class_name: 'gritter-light'
                });
            })
        });
    });

    function subjectFormatter(value, row, index) {
        var url = "${base}/task/" + row.id;
        return "<a href='" + url + "' >" + value + "</a>";
    }
</script>
</#escape>
