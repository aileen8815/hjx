<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/tenancy/store-contract-carryover">合同结转</a>
</header>

<div class="panel-body main-content-wrapper site-min-height">
 
	<div class="form-group">
      <label class="col-md-2">结转日期:</label>
      <div class="col-md-3">
        <input type="text" class="form-control Wdate" onClick="WdatePicker()" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="startDate" id="startDate" data-parsley-required="true">
      </div>
    </div>
    
    <div class="form-group">
    <div class="col-md-12">       
	   <button id="ok" type="button" class="btn btn-primary" onclick="carryoverjs.carryover();">确定</button>	   
	</div>  	   
    </div>
</div>

<script type="text/javascript">

	var carryoverjs = {

		carryover: function() {	
			var datestr = $('#startDate').val();
	    	if (datestr != '') {
	    		location.href = "${base}/tenancy/store-contract/start-carryover?datestr=" + datestr;
	    	} else {
	      		alert("请选择结转日期");
	    	}
		}
	}
	
</script>    



</#escape>
