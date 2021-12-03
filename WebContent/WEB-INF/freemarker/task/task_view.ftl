<header class="panel-heading">
    <h4><a href="${base}/task/index?taskMode=${task.taskMode?url('utf-8')}">${task.taskMode}</a></h4>
</header>
<div class="panel-body main-content-wrapper site-min-height">
    <h4>${task.subject!}</h4>
    ${task.content!}
</div>
