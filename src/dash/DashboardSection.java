package dash;

import model.Agent;

/**
 * INTERFACE for the six sections of the main window (Agent, Properties, Clients,
 * Viewing, Deals, Reports).
 *
 * ABSTRACTION: MainDashboard only knows "a section can be given the agent and refreshed".
 * POLYMORPHISM: MainDashboard keeps the six panels in one DashboardSection[] array and calls
 *   refresh() on whichever is opened; each panel reloads its own records in its own way.
 */
public interface DashboardSection {

	/** Called once after login: whose records this section shows (null = Admin, all agents). */
	void setAgent(Agent agent);

	/** Reloads the records from the database and shows them. */
	void refresh();

	/** The title shown in the top bar. */
	String getPageTitle();
}
