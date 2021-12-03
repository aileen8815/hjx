<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/store/inbound-receipt">费用结算</a>
</header>

<div class="panel-body main-content-wrapper site-min-height">
  <div class="row">
    <div class="col-md-1">客户：</div>
    <div class="col-md-3">${payment.customerName}</div>
        <div class="col-md-1">计费类型：</div>
    	<div class="col-md-3">${payment.chargeTypeName}</div> 
  </div>
  </br>
  <form id="payment-form" action="${base}/store/outbound-booking/${outboundBooking.id!}" method="post" class="form-horizontal" data-parsley-validate>
				  <table class="table table-striped table-hover">
                              <thead>
                              <tr>
                                  <th>序号</th>
                                  <th >收费项目名</th>
                                   <th>收费金额（元）</th>
                                  <th >收费项目描述</th>
                                 
                              </tr>
                              </thead>
                              <tbody>
                              <#list paymentItems as paymentItem>
                              <tr>
                                  <td>${paymentItem_index+1}</td>
                                  <td>${paymentItem.feeItemName}</td>
                                  <td>
                                    <div class="col-md-5">
                                    <input type="hidden" class="form-control"   name="id" id="id" value="${paymentItem.id}" >
        							<input type="text" class="form-control"   name="amount" id="amount" value="${paymentItem.amount}" data-parsley-required="true">
                           			</div>
                           		  </td>
                                  <td class="">${paymentItem.feeItemRemark}</td>
                             
                              </tr>
                              </#list>
                             
                              </tbody>
                          </table>
                   
        
                          <div class="text-center invoice-btn">
                             
                              <button  class="btn btn-danger btn-lg"  onclick="save();"><i class="fa fa-check">结算</i></button>
                          </div>
                           </form>
  </div>
</div>
<script type="text/javascript">


  function save(){
 	 $('#payment-form').attr("action", "${base}/store/inbound-receipt/payment-clearing?paymentId=${payment.id}");
     $('#outboundCheckItem-form').submit();
 }

</script>
</#escape>
