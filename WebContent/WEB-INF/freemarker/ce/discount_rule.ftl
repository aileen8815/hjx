<#escape x as x?html>
<style>
    input.combo-text.validatebox-text {
        padding: 0px 10px;
    }

    .combo .combo-text {
        padding: 0px 10px;
    }
</style>
<header class="panel-heading">
    <a href="${base}/ce/discount-rule">冷库业务计费折扣规则维护</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<div class="well">
    <label>业务类型：</label>
    <select class="easyui-combobox"
            id="business-type"
            style="width:180px; height:30px;"
            data-options="onSelect : businessTypeSelect">
        <option value="null">请选择计费业务</option>
        <option value="inbound">入库</option>
        <option value="outbound">出库</option>
        <option value="ownerchangeforbuyer">买方货权转移</option>
        <option value="ownerchangeforseller">卖方货权转移</option>
        <option value="monthknot">月结仓储费</option>
    </select>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <label>计费项目：</label>
    <select class="easyui-combobox"
            id="fee-item"
            style="width:180px; height:30px;"
            data-options="onSelect : feeItemSelect">
        <option value="0">请选择折扣计费项目</option>
        <#list feeItems as feeItem>
            <option value="${feeItem.id}">${feeItem.name}</option>
        </#list>
    </select>
    <script type="text/javascript">
        var businessType = "null";
        function businessTypeSelect(record) {
            businessType = record.value;
            $('#fee-item').combobox('setValue', '0');
            buildRuleDG('${base}/ce/discount-rule/findRules?feeItemId=0&businessType=');
            clearRule();
        }
        function feeItemSelect(record) {
            var bizType = $('#business-type').combobox('getValue');
            if (bizType != 'null') {
                businessType = record.value;
                buildRuleDG('${base}/ce/discount-rule/findRules?feeItemId=' + record.value + '&businessType=' + bizType);
                clearRule();
            } else {
                alert('请选择业务类型');
            }
        }
        function buildRuleDG(urlName) {
            $("#rule-datagrid").datagrid({
                url: urlName
            });
        }
        function buildRuleItemDG(urlName) {
            $("#rule-item-datagrid").datagrid({
                url: urlName
            });
        }
        function RuleLoadOK(data) {
            $("#rule-datagrid").datagrid('selectRow', 0);
        }
        function typeSelect(record) {
            buildRuleDG('${base}/ce/discount-rule/findRules?feeItemId=' + record.id);
            clearRule();
        }
        function clearRule() {
            buildRuleItemDG('${base}/ce/discount-rule/findRuleItems?ruleId=0');
        }

    </script>
</div>

<h4>规则项</h4>
<table class="easyui-datagrid" id="rule-datagrid"
       toolbar="#rule-toolbar"
       data-options="fitColumns:false,singleSelect:true,rownumbers:true,
					pagination:true,collapsible:true,
					showFooter:true, method : 'get',
					url:'${base}/ce/rule/findRules?typeId=0',
					onSelect : de_rule.selectRow">
    <thead>
    <tr>
        <th data-options="field:'name', width:200">规则名称</th>
        <th data-options="field:'priority', width:80">优先级</th>
        <th data-options="field:'itemRelation', width:80">规则项关系</th>
        <th data-options="field:'factor', width:80">折扣率</th>
    </tr>
    </thead>
</table>

<#-- 表格工具条 -->
<div id="rule-toolbar" style="padding:3px;">
    <a href="javascript:;" class="btn" iconCls="icon-add" plain="true" onclick="de_rule.addEntity()"><i class="fa fa-plus-square"></i>新建</a>
    <a href="javascript:;" class="btn" iconCls="icon-edit" plain="true" onclick="de_rule.editEntity()"><i class="fa fa-edit"></i>编辑</a>
    <a href="javascript:;" class="btn" iconCls="icon-remove" plain="true" onclick="de_rule.destroyEntity()"><i class="fa fa-trash-o"></i>删除</a>
</div>
<div id="rule-dlg" class="easyui-dialog" style="width:300px; height:300px; padding:10px 20px"
     closed="true" buttons="#rule-dlg-buttons">
    <form id="rule-fm" method="post" action="${base}/ce/discount-rule/saveRule">
        <div class='fitem'>
            <label>规则名称</label>
            <input id='name' name='name'>
        </div>
        <br/>
        <div class='fitem'>
            <label>优 先 级</label>
            <input id="priority" name="priority" size="10" panelWidth="70px">
        </div>
        <br/>
        <div class='fitem'>
            <label>子项关系</label>
            <input type='radio' id='itemRelation' name='itemRelation' checked='checked' value='与'/>与
            <input type='radio' id='itemRelation' name='itemRelation' value='或'/>或
        </div>
        <div class='fitem'>
            <label>折扣率</label>
            <input type='text' id='factor' name='factor'/>
        </div>

        <input type="hidden" id="feeItemId" name="feeItemId">
        <input type="hidden" id="businessType" name="businessType">
        <input type="hidden" id="id" name="id">
    </form>
