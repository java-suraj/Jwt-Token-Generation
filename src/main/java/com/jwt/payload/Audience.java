package com.jwt.payload;

public enum Audience {
	ADMIN("Admin Audience"), GUEST("Guest Audience"), MODERATOR("Moderator Audience"), USER("User Audience");

	public static Audience getAudience(String audienceName) {
		for (Audience audience : Audience.values()) {
			if (audience.getAudienceName().equalsIgnoreCase(audienceName)) {
				return audience;
			}
		}
		return null;
	}

	public static boolean isValidAudience(Audience audience) {
		for (Audience audienceName : Audience.values()) {
			if (audienceName.equals(audience)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValidAudience(String audienceName) {
		for (Audience audience : Audience.values()) {
			if (audience.getAudienceName().equalsIgnoreCase(audienceName)) {
				return true;
			}
		}
		return false;
	}

	private final String audienceName;

	Audience(String audienceName) {
		this.audienceName = audienceName;
	}

	public String getAudienceName() {
		return this.audienceName;
	}
}
