<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-booking">入库预约</a> -
    <#if inboundBooking.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <form id="inboundBooking-form" action="${base}/store/inbound-booking/${inboundBooking.id!}" method="post" class="form-horizontal" data-parsley-validate onsubmit="return getData()">
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${inboundBooking.createdBy!}">
        <input type="hidden" name="bookTime" value="${inboundBooking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="createdTime" value="${inboundBooking.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${inboundBooking.serialNo}">
        <input type="hidden" name="bookingStatus" value="${inboundBooking.bookingStatus}">
        <input type="hidden" name="productCheck" id="productCheck" value="">
        <input type="hidden" name="inboundRegisterId" id="inboundRegisterId" value="${inboundRegisterId}">

        <div class="row">
            <div class="col-md-11 row">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">客户:</label>

                            <div class="col-md-8">
                                <select id="customerId" name="customer.id" class="form-control" onchange="selectedCustomer(this.value)" data-parsley-required="true">
                                    <option value="">请选择客户</option>
                                    <#list customers as customer>
                                        <option value="${customer.id}"
                                                <#if customer.id == inboundBooking.customer.id>selected</#if>>${customer.text!}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">预约仓间:</label>

                            <div class="col-md-8">
                                <select id="storeAreaId" name="storeArea.id" class="form-control">
                                    <#list storeAreas as storeArea>
                                        <#if !storeArea.parent?exists>
                                            <option value="${storeArea.id}" <#if storeArea.id == inboundBooking.storeArea.id>selected</#if>>${storeArea.code} ${storeArea.name}</option>
                                            <#list storeArea.children as area>
                                                <option value="${area.id}" <#if area.id == inboundBooking.storeArea.id>selected</#if>>&nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                            </#list>
                                        </#if>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">

                    <div class="col-md-12" id="showcontactinfo"   <#if !inboundBooking.id?exists> style="display:none" </#if>>
                        <div class="form-group">
                            <label class="col-md-2 control-label">联系人列表:</label>

                            <div class="col-md-10" id="cancatlist">
                                <#if inboundBooking.id?exists>
                                    <#list contactSet as contact>
                                        <input type=radio name=contact.id value="${contact.id}" <#if contact.id == inboundBooking.contact.id>checked</#if>
                                               onclick="setval()">${contact.contactinfo}<a href="javascript:void(0)" onclick="contactjs.editlinkman(${contact.id})">编辑</a><a
                                        href="javascript:void(0)" onclick="contactjs.dellinkman(${contact.id})"> 删除</a></br>
                                    </#list>
                                    <label class="control-label"><a href="javascript:void(0)" data-toggle="modal" data-target="#location-selector-modal">添加联系人</a></label>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">预约方式:</label>

                            <div class="col-md-8">
                                <select id="bookingMethodId" name="bookingMethod.id" class="form-control">

                                    <#list bookingMethods as bookingMethod>
                                        <option value="${bookingMethod.id}"
                                                <#if bookingMethod.id == inboundBooking.bookingMethod.id>selected</#if>>${bookingMethod.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">申请入库时间:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'(.now)'})" name="applyInboundTime"
                                       id="applyInboundTime"
                                       value="<#if inboundBooking.applyInboundTime?exists>${inboundBooking.applyInboundTime?string('yyyy-MM-dd HH:mm')}</#if>"
                                       placeholder="预约入库时间" data-parsley-required="true">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">车辆类型:</label>

                            <div class="col-md-8">
                                <select id="vehicleTypeId" name="vehicleType.id" class="form-control">
                                    <option value="">请选择车辆类型</option>
                                    <#list vehicleTypes as vehicleType>
                                        <option value="${vehicleType.id}" <#if vehicleType.id == inboundBooking.vehicleType.id>selected</#if>>${vehicleType.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">车辆数目:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control" name="vehicleAmount" id="vehicleAmount" value="${inboundBooking.vehicleAmount}"
                                       data-parsley-type="integer" data-parsley-min="0">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="col-md-4 control-label">车牌号:</label>

                            <div class="col-md-8">
                                <input type="text" class="form-control" name="vehicleNumbers" id="vehicleNumbers" value="${inboundBooking.vehicleNumbers}">
                            </div>
                        </div>
                    </div>
                <#-- <div class="col-md-5">
                     <div class="form-group">
                         <label class="col-md-4 control-label">预分配托盘数:</label>

                         <div class="col-md-8">
                             <input type="text" class="form-control" name="storeContainerCount" id="storeContainerCount" value="${inboundBooking.storeContainerCount}"
                                    data-parsley-required="true" data-parsley-type="integer"  data-parsley-gt="0">
                         </div>
                     </div>
                 </div>-->
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="col-md-2 control-label">客户留言:</label>

                            <div class="col-md-10">

                                <textarea class="form-control" name="note" id="note" value="${inboundBooking.note}" rows="2"></textarea>
                            </div>
                        </div>
                    </div>
                </div>

            <#--商品详细 -->
            <#-- JEasyUI DataGrid 显示数据 -->
                <#if inboundRegisterId?exists>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label class="col-md-2 control-label">预约明细:</label>

                                <div class="col-md-10">
                                    <table class="easyui-datagrid" id="inbound-booking-datagrid"
                                           data-options="url:'${base}/store/inbound-booking/list-difference?inboundRegisterId=${inboundRegisterId}',method:'get',rownumbers:true,singleSelect:false,selectOnCheck:true,checkOnSelect:false,
      pagination:false,fitColumns:true,collapsible:false,onLoadSuccess:loadSuccess">
                                        <thead>
                                        <tr>
                                            <th data-options="field:'productId',checkbox:true"></th>
                                            <th data-options="field:'productName',width:100">商品</th>
                                            <th data-options="field:'amount',width:80,formatter: rowformater">数量</th>
                                            <th data-options="field:'storeContainerCount',formatter: storeContainerCount,width:80">预计托盘数</th>
                                            <th data-options="field:'amountMeasureUnitName',width:100">数量单位</th>
                                            <th data-options="field:'weight',width:80">重量</th>
                                            <th data-options="field:'weightMeasureUnitName',width:100">重量单位</th>


                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <div class="col-md-offset-2  col-md-12">
                                <button type="submit" class="btn btn-primary">保存</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<!-- Modal -->
<div class="modal fade" id="location-selector-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">联系人基本信息</h4>
            </div>
            <form id="linkman-form" action="" method="post" onsubmit="return contactjs.saveContact()" class="form-horizontal" data-parsley-validate>
                <input type="hidden" class="form-control" name="contactid" id="contactid" value="">

                <div id="selector-body" class="modal-body">
                    <div class="row">
                        <div class="col-md-11">
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">联系人:</label>

                                            <div class="col-md-8">
                                                <input type="text" class="form-control" name="linkman" id="linkman" value="${contact.linkman!}"
                                                       data-parsley-required="true">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">手机:</label>

                                            <div class="col-md-8">
                                                <input type="text" class="form-control" name="mobile" id="mobile" value="${contact.mobile!}"
                                                       data-parsley-required="true">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">传真:</label>

                                            <div class="col-md-8">
                                                <input type="text" class="form-control" name="fax" id="fax" value="${contact.fax!}">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">电话:</label>

                                            <div class="col-md-8">
                                                <input type="text" class="form-control" name="tel" id="tel" value="${contact.tel!}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">电子邮箱:</label>

                                            <div class="col-md-8">
                                                <input type="text" class="form-control" name="email" id="email" value="${contact.email!}"
                                                       data-parsley-type="email">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="col-md-4 control-label">邮编:</label>

                                            <div class="col-md-8">
                                                <input type="text" class="form-control" name="zip" id="zip" value="${contact.zip!}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label class="col-md-2 control-label">地址:</label>

                                            <div class="col-md-10">
                                                <input type="text" class="form-control" name="address" id="address" rows="1" value="${contact.address!}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">保存</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="contactjs.setlinkmanform()">关闭</button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    $('#note').val('${inboundBooking.note}');

    function selectedCustomer(value) {
        if (value == '') {
            $("#cancatlist").text('');
            $("#showcontactinfo").hide();
            return;
        }
        $.ajax({
            url: "${base}/customer/contact/selected-customer?customerId=" + $('#customerId').val(),
            data: '',
            type: "POST",
            success: function (data) {
                contactjs.successData(data);
                contactjs.setlinkmanform();
                $("#showcontactinfo").show();
            }
        })

    }

    var contactjs = {
        saveContact: function () {
            if ($("#linkman").val().trim() == "" || $("#mobile").val().trim() == "") {
                return false;
            }
            if ($("#email").val().trim() != "") {
                var myreg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
                if (!myreg.test($("#email").val().trim())) {
                    return false;
                }
            }
            $.ajax({
                url: "${base}/customer/contact/" + $("#contactid").val() + "/create-contact",
                data: {"id": $("#contactid").val(), "linkman": $("#linkman").val(), "mobile": $("#mobile").val(), "tel": $("#tel").val(),
                    "fax": $("#fax").val(), "zip": $("#zip").val(), "address": $("#address").val(), "email": $("#email").val(), "customerId": $("#customerId").val()
                },
                type: "POST",
                success: function (data) {

                    if ($("#contactid").val() == '') {//新建
                        var ar = eval('(' + data + ')');
                        var t2 = $("#cancatlist");
                        var info = '<input type=radio name=contact.id id=' + ar.id + '  checked value="' + ar.id + '"  >' + ar.contactinfo + '<a href="javascript:void(0)"  onclick="contactjs.editlinkman(' + ar.id + ')" >编辑</a><a href="javascript:void(0)"  onclick="contactjs.dellinkman(' + ar.id + ')" >   删除</a></br>';
                        t2.prepend(info);
                    } else {//编辑
                        contactjs.successData(data);
                    }
                    contactjs.setlinkmanform();
                }
            })
            $('#location-selector-modal').modal('hide');

            return false;
        },

        setlinkmanform: function (id, linkman, mobile, tel, fax, zip, address, email) {

            $("#contactid").val(id);
            $("#linkman").val(linkman);
            $("#mobile").val(mobile);
            $("#tel").val(tel);
            $("#fax").val(fax);
            $("#zip").val(zip);
            $("#address").val(address);
            $("#email").val(email);
            $("#linkman-form").parsley().reset();

        },

        successData: function (data) {
            var ar = eval('(' + data + ')');
            var t2 = $("#cancatlist");
            t2.text('');
            for (var i = 0; i < ar.length; i++) {
                var info = '<input type=radio name=contact.id id=' + ar[i].id + '  checked value="' + ar[i].id + '"  >' + ar[i].contactinfo + '<a href="javascript:void(0)"  onclick="contactjs.editlinkman(' + ar[i].id + ')" >编辑</a><a href="javascript:void(0)"  onclick="contactjs.dellinkman(' + ar[i].id + ')" >   删除</a></br>';
                t2.prepend(info);
            }
            t2.append('<label class="control-label"><a href="javascript:void(0);" data-toggle="modal" data-target="#location-selector-modal" >添加联系人</a></label>');
        },

        editlinkman: function (id) {
            $.ajax({
                url: "${base}/customer/contact/booking-edit-contact",
                data: {"id": id, "customerId": $("#customerId").val()},
                type: "POST",
                success: function (data) {
                    var ar = eval('(' + data + ')');
                    contactjs.setlinkmanform(ar.id, ar.linkman, ar.mobile, ar.tel, ar.fax, ar.zip, ar.address, ar.email);
                }
            })
            $('#location-selector-modal').modal('show');

        },

        dellinkman: function (id) {

            $.ajax({
                url: "${base}/customer/contact/booking-del-contact",
                data: {"id": id, "customerId": $("#customerId").val()},
                type: "POST",
                success: function (data) {
                    contactjs.successData(data);
                }
            })
        }
    }
    function getData() {
        var result = true;
        if ('${inboundRegisterId}' != '') {
            var checkedItems = $('#inbound-booking-datagrid').datagrid('getChecked');
            var ids = [];
            var reg = "^[1-9]*[1-9][0-9]*$";

            $.each(checkedItems, function (index, item) {

                var inboundAmount = $('#val_' + item.productId).val();
                var storeContainerCount = $('#str_' + item.productId).val();
                if (inboundAmount.match(reg) == null || storeContainerCount.match(reg) == null) {
                    result = false;
                    return result;
                }
                ids.push(item.productId + ":" + inboundAmount + ":" + storeContainerCount);
            });
            if (!result) {
                alert("选择的商品数量或预计托盘数需大于零");
            }

            $('#productCheck').val(ids);
        }
        return result;
    }
    function rowformater(value, row, index) {
        return   '<input type="text"  name="inboundAmount" id=val_' + row.productId + ' style="color:red"   onchange="selectedproduct(this.id,this.value)"  data-parsley-type="integer"  value=' + value + ' >'
    }
    function storeContainerCount(value, row, index) {
        return   '<input type="text"  name="storeContainerCount" id=str_' + row.productId + ' style="color:red"  data-parsley-type="integer"  value=' + value + ' >'
    }

    function selectedproduct(id, value) {
        if (id.trim() == '') {
            return;
        }
        var productId = id.replace(/val_/, "");
        $.ajax({
            url: "${base}/settings/customer/getproduct?id=" + productId,
            data: '',
            type: "get",
            success: function (data) {
                var ar = eval('(' + data + ')');
                var reg = "^[1-9]*[1-9][0-9]*$";
                if (value.match(reg) != null) {
                    var count = 0;
                    if (value % ar.bearingCapacity > 0) {
                        count = 1
                    }
                    $("#" + "str_" + productId).val((value - value % ar.bearingCapacity) / ar.bearingCapacity + count);

                }
            }
        })
    }

	function loadSuccess(data){
 		if(data){
			$.each(data.rows, function(index, item){
		       $('#inbound-booking-datagrid').datagrid('checkRow', index);
			});
		}
	}
</script>
</#escape>