</div>
<div id="rule-dlg-buttons">
    <a href="#" class="btn" iconCls="icon-ok" onclick="de_rule.saveEntity()">保存</a>
    <a href="#" class="btn" iconCls="icon-cancel" onclick="javascript:$('#rule-dlg').dialog('close')">关闭</a>
</div>

<script type="text/javascript">
    var de_rule = {
        addEntity: function () {
            var row = $('#fee-item').combobox('getValue');
            if (row != '0') {
                $('#rule-dlg').dialog('open').dialog('setTitle', '添加新规则');
                $('#rule-fm').form('clear');
                $('#rule-fm #feeItemId').val(row);
            } else {
                alert('请选择计费项目！');
            }
        },

        editEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $('#rule-dlg').dialog('open').dialog('setTitle', '修改规则');
                $('#rule-fm').form('load', '${base}/ce/discount-rule/' + row.id + '/viewRule');
            } else {
                alert('请选择一条规则！');
            }
        },

        saveEntity: function () {
            var feeItemId = $('#fee-item').combobox('getValue');
            var bizType = $('#business-type').combobox('getValue');
            if (feeItemId != '0' && bizType != 'null') {
                $('#rule-fm #feeItemId').val(feeItemId);
                $('#rule-fm #businessType').val(bizType);

                $('#rule-fm').form('submit', {
                    onSubmit: function () {
                        return $(this).form('validate');
                    },
                    success: function (result) {
                        var resultJson = eval('(' + result + ')');
                        if (resultJson.error == 0) {
                            $('#rule-dlg').dialog('close');      // close the dialog
                            $('#rule-datagrid').datagrid('reload');    // reload the user data
                        } else {
                            $.messager.alert('发生如下错误', resultJson.message, 'error');
                        }
                    }
                });
            } else {
                alert('未指定计费项目');
            }
        },

        destroyEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $.messager.confirm('确认', '确实要删除选中的规则吗？确认将会导致其构成的规则条件项被级联删除。', function (r) {
                    if (r) {
                        $.post('${base}/ce/discount-rule/' + row.id + '/deleteRule', function (result) {
                            if (result.error == 0) {
                                $('#rule-datagrid').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.alert('发生如下错误', result.message, 'error');
                            }
                        }, 'json');
                        clearRule();
                    }
                });
            } else {
                alert('请选择一条规则！');
            }
        },

        selectRow: function (rowIndex, rowData) {
            buildRuleItemDG('${base}/ce/discount-rule/findRuleItems?ruleId=' + rowData.id);
        }

    };
</script>
<h4>规则条件项</h4>
<table id='rule-item-datagrid' class='easyui-datagrid'
       toolbar='#rule-item-toolbar'
       data-options="fitColumns:false,singleSelect:true,rownumbers:true,pagination:true,
			method : 'get',
			url:'${base}/ce/rule/findRuleItems?ruleId=0'">

    <thead>
    <tr>
        <th field='optionalItem' width="200px"
            data-options='formatter : function(value, row, index){return row.optionalItem.itemTitle;}'>选项
        </th>
        <th field='relation' width="80px"
            data-options='formatter : function(value, row, index){return de_rule_item.referOperator(value, row);}'>关系
        </th>
        <th field='value' width="100px"
            data-options='formatter : function(value, row, index){return de_rule_item.referValue(value, row, index);}'>条件值
        </th>
    </tr>
    </thead>
</table>

<#-- 规则子项CRUD -->
<div id="rule-item-toolbar" style="padding:3px;">
    <a href="#" class="btn" iconCls="icon-add" plain="true" onclick="de_rule_item.addEntity()"><i class="fa fa-plus-square"></i>新建</a>
    <a href="#" class="btn" iconCls="icon-edit" plain="true" onclick="de_rule_item.editEntity()"><i class="fa fa-edit"></i>编辑</a>
    <a href="#" class="btn" iconCls="icon-remove" plain="true" onclick="de_rule_item.destroyEntity()"><i class="fa fa-trash-o"></i>删除</a>
</div>

