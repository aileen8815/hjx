<#escape x as x?html>
<header class="panel-heading">
    <h4><a href="${base}/security/operator/log-index">操作日志</a></h4>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<#-- JEasyUI DataGrid 显示数据 -->
    <table class="easyui-datagrid" toolbar="#operation-log-toolbar" id="operation-log-datagrid"
           data-options="url:'${base}/security/operator/list-log',method:'get',rownumbers:true,singleSelect:true,pagination:true,fitColumns:true,collapsible:false">
        <thead>
        <tr>
            <th data-options="field:'operation',width:140,fixed:true">操作</th>
            <th data-options="field:'operatorUsername',fixed:true,width:80">操作人用户名</th>
            <th data-options="field:'operatorName',fixed:true,width:80">操作人名字</th>
            <th data-options="field:'ipAddr',fixed:true,width:100">IP地址</th>
            <th data-options="field:'params',fixed:true,width:130">操作参数</th>
			<th data-options="field:'operateTime',width:90,formatter:formatterDate">操作时间</th>
            <th data-options="field:'logInfo',fixed:true,width:360">操作内容</th>
        </tr>
        </thead>
    </table>

<#-- 表格工具条 -->
    <div class="col-md-12" id="operation-log-toolbar">
        <div class="row row-toolbar">
            <div class="col-md-4">
                <div class="m-b-sm">
                    <div class="btn-group">
                        
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div style="text-align:right">
                    <form class="form-inline" operator="form">
                    <div class="form-group">
              		<label class="sr-only" for="a">操作时间</label>
                	<input type="text" class="form-control Wdate" onClick="WdatePicker()" name="startTime" id="startTime" value=""  placeholder="操作时间">
            		至
              		<label class="sr-only" for="a">操作时间</label>
                	<input type="text" class="form-control Wdate" onClick="WdatePicker()" name="endTime" id="endTime" value=""   placeholder="操作时间">
 
            	</div>
                <div class="form-group">
                    <label class="sr-only" for="a"> 操作员</label>
                    <select id="operationId" name="operationId" class="form-control" style="width:180px;text-align: left;">
                        <option value="">选择操作员</option>
                        <#list operators as operator>
                            <option value="${operator.id}">${operator.name!}</option>
                        </#list>
                    </select>
                </div>
                 

                <div class="btn-group">
                    <button class="btn btn-primary btn-small" onclick="storelocationtypejs.search();" type="button"><i class="fa fa-search"></i> 查询</button>
         
                </div>

                </form>
            </div>
        </div>
    </div>
</div>
</div>

<script type="text/javascript">
    var storelocationtypejs = {
        search: function () {
            $('#operation-log-datagrid').datagrid('load', {
                startTime: $('#startTime').val(),
                endTime: $('#endTime').val(),
                operationId: $('#operationId').val(),
            });
        }
    };

    function formatterDate(value) {
        var date = new Date(value);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
        dateFormat(date, 'yyyy-mm-dd HH:MM:SS');
        return date.format('yyyy-mm-dd HH:MM:ss');
    }

    
</script>

</#escape>
