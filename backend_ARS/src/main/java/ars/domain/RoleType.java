package ars.domain;

public enum RoleType {
	ADMIN("ADMIN"), CUSTOMER("CUSTOMER"), PROVIDER("PROVIDER");

	private final String roleType;

	/**
	 * @param roleType
	 */
	RoleType(final String roleType) {
		this.roleType = roleType;
	}

	@Override
	public String toString() {
		return roleType;
	}
}