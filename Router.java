/**
 * This class represent a single router entry, will be stored within the array list.
 */
public class Router {

	private String hostname;
	private String ipAddress;
	private String patched;
	private String osVersion;
	private String notes;

	public Router(String hostname, String ip, String status, String os, String note) {

		this.hostname = hostname;
		this.ipAddress = ip;
		this.patched = status;
		this.osVersion = os;
		this.notes = note;
	}

	public String getHostname() {
		return hostname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String isPatched() {
		return patched;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getNotes() {
		return notes;
	}

	@Override
	public String toString() {
		return "Router [Hostname=" + hostname + ", IP Address=" + ipAddress + ", Patched=" + patched + ", OS Version="
				+ osVersion + ", Notes=" + notes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Router)) {
			return false;
		}
		Router other = (Router) obj;
		if (hostname == null) {
			if (other.hostname != null) {
				return false;
			}
		} else if (!hostname.equals(other.hostname)) {
			return false;
		}
		if (ipAddress == null) {
			if (other.ipAddress != null) {
				return false;
			}
		} else if (!ipAddress.equals(other.ipAddress)) {
			return false;
		}
		return true;
	}

	
	
	

}
