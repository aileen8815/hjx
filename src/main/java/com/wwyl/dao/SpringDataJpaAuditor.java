package com.wwyl.dao;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.wwyl.ThreadLocalHolder;

/**
 * @author fyunli
 */
@Component
public class SpringDataJpaAuditor implements AuditorAware<String> {

	@SuppressWarnings("unused")
	private String currentAuditor;

	@Override
	public String getCurrentAuditor() {
		return ThreadLocalHolder.getCurrentOperator() == null ? "" : ThreadLocalHolder.getCurrentOperator().getUsername();
	}

	public void setCurrentAuditor(String currentAuditor) {
		this.currentAuditor = currentAuditor;
	}

}
