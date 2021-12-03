<#escape x as x?html>
<#include "lodop.ftl">
<br/>
<a href="javascript:lodop_preview()">打印预览</a><br/>
<a href="javascript:lodop_print()">直接打印</a><br/>      
<a href="javascript:lodop_printA()">选择打印机</a><br><br>

<script language="javascript" type="text/javascript">   
    var LODOP; //声明为全局变量
 
    var jsonStr = '{"data":{"id":"1","name":"test"}}';
    var jsonObj = eval('(' + jsonStr + ')');

   <#-- 打印预览 -->
	function lodop_preview() {
		CreatePrintContent(jsonObj.entity);	
		LODOP.PREVIEW();	
	};
	
	<#-- 打印 -->
	function lodop_print() {
		CreatePrintContent(jsonObj.entity);
		LODOP.PRINT();	
	};
	
	<#-- 打印设置 -->
	function lodop_printA() {
		CreatePrintContent(jsonObj.entity);
		LODOP.PRINTA(); 	
	};
		
	<#-- 创建打印内容 -->
	function CreatePrintContent(entity){
		LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
		<#-- 以下为打印内容 -->
		<#include "template/lodop.ftl">
	};
</script>
</#escape>