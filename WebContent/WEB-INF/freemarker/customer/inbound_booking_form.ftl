<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/customer/customer-inbound-booking">入库预约</a> - 
    <#if inboundBooking.id?exists>编辑<#else>新建</#if>
</header>
<div class="panel-body main-content-wrapper site-min-height">
  <form id="inboundBooking-form" action="${base}/customer/customer-inbound-booking/${inboundBooking.id!}" method="post" class="form-horizontal" data-parsley-validate>
        <input type="hidden" name="_method" value="${_method}">
        <input type="hidden" name="createdBy" value="${inboundBooking.createdBy!}">
        <input type="hidden" name="bookTime" value="${inboundBooking.bookTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" name="createdTime" value="${inboundBooking.createdTime?default(.now)?string('yyyy-MM-dd HH:mm:ss')}">
        <input type="hidden" class="form-control" name="serialNo" id="serialNo" value="${inboundBooking.serialNo}">
        <input type="hidden" name="bookingStatus" value="${inboundBooking.bookingStatus}">
  		<input type="hidden" name="bookingMethod.id"   value="${inboundBooking.bookingMethod.id}" >
        <input type="hidden" name="customer.id"  id="customerId"  value="${inboundBooking.customer.id}" >
        

        <div class="row">
           
            <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">预约仓间:</label>

                    <div class="col-md-8">
                        <select id="storeAreaId" name="storeArea.id" class="form-control">
                            <#list storeAreas as storeArea>
                                <option value="${storeArea.id}"
                                        <#if storeArea.id == inboundBooking.storeArea.id>selected</#if>>${storeArea.code} ${storeArea.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">申请入库时间:</label>

                    <div class="col-md-8">
                        <input type="text" class="form-control Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'(.now)'})"  name="applyInboundTime"
                               id="applyInboundTime"
                               value="<#if inboundBooking.applyInboundTime?exists>${inboundBooking.applyInboundTime?string('yyyy-MM-dd HH:mm')}</#if>"
                               placeholder="预约入库时间" data-parsley-required="true">
                    </div>
                </div>
            </div>
        </div>

        <div class="row">

            <div class="col-md-10" id="showcontactinfo"  >
                <div class="form-group">
                    <label class="col-md-2 control-label">联系人列表:</label>

                    <div class="col-md-10" id="cancatlist">
                        
                            <#list contactSet as contact>
                                <input type=radio name=contact.id value="${contact.id}" <#if contact.id == inboundBooking.contact.id>checked</#if>
                                       onclick="setval()">${contact.contactinfo}<a href="javascript:void(0)" onclick="contactjs.editlinkman(${contact.id})">编辑</a><a
                                    href="javascript:void(0)" onclick="contactjs.dellinkman(${contact.id})"> 删除</a></br>
                            </#list>
                            <label class="control-label"><a href="javascript:void(0)" data-toggle="modal" data-target="#location-selector-modal">添加联系人</a></label>
                        
                    </div>
                </div>
            </div>
        </div>

 
        <div class="row">
            <div class="col-md-5">
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
            <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">车辆数目:</label>

                    <div class="col-md-8">
                        <input type="text" class="form-control" name="vehicleAmount" id="vehicleAmount" value="${inboundBooking.vehicleAmount}"
                               data-parsley-type="integer"   data-parsley-min="0">
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-5">
                <div class="form-group">
                    <label class="col-md-4 control-label">车牌号:</label>

                    <div class="col-md-8">
                        <input type="text" class="form-control" name="vehicleNumbers" id="vehicleNumbers" value="${inboundBooking.vehicleNumbers}">
                    </div>
                </div>
            </div>
           
        </div>
        <div class="row">
            <div class="col-md-10">
                <div class="form-group">
                    <label class="col-md-2 control-label">客户留言:</label>

                    <div class="col-md-10">
 
                        <textarea class="form-control" name="note" id="note"  value="${inboundBooking.note}"  rows="3"></textarea>
                    </div>
                </div>
            </div>
        </div>
        

 
        <div class="row">
            <div class="col-md-10">
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-3">
                        <button type="submit" class="btn btn-primary" >保存</button>
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
  

    var contactjs = {
        saveContact: function () {
            if ($("#linkman").val().trim() == "" || $("#mobile").val().trim() == "" ) {
                return false;
            }
            if( $("#email").val().trim() != ""){
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
	
</script>
</#escape>
