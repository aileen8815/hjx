<#escape x as x?html>
<header class="panel-heading">
    <a href="${base}/lodop/design">打印设计</a>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <#include "/print/lodop.ftl">
    <div class="row">
        <div class="col-md-2">
        	<#if defineFiles?has_content>
        		已定义打印模板：<br/>
        		<ul>
        		<#list defineFiles as file>
        			<li><a class="defineLink" href="javascript:;" rel="${file.name}">${file.name?replace('.define', '')}</a></li>
        		</#list>
        		</ul>
        	</#if>
        </div>
        <div class="col-md-10">
        	<form id="lodopDesign">
        		<a href="javascript:lodop_design()" class="btn btn-primary">开启设计器</a><br><br>
        		<label>文件名(不需要扩展名)：</label><br/>
        		<input type="text" id="lodopFile" name="lodopFile" value="" class="form-control">
        		<br/>
        		<label>内容：</label><br/>
        		<textarea rows="12" id="lodopContent" name="lodopContent" class="form-control"></textarea>
        		<br/>
        		<input type="button" class="btn btn-primary" value="保存" id="lodopSave">
        	</form>
        </div>
    </div>
</div>

<script language="javascript" type="text/javascript">   
    var LODOP; //声明为全局变量
		
	<#-- 创建打印内容 -->
	function CreatePrintContent(){
		LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
		eval($('#lodopContent').val()); 
	};
	
	<#-- 开启设计器并复制模板代码 -->
	function lodop_design() {		
		CreatePrintContent();
		$('#lodopContent').val(LODOP.PRINT_DESIGN());
	};
	
	<#-- 保存打印模板 -->
	$('#lodopSave').click(function(){
		$.ajax({
			url: "${base}/lodop/design/save",
			data: $("#lodopDesign").serialize(),
			type: "POST",
			error: function(request) {
				alert("表单提交出错，请稍候再试");
			},
			success: function(data) {
				var result = eval('(' + data + ')');
				if(result.error == 0){
					alert("保存成功");
				} else {
					alert(result.message);
				}
			}
		});
	});
	
	$('.defineLink').click(function(){
		var define = $(this).attr('rel');
		$.get('${base}/lodop/design/content?lodopFile=' + define, function(data){
			$('#lodopFile').val(define.replace('.define', ''));
			var result = $.parseJSON(data);
			$('#lodopContent').val(result.message);
		});
	});
</script>
</#escape>
