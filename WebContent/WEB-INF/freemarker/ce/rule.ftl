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
    <a href="${base}/ce/rule">冷库业务计费规则维护</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<div class="row well">
    <label>业务类型：</label>
    <select class="easyui-combobox"
            id="business-type"
            style="width:160px; height: 30px;"
            data-options="onSelect : businessTypeSelect"
            >
        <option value="null">请选择计费业务</option>
        <option value="inbound">入库</option>
        <option value="outbound">出库</option>
        <option value="ownerchangeforbuyer">买方货权转移</option>
        <option value="ownerchangeforseller">卖方货权转移</option>
        <option value="monthknot">月结仓储费</option>
    </select>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <label>规则类型：</label>
    <input class="easyui-combo"
           id="rule-type"
           style="width:160px; height: 30px;"
           data-options="
                        url:'${base}/ce/rule/findRuleTypes?businessType=null',
                        method : 'get',
                        valueField:'id',
                        textField:'typeName',
                        panelHeight:'auto',
                        onSelect : typeSelect
                        ">
</div>

<script type="text/javascript">
    var businessType = "null";
    function businessTypeSelect(record) {
        businessType = record.value;
        buildRuleType('${base}/ce/rule/findRuleTypes?businessType=' + record.value);
        buildRuleDG('${base}/ce/rule/findRules?typeId=0');
        clearRule();
    }
    function buildRuleType(urlName) {
        $("#rule-type").combobox({
            url: urlName
        });
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
    function buildCalculationItemDG(urlName) {
        $("#ce-calculation-item-datagrid").datagrid({
            url: urlName
        });
    }
    function buildConditionItemDG(urlName) {
        $("#ce-condition-item-datagrid").datagrid({
            url: urlName
        });
    }

    function RuleLoadOK(data) {
        $("#rule-datagrid").datagrid('selectRow', 0);
    }

    function typeSelect(record) {
        buildRuleDG('${base}/ce/rule/findRules?typeId=' + record.id);
        clearRule();
    }

    function clearRule() {
        buildRuleItemDG('${base}/ce/rule/findRuleItems?ruleId=0');
        clearCalc();
    }

    function clearCalc() {
        buildCalculationItemDG('${base}/ce/rule/findCalculationItems?ruleId=0');
        buildConditionItemDG('${base}/ce/rule/findConditionItems?calculationItemId=0');
    }
</script>

<div class="row">
<div class="col-md-6">
<div class="well">
<h4>规则项</h4>
<table class="easyui-datagrid" id="rule-datagrid"
       toolbar="#rule-toolbar"
       data-options="fitColumns:false,singleSelect:true,rownumbers:true,
                            pagination:true,collapsible:true,
                            showFooter:true, method : 'get',
                            url:'${base}/ce/rule/findRules?typeId=0',
                            onSelect : ce_rule.selectRow">
    <thead>
    <tr>
        <th data-options="field:'name', width:200">规则名称</th>
        <th data-options="field:'priority', width:80">优先级</th>
        <th data-options="field:'itemRelation', width:80">规则项关系</th>
    </tr>
    </thead>
</table>
<#-- 表格工具条 -->
<div id="rule-toolbar" style="padding:3px;">
    <a href="#" class="btn" iconCls="icon-add" plain="true" onclick="ce_rule.addEntity()"><i class="fa fa-plus-square"></i>新建</a>
    <a href="#" class="btn" iconCls="icon-edit" plain="true" onclick="ce_rule.editEntity()"><i class="fa fa-edit"></i>编辑</a>
    <a href="#" class="btn" iconCls="icon-remove" plain="true" onclick="ce_rule.destroyEntity()"><i class="fa fa-trash-o"></i>删除</a>
</div>
<div id="rule-dlg" class="easyui-dialog" style="width:300px; height:300px; padding:10px 20px"
     closed="true" buttons="#rule-dlg-buttons">
    <form id="rule-fm" method="post" action="${base}/ce/rule/saveRule">
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
        <input type="hidden" id="ruleTypeId" name="ruleTypeId">
        <input type="hidden" id="id" name="id">
    </form>
</div>
<div id="rule-dlg-buttons">
    <a href="#" class="btn" iconCls="icon-ok" onclick="ce_rule.saveEntity()">保存</a>
    <a href="#" class="btn" iconCls="icon-cancel" onclick="javascript:$('#rule-dlg').dialog('close')">关闭</a>
</div>
<script type="text/javascript">
    var ce_rule = {
        addEntity: function () {
            var row = $('#rule-type').combobox('getValue');
            if (row) {
                $('#rule-dlg').dialog('open').dialog('setTitle', '添加新规则');
                $('#rule-fm').form('clear');
                $('#rule-fm #ruleTypeId').val(row.id);
            } else {
                alert('请选择规则类型！');
            }
        },

        editEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $('#rule-dlg').dialog('open').dialog('setTitle', '修改规则');
                $('#rule-fm').form('load', '${base}/ce/rule/' + row.id + '/viewRule');
            } else {
                alert('请选择一条规则！');
            }
        },

        saveEntity: function () {
            var ruleTypeId = $('#rule-type').combobox('getValue');
            if (ruleTypeId) {
                $('#rule-fm #ruleTypeId').val(ruleTypeId);
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
                alert('未指定规则类型');
            }
        },

        destroyEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $.messager.confirm('确认', '确实要删除选中的规则吗？确认将会导致其构成的规则条件项、计算项等数据被级联删除。', function (r) {
                    if (r) {
                        $.post('${base}/ce/rule/' + row.id + '/deleteRule', function (result) {
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
            buildRuleItemDG('${base}/ce/rule/findRuleItems?ruleId=' + rowData.id);
            buildCalculationItemDG('${base}/ce/rule/findCalculationItems?ruleId=' + rowData.id);
            buildConditionItemDG('${base}/ce/rule/findConditionItems?calculationItemId=0');
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
            data-options='formatter : function(value, row, index){return ce_rule_item.referOperator(value, row);}'>关系
        </th>
        <th field='value' width="100px"
            data-options='formatter : function(value, row, index){return ce_rule_item.referValue(value, row, index);}'>条件值
        </th>
    </tr>
    </thead>
</table>

<#-- 规则子项CRUD -->
<div id="rule-item-toolbar" style="padding:3px;">
    <a href="#" class="btn" iconCls="icon-add" plain="true" onclick="ce_rule_item.addEntity()"><i class="fa fa-plus-square"></i>新建</a>
    <a href="#" class="btn" iconCls="icon-edit" plain="true" onclick="ce_rule_item.editEntity()"><i class="fa fa-edit"></i>编辑</a>
    <a href="#" class="btn" iconCls="icon-remove" plain="true" onclick="ce_rule_item.destroyEntity()"><i class="fa fa-trash-o"></i>删除</a>
</div>

<#-- 规则子项编辑窗体 -->
<div id="rule-item-dlg" class="easyui-dialog" style="width:500px; height:300px; padding:10px 20px"
     closed="true" buttons="#rule-item-dlg-buttons">
    <form id="rule-item-fm" method="post" action="${base}/ce/rule/saveRuleItem">
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
            <label>&nbsp;值&nbsp;&nbsp;</label>
            <span id="ruleItemSpan"><input id='value' name='value' style='width:200px' data-parsley-required="true"/></span>
        </div>
        <input type="hidden" id="ruleId" name="ruleId">
        <input type="hidden" id="id" name="id">
    </form>
</div>
<div id="rule-item-dlg-buttons">
    <a href="#" class="btn" iconCls="icon-ok" onclick="ce_rule_item.saveEntity()">保存</a>
    <a href="#" class="btn" iconCls="icon-cancel" onclick="javascript:$('#rule-item-dlg').dialog('close')">关闭</a>
</div>

<script type="text/javascript">
var ce_rule_item = {
    addEntity: function () {
        var row = $('#rule-datagrid').datagrid('getSelected');
        if (row) {
            $('#rule-item-dlg').dialog('open').dialog('setTitle', '新建规则条件项');
            $('#rule-item-fm').form('clear');
            $('#rule-item-fm #optionalItemID').combobox({
                url: '${base}/ce/rule/findOptionalItems?itemType=rule&businessType=' + $("#business-type").combobox('getValue'),
                method: 'get',
                valueField: 'id',
                textField: 'itemTitle',
                panelHeight: 'auto',
                onSelect: ce_rule_item.optionalItemChange
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
                url: '${base}/ce/rule/findOptionalItems?itemType=rule&businessType=' + $("#business-type").combobox('getValue'),
                method: 'get',
                valueField: 'id',
                textField: 'itemTitle',
                panelHeight: 'auto',
                onSelect: ce_rule_item.optionalItemChange
            });
            this.fillOptionalItem(row.optionalItem.id, row.value);
            this.fillRelation(row.relation);
            $('#rule-item-fm').form('load', '${base}/ce/rule/' + row.id + '/viewRuleItem');
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
                    $.post('${base}/ce/rule/' + row.id + '/deleteRuleItem', function (result) {
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
            $.getJSON("${base}/ce/rule/getReferObject?className=" + row.optionalItem.refEntity
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
</div>

<div class="col-md-6">
<div class="well">
<h4>计算项</h4>
<table id='ce-calculation-item-datagrid' class='easyui-datagrid'
       toolbar='#ce-calculation-item-toolbar'
       data-options="fitColumns:false,singleSelect:true,rownumbers:true,pagination:true,
                        method : 'get',
                        url:'${base}/ce/rule/findCalculationItems?ruleId=0',
                        onSelect : ce_calculation_item.selectRow">

    <thead>
    <tr>
        <th field='feeItem' data-options='formatter : function(value, row, index){return row.feeItem.name;}'>收费项目</th>
        <th field='optionalItem' data-options='formatter : function(value, row, index){if(row.optionalItem) {return row.optionalItem.itemTitle;}}'>计费依据</th>
        <th field='factor'>计算因子</th>
      <!--  <th field='addedValue'>附加额</th> 
        <th field='decrease'>调整量</th>
      -->
        <th field='conditionRelation'>条件项关系</th>
    </tr>
    </thead>
</table>

<#-- 计算子项CRUD -->
<div id="ce-calculation-item-toolbar" style="padding:3px;">
    <a href="#" class="btn" iconCls="icon-add" plain="true" onclick="ce_calculation_item.addEntity()"><i class="fa fa-plus-square"></i>新建</a>
    <a href="#" class="btn" iconCls="icon-edit" plain="true" onclick="ce_calculation_item.editEntity()"><i class="fa fa-edit"></i>编辑</a>
    <a href="#" class="btn" iconCls="icon-remove" plain="true" onclick="ce_calculation_item.destroyEntity()"><i class="fa fa-trash-o"></i>删除</a>
</div>

<#-- 计算子项编辑窗体 -->
<div id="ce-calculation-item-dlg" class="easyui-dialog" style="width:500px; height:300px; padding:10px 20px"
     closed="true" buttons="#ce-calculation-item-dlg-buttons">
    <form id="ce-calculation-item-fm" method="post" action="${base}/ce/rule/saveCalculationItem"  data-parsley-validate>
        <div class='fitem'>
            <label>收费项目</label>
            <select class="easyui-combobox" id='feeItemID' name='feeItemID'
                    size="10" style="width:200px"  data-parsley-required="true" >
                <#list feeItems as feeItem>
                    <option value="${feeItem.id}">${feeItem.name}</option>
                </#list>
            </select>
        </div>
        <div class='fitem'>
            <label>计费依据</label>
            <select class="easyui-combobox" id='optionalItemID' name='optionalItemID'
                    size="10" style="width:200px"  data-parsley-required="true" >
            </select>
        </div>
        <br/>

        <div class='fitem'>
            <label>条件项关系</label>
            <input type='radio' id='conditionRelation' name='conditionRelation' checked='checked' value='与'/>与
            <input type='radio' id='conditionRelation' name='conditionRelation' value='或'/>或
        </div>
        <br/>

        <div class='fitem'>
            <label>计算因子</label>
            <input id='factor' name='factor' style='width:120px'  data-parsley-required="true"/>
        </div>
       <!-- <div class='fitem'>
            <label>附&nbsp;加&nbsp;额&nbsp;&nbsp;</label>
            <input id='addedValue' name='addedValue' style='width:120px'/>
        
        </div>
        <div class='fitem'>
            <label>调&nbsp;整&nbsp;量&nbsp;&nbsp;</label>
            <input id='decrease' name='decrease' style='width:120px'/>
        </div>
        -->
        <input type="hidden" id="ruleId" name="ruleId">
        <input type="hidden" name="id">
    </form>
</div>
<div id="ce-calculation-item-dlg-buttons">
    <a href="#" class="btn" iconCls="icon-ok" onclick="ce_calculation_item.saveEntity()">保存</a>
    <a href="#" class="btn" iconCls="icon-cancel" onclick="javascript:$('#ce-calculation-item-dlg').dialog('close')">关闭</a>
</div>

<script type="text/javascript">
    var ce_calculation_item = {
        addEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $('#ce-calculation-item-dlg').dialog('open').dialog('setTitle', '新建计算子项');
                $('#ce-calculation-item-fm').form('clear');
                $('#ce-calculation-item-fm #optionalItemID').combobox({
                    url: '${base}/ce/rule/findOptionalItems?itemType=calculation&businessType=' + $("#business-type").combobox('getValue'),
                    method: 'get',
                    valueField: 'id',
                    textField: 'itemTitle',
                    panelHeight: 'auto',
                    onSelect: ce_rule_item.optionalItemChange
                });
            } else {
                alert('请选择一条规则！');
            }
        },

        editEntity: function () {
            var row = $('#ce-calculation-item-datagrid').datagrid('getSelected');
            if (row) {
                $('#ce-calculation-item-dlg').dialog('open').dialog('setTitle', '编辑计算子项');
                $('#ce-calculation-item-fm').form('clear');
                $.ajaxSetup({async: false});
                $('#ce-calculation-item-fm #optionalItemID').combobox({
                    url: '${base}/ce/rule/findOptionalItems?itemType=calculation&businessType=' + $("#business-type").combobox('getValue'),
                    method: 'get',
                    valueField: 'id',
                    textField: 'itemTitle',
                    panelHeight: 'auto',
                    onSelect: ce_rule_item.optionalItemChange
                });
                if (row.optionalItem) {
                    this.fillOptionalItem(row.optionalItem.id);
                }
                this.fillFeeItem(row.feeItem.id);
                $('#ce-calculation-item-fm').form('load', '${base}/ce/rule/' + row.id + '/viewCalculationItem?id=' + row.id);
                $.ajaxSetup({async: true});
            } else {
                alert('请选择一个规则子项！');
            }
        },

        saveEntity: function () {
            var row = $('#rule-datagrid').datagrid('getSelected');
            if (row) {
                $('#ce-calculation-item-fm #ruleId').val(row.id);
            }

            $('#ce-calculation-item-fm').form('submit', {
                onSubmit: function () {
                	 var factor= $("#ce-calculation-item-fm #factor").val();  
                	 var feeItemID= $("#ce-calculation-item-fm #feeItemID").combobox("getValue"); 
                	 var optionalItemID= $("#ce-calculation-item-fm #optionalItemID").combobox("getValue");               	 
           			 if(factor!=''&&feeItemID!=''&&optionalItemID!=''){
           			 	 return true;
           			 }else{
           			 	alert("数据填写不完整");
           			 	return false;
           			 	
           			 }  
                    
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.error == 0) {
                        $('#ce-calculation-item-dlg').dialog('close');      // close the dialog
                        $('#ce-calculation-item-datagrid').datagrid('reload');    // reload the user data
                    } else {
                        $.messager.alert('发生如下错误', result.message, 'error');
                    }
                }
            });
        },

        destroyEntity: function () {
            var row = $('#ce-calculation-item-datagrid').datagrid('getSelected');
            if (row) {
                $.messager.confirm('确认', '确实要删除选中的计算项吗？确认将导致其下的计算条件项也被 级联删除。', function (r) {
                    if (r) {
                        $.post('${base}/ce/rule/' + row.id + '/deleteCalculationItem', function (result) {
                            if (result.error == 0) {
                                $('#ce-calculation-item-datagrid').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.alert('发生如下错误', result.message, 'error');
                            }
                        }, 'json');
                        clearCalc();
                    }
                });
            } else {
                alert('请选择一个规则子项！');
            }
        },
        fillOptionalItem: function (optionalItemId) {
            $("#ce-calculation-item-fm #optionalItemID").combobox('select', optionalItemId);
        },
        fillFeeItem: function (feeItemId) {
            $("#ce-calculation-item-fm #feeItemID").combobox('select', feeItemId);
        },
        selectRow: function (rowIndex, rowData) {
            buildConditionItemDG('${base}/ce/rule/findConditionItems?calculationItemId=' + rowData.id);
        }
    };
</script>

<h4>计算条件项</h4>
<table id='ce-condition-item-datagrid' class='easyui-datagrid'
       toolbar='#ce-condition-item-toolbar'
       data-options="fitColumns:false,singleSelect:true,rownumbers:true,pagination:true,
                    method : 'get',
                    url:'${base}/ce/rule/findConditionItems?calculationItemId=0'">

    <thead>
    <tr>
        <th field='optionalItem' data-options='formatter : function(value, row, index){return row.optionalItem.itemTitle;}'>选项</th>
        <th field='relation' data-options='formatter : function(value, row, index){return ce_rule_item.referOperator(value, row);}'>关系</th>
        <th field='value' data-options='formatter : function(value, row, index){return ce_rule_item.referValue(value, row, index);}'>条件值</th>
    </tr>
    </thead>
</table>

<#-- 条件子项CRUD -->
<div id="ce-condition-item-toolbar" style="padding:3px;">
    <a href="#" class="btn" iconCls="icon-add" plain="true" onclick="ce_condition_item.addEntity()"><i class="fa fa-plus-square"></i>新建</a>
    <a href="#" class="btn" iconCls="icon-edit" plain="true" onclick="ce_condition_item.editEntity()"><i class="fa fa-edit"></i>编辑</a>
    <a href="#" class="btn" iconCls="icon-remove" plain="true" onclick="ce_condition_item.destroyEntity()"><i class="fa fa-trash-o"></i>删除</a>
</div>

<#-- 条件子项编辑窗体 -->
<div id="ce-condition-item-dlg" class="easyui-dialog" style="width:500px; height:300px; padding:10px 20px"
     closed="true" buttons="#ce-condition-item-dlg-buttons">
    <form id="ce-condition-item-fm" method="post" action="${base}/ce/rule/saveConditionItem">
        <div class='fitem'>
            <label>选项</label>
            <select class="easyui-combobox" id='optionalItemID' name='optionalItemID'
                    size="10" style="width:200px">
            </select>
        </div>
        <br/>

        <div class='fitem'>
            <label>关系</label>
                    <span id='condItemRelationSpan'>
                    </span>
        </div>
        <br/>

        <div class='fitem'>
            <label>&nbsp;值&nbsp;&nbsp;&nbsp;</label>
            <span id="ConditionItemSpan"><input id='value' name='value' style='width:200px'  /></span>
        </div>
        <input type="hidden" id="calculationItemID" name="calculationItemID">
        <input type="hidden" id="id" name="id">
    </form>
</div>
<div id="ce-condition-item-dlg-buttons">
    <a href="#" class="btn" iconCls="icon-ok" onclick="ce_condition_item.saveEntity()">保存</a>
    <a href="#" class="btn" iconCls="icon-cancel" onclick="javascript:$('#ce-condition-item-dlg').dialog('close')">关闭</a>
</div>
</div>
</div>
</div>
</div>

<script type="text/javascript">
    var ce_condition_item = {
        addEntity: function () {
            var row = $('#ce-calculation-item-datagrid').datagrid('getSelected');
            if (row) {
                $('#ce-condition-item-dlg').dialog('open').dialog('setTitle', '新建计算条件项');
                $('#ce-condition-item-fm').form('clear');
                $('#ce-condition-item-fm #optionalItemID').combobox({
                    url: '${base}/ce/rule/findOptionalItems?itemType=condition&businessType=' + $("#business-type").combobox('getValue'),
                    method: 'get',
                    valueField: 'id',
                    textField: 'itemTitle',
                    panelHeight: 'auto',
                    onSelect: ce_condition_item.optionalItemChange
                });
            } else {
                alert('请选择一条计算项！');
            }
        },

        editEntity: function () {
            var row = $('#ce-condition-item-datagrid').datagrid('getSelected');
            if (row) {
                $('#ce-condition-item-dlg').dialog('open').dialog('setTitle', '编辑计算条件项');
                $.ajaxSetup({async: false});
                $('#ce-condition-item-fm #optionalItemID').combobox({
                    url: '${base}/ce/rule/findOptionalItems?itemType=condition&businessType=' + $("#business-type").combobox('getValue'),
                    method: 'get',
                    valueField: 'id',
                    textField: 'itemTitle',
                    panelHeight: 'auto',
                    onSelect: ce_condition_item.optionalItemChange
                });
                this.fillOptionalItem(row.optionalItem.id, row.value);
                this.fillRelation(row.relation);
                $('#ce-condition-item-fm').form('load', '${base}/ce/rule/' + row.id + '/viewConditionItem');
                $.ajaxSetup({async: true});
            } else {
                alert('请选择一个计算条件项！');
            }
        },

        saveEntity: function () {
            var row = $('#ce-calculation-item-datagrid').datagrid('getSelected');
            if (row) {
                $('#ce-condition-item-fm #calculationItemID').val(row.id);
            }

            $('#ce-condition-item-fm').form('submit', {
                onSubmit: function () {
                    return $(this).form('validate');
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.error == 0) {
                        $('#ce-condition-item-dlg').dialog('close');      // close the dialog
                        $('#ce-condition-item-datagrid').datagrid('reload');    // reload the user data
                    } else {
                        $.messager.alert('发生如下错误', result.message, 'error');
                    }
                }
            });
        },

        destroyEntity: function () {
            var row = $('#ce-condition-item-datagrid').datagrid('getSelected');
            if (row) {
                $.messager.confirm('确认', '确实要删除选中记录吗？', function (r) {
                    if (r) {
                        $.post('${base}/ce/rule/' + row.id + '/deleteConditionItem', function (result) {
                            if (result.error == 0) {
                                $('#ce-condition-item-datagrid').datagrid('reload');    // reload the user data
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
            var itemId = $("#ce-condition-item-fm #optionalItemID").combobox("getValue");
            var valueType = record.valueType;
            var refSource = record.refSource;
            var refName = record.refName;
            if (refSource) {
                if (valueType == "hierarchy") {
                    $("#ConditionItemSpan").html("<input class='easyui-combotree' id='value' name='value' style='width:200px' url='${base}" + refSource + "'"
                            + " method='get'>");
                    $("#ce-condition-item-fm #value").combotree();
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
                    $("#ConditionItemSpan").html("<input class='easyui-combobox' id='value' name='value' style='width:200px' url='${base}" + refSource + "'"
                            + " method='get' valueField='id' textField='" + refName + "'>");
                    $("#ce-condition-item-fm #value").combobox();
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
                $("#condItemRelationSpan").html(htmlStr + "</select>");
                $("#ce-condition-item-fm #relation").combobox();
            } else if (valueType == 'date') {
                $("#ConditionItemSpan").html("<input class='easyui-datebox' id='value' name='value' style='width:120px'/>");
                $("#ce-condition-item-fm #value").datebox();
                var htmlStr = "<select class='easyui-combobox' id='relation' name='relation' size='6' style='width:200px'>";
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
                $("#condItemRelationSpan").html(htmlStr + "</select>");
                $("#ce-condition-item-fm #relation").combobox();
            } else {
                $("#ConditionItemSpan").html("<input id='value' name='value' style='width:120px' required='true' />");
                var htmlStr = "<select class='easyui-combobox' id='relation' name='relation' size='6' style='width:200px'>";
                <#list relationOperators as relation>
                    <#if "${relation}"!="属于">
                        htmlStr = htmlStr + "<option value='${relation}'>${relation}</option>"
                    </#if>
                </#list>
                $("#condItemRelationSpan").html(htmlStr + "</select>");
                $("#ce-condition-item-fm #relation").combobox();
            }

        },
        fillOptionalItem: function (optionalItemId, value) {
            $("#ce-condition-item-fm #optionalItemID").combobox('select', optionalItemId);
            $("#ce-condition-item-fm #value").val(value);
        },
        fillRelation: function (sn) {
            $("#ce-condition-item-fm #relation").combobox('select', sn);
        }
    };
</script>
</#escape>