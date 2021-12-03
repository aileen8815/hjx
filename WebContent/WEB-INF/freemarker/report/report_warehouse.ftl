<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/report/report-warehouse">仓库汇总报表</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <div>
         
      <form id="report-form" target="reportFrame"   action="${reportUrl}/pentaho/api/repos/%3Apublic%3Awarehouse.prpt/report"  class="form-horizontal" data-parsley-validate>
        <input type ="hidden" id="output-target" name="output-target" value="pageable/pdf">
		<input type ="hidden" id="userid" name="userid" value="${user}">
      	<input type ="hidden" id="password" name="password" value="${password}">

        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="col-md-4 control-label">库区:</label>
                    <div class="col-md-8">
                      	    <select id="storeAreaId" name="storeAreas" class="form-control">
                                    <option value="-1">全部库区</option>
                                    <#list storeAreaList as storeArea>
                                        <#if !storeArea.parent?exists>
                                            <option value="${storeArea.id}" >${storeArea.code} ${storeArea.name}</option>
                                            <#list storeArea.children as area>
                                                <option value="${area.id}" >&nbsp;&nbsp;&nbsp;&nbsp;${area.code} ${area.name}</option>
                                            </#list>
                                        </#if>
                                    </#list>
                            </select>
                             <input type ="hidden" id="storeAreaText" name="storeAreaText" value="全部库区">
                    </div>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="form-group">
                    <label class="col-md-4 control-label">上日时间:</label>

                    <div class="col-md-8">
                               <input type="text" class="form-control Wdate"   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"   name="startDate"
                               id="startDate"   value=""
                               placeholder="上日时间" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">当日时间:</label>

                    <div class="col-md-8">
                               <input type="text" class="form-control Wdate"   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"   name="endDate"
                               id="endDate"   value=""
                               placeholder="当日时间" >
                    </div>
                </div>
            </div>
       
	   <div class="col-md-4">
                        <button type="submit" class="btn btn-primary"    onClick="setReportTypeQuery()" >查询</button> &nbsp;&nbsp;&nbsp;
                        <button type="submit" class="btn btn-primary"  onclick="setExpReportType()">导出Excel</button> 
 		</div>	
	
 	</div>
         
  
    </form>
         
    </div>
    <iframe id="reportFrame" name="reportFrame"   width="100%" height="600" frameborder="0"></iframe>
</div>
<script src="${base}/assets/report/report.js"></script>
<script type="text/javascript">
	 $(function () {
   		$("#startDate").val(getCurrentTime());
   		$("#endDate").val(getCurrentTime());
        $("#storeAreaId").bind("change",function(){
        	var checkText=$("#storeAreaId").find("option:selected").text();
			$("#storeAreaText").val(checkText);					
		});

    });

 	function setExpReportType(){
	$("#output-target").val('table/excel;page-mode=flow');
	}
 
   	function setReportTypeQuery(){
		$("#output-target").val('pageable/pdf');
	}
	function getCurrentTime() {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) ;
    }
</script>
</#escape>
