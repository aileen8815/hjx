<#if currentOperator.hasPermission('1300')>
 <a data-toggle="dropdown" class="dropdown-toggle" href="#">
    <i class="fa fa-tasks"></i>
    <#if tasks?has_content><span class="badge bg-success">${tasks?size}</span></#if>
</a>
<ul class="dropdown-menu extended tasks-bar">
    <div class="notify-arrow notify-arrow-green"></div>
    <li>
        <p class="green">您有 ${tasks?size} 个工作通知</p>
    </li>
    <#list tasks as task>
    <li>
        <a href="${base}/task/${task.id}"><span class="notify-type">${task.taskType!}</span>: ${task.subject!}</a>
    </li>
    </#list>
    <li class="external">
        <a href="${base}/task/index?taskMode=${'工作通知'?url('utf-8')}">查看所有工作通知</a>
    </li>
</ul>
</#if>
