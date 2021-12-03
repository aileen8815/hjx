<#if currentOperator.hasPermission('1400')>
<a data-toggle="dropdown" class="dropdown-toggle" href="#">
    <i class="fa fa-bell-o"></i>
    <#if notices?has_content><span class="badge bg-warning">${notices?size}</span></#if>
</a>
<ul class="dropdown-menu extended notification">
    <div class="notify-arrow notify-arrow-yellow"></div>
    <li>
        <p class="yellow">你有 ${notices?size} 个预警提醒</p>
    </li>
    <#list notices as notice>
    <li>
        <div>
            <a href="${base}/task/${notice.id}">
                <span class="notify-type">${notice.taskType!}</span>: ${notice.subject!}
            </a>
        </div>
    </li>
    </#list>
    <li>
        <a href="${base}/task/index?taskMode=${'预警提醒'?url('utf-8')}">查看所有预警提醒</a>
    </li>
</ul>
</#if>