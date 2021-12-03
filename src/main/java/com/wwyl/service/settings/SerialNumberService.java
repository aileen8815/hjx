package com.wwyl.service.settings;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wwyl.Enums.TransactionType;
import com.wwyl.dao.settings.SerialNumberDao;
import com.wwyl.entity.settings.SerialNumber;

/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class SerialNumberService {

	@Resource
	private SerialNumberDao serialNumberDao;

	private static String serialLock = "SerialNumberServiceLock";

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public String getSerialNumber(final TransactionType transactionType) {
		synchronized (serialLock) {
			SerialNumber serialNumber = this.findSerialNumber(transactionType);
			Assert.notNull(serialNumber);

			Date now = new Date();
			int number = 1;
			if (StringUtils.isBlank(serialNumber.getDateFormat()) || DateUtils.isSameDay(now, serialNumber.getSerialDate())) {
				number = serialNumber.getMaxNumber() + 1;
			} else {
				serialNumber.setSerialDate(now);
			}
			serialNumber.setMaxNumber(number);
			serialNumberDao.save(serialNumber);
			serialNumberDao.flush();
			return (serialNumber.getPrefix() == null ? "" : serialNumber.getPrefix())
					+ (StringUtils.isBlank(serialNumber.getDateFormat()) ? "" : DateFormatUtils.format(now, serialNumber.getDateFormat()))
					+ getFixedLengthNumber(number, serialNumber.getLength());
		}
	}

	public SerialNumber findSerialNumber(final TransactionType transactionType) {
		return serialNumberDao.findOne(new Specification<SerialNumber>() {
			@Override
			public Predicate toPredicate(Root<SerialNumber> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("transactionType").as(TransactionType.class), transactionType);
			}
		});
	}

	public String getFixedLengthNumber(final int number, final int length) {
		String fixedLengthNumber = String.valueOf(number);
		int point = fixedLengthNumber.length();
		for (int i = length; i > point; i--) {
			fixedLengthNumber = "0" + fixedLengthNumber;
		}
		return fixedLengthNumber;
	}

}
