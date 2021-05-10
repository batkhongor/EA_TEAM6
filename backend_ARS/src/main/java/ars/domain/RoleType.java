package ars.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum RoleType {
	ADMIN("ADMIN"), CUSTOMER("CUSTOMER"), PROVIDER("PROVIDER");

	private final String roleType;

	/**
	 * @param text
	 */
	RoleType(final String roleType) {
		this.roleType = roleType;
	}

	@Override
	public String toString() {
		return roleType;
	}
}