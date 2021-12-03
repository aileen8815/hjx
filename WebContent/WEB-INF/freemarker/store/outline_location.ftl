<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/outline/area">库区概览</a>
    <span class="store-area-stat-filter">
        请选择库区：
        <select id="areaId" style="width:160px;">
            <option value="">所有库区</option>
            <#list storeAreas as area>
                <#if !area.parent?exists>
                    <option value="${area.id!}" ref="branch" <#if defaultAreaId == area.id>selected</#if>><#if area.parent?exists>&nbsp;&nbsp;&nbsp;&nbsp;</#if>${area.text!}</option>
                    <#list area.children as subarea>
                        <option value="${subarea.id!}" ref="leaf" <#if defaultAreaId == subarea.id>selected</#if>><#if subarea.parent?exists>&nbsp;&nbsp;&nbsp;&nbsp;</#if>${subarea.text!}</option>
                    </#list>
                </#if>
            </#list>
        </select>
    </span>
</header>
<div class="panel-body main-content-wrapper site-min-height">
<#--
<div class="store-area-list">
    <div>
        <ul style="padding-left: 1em;">
            <li><a class="btn btn-link" href="${base}/store/outline/locations?locationCode=${locationCode!}&showStock=${showStock?string}">显示所有库区</a></li>
        </ul>
        <ul class="easyui-tree"
            data-options="url:'${base}/settings/store-area/tree',
                method:'get',
                formatter:treeNodeFormatter">
        </ul>
    </div>
</div>
-->
    <div class="location-map">
        <#assign totalMaxRowCount = 0>
        <#include "/store/outline_location_map.ftl"/>
    </div>
</div>
<script>
    $(function () {
        var panelWidth = ${totalMaxRowCount} *
        101 + 70;
        var originWidth = $('.panel').width();
        if (panelWidth > originWidth) {
            $('.panel').width(panelWidth);
        }

        $('#areaId').select2();
        $('#areaId').change(function () {
            if ($(this).find('option:selected').attr('ref') == "leaf") {
                location.href = "${base}/store/outline/locations?areaId=" + $(this).val() + "&locationCode=${locationCode!}&showStock=${showStock?string}";
            } else {
                alert('请选择下属库房！');
            }
        });
    })
</script>
</#escape>
