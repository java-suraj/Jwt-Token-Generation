package com.jwt.payload;

public enum Role {
	ADMIN("Admin"), GUEST("Guest"), USER("User");

	public static Role getRole(String roleName) {
		for (Role role : Role.values()) {
			if (role.getRoleName().equalsIgnoreCase(roleName)) {
				return role;
			}
		}
		return null;
	}

	public static boolean isValidRole(Role role) {
		for (Role roleName : Role.values()) {
			if (roleName.equals(role)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValidRole(String roleName) {
		for (Role role : Role.values()) {
			if (role.getRoleName().equalsIgnoreCase(roleName)) {
				return true;
			}
		}
		return false;
	}

	private final String roleName;

	Role(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return this.roleName;
	}
}
