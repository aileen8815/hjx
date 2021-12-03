<#escape x as x?html>
<div class="">
    <span>
        请选择库区：
        <select id="areaId" style="width:160px;">
            <option value="">所有库区</option>
            <#list storeAreas as area>
                <#if !area.parent?exists>
                    <option value="${area.id!}" <#if defaultAreaId == area.id>selected</#if>><#if area.parent?exists>&nbsp;&nbsp;&nbsp;&nbsp;</#if>${area.text!}</option>
                    <#list area.children as subarea>
                        <option value="${subarea.id!}" <#if defaultAreaId == subarea.id>selected</#if>><#if subarea.parent?exists>&nbsp;&nbsp;&nbsp;&nbsp;</#if>${subarea.text!}</option>
                    </#list>
                </#if>
            </#list>
        </select>
    </span>
</div>
<div class="location-map">
    <#assign totalMaxRowCount = 0>
    <#include "/store/outline_location_map.ftl"/>
    <script>
        var modalWidth = ${totalMaxRowCount} *
        101 + 70;
        var originWidth = $('.modal-location-selector').width();
        if (modalWidth > originWidth) {
            $('.modal-location-selector').width(modalWidth);
        }
    </script>
</div>
<div class="clearfix"></div>
<div>
    选择的储位: <span id="location-result">${selectedLocationCode!}</span>
</div>
<script>
    $(function () {
        $('#areaId').select2();

        var url = '${base}/store/store-location-selector?customerId=${customerId}&allowSelectStatus=${allowSelectStatus!}&showStock=${showStock?string}&selectedLocationCode=${selectedLocationCode}&&selectableStoreContainer=${selectableStoreContainer}';
        $('#areaId').change(function () {
            var reloadUrl = url + "&areaId=" + $(this).val();
            $.get(reloadUrl, function (data) {
                $("#selector-body").html(data);
            });
        });

        $('.location-selector').click(locationNodeClick);
    });
</script>
</#escape>
