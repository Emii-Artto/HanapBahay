package model;

/**
 * 5.1.2 Agent - the real estate agent who uses the system.
 * INHERITANCE: an Agent IS-A Person. It adds the agent ID and the user ID
 * that links the agent to a login account (User).
 */
public class Agent extends Person {

	private int agentId;
	private int userId;

	public Agent(int agentId, int userId, String firstName, String lastName, String contactNumber, String email) {
		super(firstName, lastName, contactNumber, email); // the Person constructor fills the shared fields
		this.agentId = agentId;
		this.userId = userId;
	}

	@Override
	public String getRoleLabel() {
		return "Real Estate Agent";
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
