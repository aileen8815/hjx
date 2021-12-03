<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/finance/create_standing_book_daily_all_customer">重新生成所有客户指定日期台帐信息</a>
</header>

<div class="panel-body main-content-wrapper site-min-height">
 
	<div class="form-group">
      <label class="col-md-1">开始日期:</label>
      <div class="col-md-3">
        <input type="text" class="form-control Wdate" onClick="WdatePicker()" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="startDate" id="startDate" data-parsley-required="true">
      </div>
      
      <label class="col-md-1">结束日期:</label>
      <div class="col-md-3">
        <input type="text" class="form-control Wdate" onClick="WdatePicker()" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="endDate" id="endDate" data-parsley-required="true">
      </div>
        
       <div class="col-md-1">
	  	 <button id="ok" type="button" class="btn btn-primary" onclick="carryoverjs.carryover();">生成所有客户台帐信息</button>	   
	   </div>  
    </div>
    
    <div class="form-group">
    	   <label class="col-md-10" style="font-size:20px;" fontColor="red">点击【开始生成】，请等待系统运行，出现完成提示后再继续其他操作！</label>
    </div>
</div>

<script type="text/javascript">

	var carryoverjs = {

		carryover: function() {	
			var startDate = $('#startDate').val();
			var endDate = $('#endDate').val();
	    	if (startDate != '' && endDate != '') {
	    		location.href = "${base}/finance/standing-book-daily/create-again-standing-book-daily-all-customer?startDate=" + startDate + "&endDate=" + endDate;
	    	} else {
	      		alert("请检查开始日期和结束日期");
	    	}
		}
	}
	
</script>    



</#escape>
