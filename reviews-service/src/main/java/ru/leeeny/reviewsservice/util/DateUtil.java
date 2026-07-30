package ru.leeeny.reviewsservice.util;

import ru.leeeny.reviewsservice.exception.ReviewServiceException;

public class DateUtil {
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private DateUtil() {
		throw new ReviewServiceException("Utility class");
	}
}
