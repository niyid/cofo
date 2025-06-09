package com.vasworks.cofo.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.search.annotations.DocumentId;

/**
 * The class is the base entity for Audited (see {@link Auditable}) Hibernate managed entities. Every entity has a <code>Long</code> <b>id</b>.
 */
@MappedSuperclass
public class MySqlBaseEntity implements Serializable, Comparable<MySqlBaseEntity>, Auditable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5679412449041448091L;

	private Date lastUpdated;

	private Long lastUpdatedBy;

	private Date createdDate;

	private Long createdBy;

	/** The id. */
	private Long id;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@DocumentId
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MySqlBaseEntity entity) {
		if(entity != null) {
			if(id != null && entity.getId() != null) {
				return id.compareTo(entity.getId());
			}
		}
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o==null) return super.equals(o);
		if (id==null) return super.equals(o);
		if (!(o instanceof MySqlBaseEntity) || ((MySqlBaseEntity) o).getId()==null) return super.equals(o);
		
		if (getClass().equals(o.getClass())) {
			if (id.equals(((MySqlBaseEntity) o).getId())) {
				return true;
			}
		}

		return false;
	}

	public Object displayPropertyValue(String property) throws Exception {
		Object value = null;

		Method[] m = this.getClass().getDeclaredMethods();
		for (int i = 0; i < m.length; i++) {
			if (m[i].getName().equalsIgnoreCase("get" + property) || m[i].getName().equalsIgnoreCase("is" + property)) {
				value = m[i].invoke(this, (Object[]) null);
				break;
			}
		}

		if (value != null && "false".equals(value.toString())) {
			value = "N/A";
		}
		if (value != null && "true".equals(value.toString())) {
			value = "YES";
		}

		if (value instanceof Double) {
			DecimalFormat formatMask = new DecimalFormat("###,###,###,###,###,###,###,###,###.00");
			value = formatMask.format(value);
		}

		return value;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUpdated() {
		return lastUpdated;
	}

	@Override
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	@Override
	public void setLastUpdated(Date timestamp) {
		lastUpdated = timestamp;
	}

	@Override
	public void setLastUpdatedBy(Long username) {
		lastUpdatedBy = username;
	}

	@Override
	public Long getCreatedBy() {
		return createdBy;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedBy(Long username) {
		createdBy = username;
	}

	@Override
	public void setCreatedDate(Date timestamp) {
		createdDate = timestamp;
	}

	public String buildViewUrl(String prefix) {
		StringBuilder builder = new StringBuilder();

		builder.append(prefix);
		builder.append("?id=");
		builder.append(getId());
		builder.append("&typeId=");
		builder.append(this.getClass().getSimpleName());

		return builder.toString();
	}
	
	public String shortString() {
		String shortString = toString();
		StringBuilder b = new StringBuilder(toString());
		
		int openingBraceIdx = b.indexOf("{");
		int closingBraceIdx = b.indexOf("}");
		
		if(openingBraceIdx > -1 && closingBraceIdx > -1) {
			shortString = b.substring(closingBraceIdx + 1);
		} else {
			if(b.length() > 27) {
				shortString = b.substring(0, 27) + "...";
			}
		}
		
		return shortString;
	}
}
