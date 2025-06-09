package com.vasworks.cofo.model;

import java.util.Date;

public interface Auditable {
	void setLastUpdated(Date timestamp);
	Date getLastUpdated();

	void setLastUpdatedBy(Long userId);
	Long getLastUpdatedBy();

	void setCreatedDate(Date timestamp);
	Date getCreatedDate();

	void setCreatedBy(Long userId);
	Long getCreatedBy();
}
