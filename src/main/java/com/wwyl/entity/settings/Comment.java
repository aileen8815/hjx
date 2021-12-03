package com.wwyl.entity.settings;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.CommentType;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;

/**
 * @author fyunli
 */
@Entity
@Table(name = "TJ_COMMENT")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class Comment extends PersistableEntity {

	@Basic
	private CommentType commentType;
	@Lob
	private String content; // 评论内容
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator commentor; // 评论人
	@Temporal(TemporalType.TIMESTAMP)
	private Date commentTime;// 评论时间

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Operator getCommentor() {
		return commentor;
	}

	public CommentType getCommentType() {
		return commentType;
	}

	public void setCommentType(CommentType commentType) {
		this.commentType = commentType;
	}

	public void setCommentor(Operator commentor) {
		this.commentor = commentor;
	}

	public Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}

}