<#-- 规则子项编辑窗体 -->
<div id="rule-item-dlg" class="easyui-dialog" style="width:500px; height:300px; padding:10px 20px"
     closed="true" buttons="#rule-item-dlg-buttons">
    <form id="rule-item-fm" method="post" action="${base}/ce/discount-rule/saveRuleItem">
        <div class='fitem'>
            <label>选项</label>
            <input class="easyui-combobox"
                   id='optionalItemID' name='optionalItemID'
                   size="10" style="width:200px">
        </div>
        <br/>

        <div class='fitem'>
            <label>关系</label>
	    		<span id="ruleItemRelationSpan">
	    		</span>
        </div>
        <br/>

        <div class='fitem'>
            <label>值</label>
            <span id="ruleItemSpan"><input id='value' name='value' style='width:200px' required='true'/></span>
        </div>
        <input type="hidden" id="ruleId" name="ruleId">
        <input type="hidden" id="id" name="id">
    </form>
</div>
<div id="rule-item-dlg-buttons">
    <a href="#" class="btn" iconCls="icon-ok" onclick="de_rule_item.saveEntity()">保存</a>
    <a href="#" class="btn" iconCls="icon-cancel" onclick="javascript:$('#rule-item-dlg').dialog('close')">关闭</a>
</div>

