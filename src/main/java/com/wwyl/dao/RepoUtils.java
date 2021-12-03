package com.wwyl.dao;

import org.springframework.data.domain.PageRequest;

/**
 * @author fyunli
 */
public final class RepoUtils {

	public static final int MIN_PAGE_SIZE = 10;
	public static final String DEFAULT_PAGE_SIZE = "" + MIN_PAGE_SIZE;

	// EasyUI页码从一开始（一般国人习惯从1开始分页），但JPA实现页码从0开始
	public static final PageRequest buildPageRequest(int page, int rows) {
		int jpaPage = (page - 1) > 0 ? (page - 1) : 0;
		int jpaRows = rows > MIN_PAGE_SIZE ? rows : MIN_PAGE_SIZE;
		return new PageRequest(jpaPage, jpaRows);
	}

}
