package com.wwyl.entity.settings;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.TaskMode;
import com.wwyl.Enums.TaskStatus;
import com.wwyl.Enums.TaskType;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;

/**
 * 任务
 *
 * @author fyunli
 */
@Entity
@Table(name = "TJ_TASK")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class Task extends PersistableEntity {

    @Basic
    private TaskMode taskMode; // 模式：任务或提醒
    @Basic
    private TaskType taskType;// 类型：上架、拣货等
    @NotBlank
    @Column(length = 200, nullable = false)
    private String subject;// 任务主题
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date taskTime;// 分派时间
    @Column(length = 2000)
    private String content;// 内容
    @Column(length = 100)
    private String url;// 链接，如上架任务的上架单链接
    @Basic
    private TaskStatus taskStatus;// 任务状态
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;// 责任人
    @Temporal(TemporalType.TIMESTAMP)
    private Date completeTime;// 完成时间

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TJ_TASK_ACCEPTERS", joinColumns = {@JoinColumn(name = "task")}, inverseJoinColumns = {@JoinColumn(name = "operator")})
    private Set<Operator> accepters;

    public TaskMode getTaskMode() {
        return taskMode;
    }

    public void setTaskMode(TaskMode taskMode) {
        this.taskMode = taskMode;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Set<Operator> getAccepters() {
        return accepters;
    }

    public void setAccepters(Set<Operator> accepters) {
        this.accepters = accepters;
    }

}