<script type="text/javascript">
    var de_rule_item = {
        addEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $('#rule-item-dlg').dialog('open').dialog('setTitle', '新建规则条件项');
                $('#rule-item-fm').form('clear');
                $('#rule-item-fm #optionalItemID').combobox({
                    url: '${base}/ce/discount-rule/findOptionalItems?feeItemId=' + $("#fee-item").combobox('getValue') +
                            '&businessType=' + $("#business-type").combobox('getValue'),
                    method: 'get',
                    valueField: 'id',
                    textField: 'itemTitle',
                    panelHeight: 'auto',
                    onSelect: de_rule_item.optionalItemChange
                });
            } else {
                alert('请选择一条规则！');
            }
        },

        editEntity: function () {
            var row = $('#rule-item-datagrid').datagrid('getSelected');
            if (row) {
                $('#rule-item-dlg').dialog('open').dialog('setTitle', '编辑规则条件项');
                $.ajaxSetup({async: false});
                $('#rule-item-fm #optionalItemID').combobox({
                    url: '${base}/ce/discount-rule/findOptionalItems?feeItemId=' + $("#fee-item").combobox('getValue'),
                    method: 'get',
                    valueField: 'id',
                    textField: 'itemTitle',
                    panelHeight: 'auto',
                    onSelect: de_rule_item.optionalItemChange
                });
                this.fillOptionalItem(row.optionalItem.id, row.value);
                this.fillRelation(row.relation);
                $('#rule-item-fm').form('load', '${base}/ce/discount-rule/' + row.id + '/viewRuleItem');
                $.ajaxSetup({async: true});
            } else {
                alert('请选择一个规则条件项！');
            }
        },

        saveEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $('#rule-item-fm #ruleId').val(row.id);
            }

            $('#rule-item-fm').form('submit', {
                onSubmit: function () {
                    return $(this).form('validate');
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.error == 0) {
                        $('#rule-item-dlg').dialog('close');      // close the dialog
                        $('#rule-item-datagrid').datagrid('reload');    // reload the user data
                    } else {
                        $.messager.alert('发生如下错误', result.message, 'error');
                    }
                }
            });
        },

        destroyEntity: function () {
            var row = $('#rule-item-datagrid').datagrid('getSelected');
            if (row) {
                $.messager.confirm('确认', '确实要删除选中记录吗？', function (r) {
                    if (r) {
                        $.post('${base}/ce/discount-rule/' + row.id + '/deleteRuleItem', function (result) {
                            if (result.error == 0) {
                                $('#rule-item-datagrid').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.alert('发生如下错误', result.message, 'error');
                            }
                        }, 'json');
                    }
                });
            } else {
                alert('请选择一个规则子项！');
            }
        },
        optionalItemChange: function (record) {
            var itemId = $("#optionalItemID").combobox("getValue");
            var valueType = record.valueType;
            var refSource = record.refSource;
            var refName = record.refName;

            if (refSource) {
                if (valueType == "hierarchy") {
                    $("#ruleItemSpan").html("<input class='easyui-combotree' id='value' name='value' style='width:200px' url='${base}" + refSource + "'"
                            + " method='get' >");
                    $("#rule-item-fm #value").combotree();
                    var htmlStr = "<select class='easyui-combobox' id='relation' name='relation' size='2' style='width:200px'>";
                    <#list relationOperators as relation>
                        <#if "${relation}"=="属于">
                            htmlStr = htmlStr + "<option value='${relation}'>属于</option>"
                        </#if>
                        <#if "${relation}"=="不属于">
                            htmlStr = htmlStr + "<option value='${relation}'>不属于</option>"
                        </#if>
                    </#list>
                } else {
                    $("#ruleItemSpan").html("<input class='easyui-combobox' id='value' name='value' style='width:200px' url='${base}" + refSource + "'"
                            + " method='get' valueField='id' textField='" + refName + "'>");
                    $("#rule-item-fm #value").combobox();
                    var htmlStr = "<select class='easyui-combobox' id='relation' name='relation' size='2' style='width:200px'>";
                    <#list relationOperators as relation>
                        <#if "${relation}"=="等于">
                            htmlStr = htmlStr + "<option value='${relation}'>是</option>"
                        </#if>
                        <#if "${relation}"=="不等于">
                            htmlStr = htmlStr + "<option value='${relation}'>不是</option>"
                        </#if>
                    </#list>
                }
                $("#ruleItemRelationSpan").html(htmlStr + "</select>");
                $("#rule-item-fm #relation").combobox();

            } else if (valueType == 'date') {
                $("#ruleItemSpan").html("<input class='easyui-datebox' id='value' name='value' style='width:120px'/>");
                $("#rule-item-fm #value").datebox();
                var htmlStr = "<select class='easyui-combobox' id='relation' name='relation' size='10' style='width:200px'>";
                <#list relationOperators as relation>
                    <#if "${relation}"=="等于">
                        htmlStr = htmlStr + "<option value='${relation}'>在</option>"
                    </#if>
                    <#if "${relation}"=="不等于">
                        htmlStr = htmlStr + "<option value='${relation}'>不在</option>"
                    </#if>
                    <#if "${relation}"=="大于">
                        htmlStr = htmlStr + "<option value='${relation}'>晚于</option>"
                    </#if>
                    <#if "${relation}"=="大于等于">
                        htmlStr = htmlStr + "<option value='${relation}'>不早于</option>"
                    </#if>
                    <#if "${relation}"=="小于">
                        htmlStr = htmlStr + "<option value='${relation}'>早于</option>"
                    </#if>
                    <#if "${relation}"=="小于等于">
                        htmlStr = htmlStr + "<option value='${relation}'>不晚于</option>"
                    </#if>
                </#list>
                $("#ruleItemRelationSpan").html(htmlStr + "</select>");
                $("#rule-item-fm #relation").combobox();
            } else {
                $("#ruleItemSpan").html("<input id='value' name='value' style='width:120px' required='true' />");
                var htmlStr = "<select class='easyui-combobox' id='relation' name='relation' size='10' style='width:200px'>";
                <#list relationOperators as relation>
                    <#if "${relation}"!="属于">
                        htmlStr = htmlStr + "<option value='${relation}'>${relation}</option>"
                    </#if>
                </#list>
                $("#ruleItemRelationSpan").html(htmlStr + "</select>");
                $("#rule-item-fm #relation").combobox();
            }

        },
        referValue: function (value, row, index) {
            var listValue = value;
            if (row.optionalItem.refEntity) {
                $.ajaxSetup({async: false});
                $.getJSON("${base}/ce/discount-rule/getReferObject?className=" + row.optionalItem.refEntity
                        + "&id=" + value + "&refName=" + row.optionalItem.refName, function (data) {
                    listValue = data;
                });
                $.ajaxSetup({async: true});
                return listValue;
            }
            return listValue;
        },
        referOperator: function (value, row) {
            var result = value;
            if (row.optionalItem.refEntity) {
                if (value == '等于') {
                    result = '是';
                }
                if (value == '不等于') {
                    result = '不是';
                }

            }
            if (row.optionalItem.valueType == 'date') {
                if (value == '大于') {
                    result = '晚于';
                }
                if (value == '大于等于') {
                    result = '不早于';
                }
                if (value == '小于') {
                    result = '早于';
                }
                if (value == '小于等于') {
                    result = '不晚于';
                }
            }
            return result;
        },
        fillOptionalItem: function (optionalItemId, value) {
            $("#rule-item-fm #optionalItemID").combobox('select', optionalItemId);
            $("#rule-item-fm #value").val(value);
        },
        fillRelation: function (sn) {
            $("#rule-item-fm #relation").combobox('select', sn);
        }
    };
</script>
</div>
</#escape>