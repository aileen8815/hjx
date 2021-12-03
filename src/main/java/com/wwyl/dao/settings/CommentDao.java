package com.wwyl.dao.settings;


import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Comment;

/**
 * @author liujian
 */
@Repository
public interface CommentDao extends BaseRepository<Comment, Long> {

 

}
