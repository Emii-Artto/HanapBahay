# HanapBahay

**A Real Estate Listing, Inquiry, and Sales Monitoring System** (Java Swing + MySQL, Eclipse + WindowBuilder).
Built from the group's project proposal (BSIT-2103, Group 4: Guce, Mañibo, Vergara).

## Who uses the system

- **Agents** log in and manage their properties, clients (buyers), viewings, deals and reports.
- **Buyers have no account.** In **Client Browse Mode** the agent hands the device to the buyer, who can only browse,
  search and filter *Available* properties and see their photos. Leaving the mode needs the agent's password.
- **Admin** (extra, not in the paper): sees an overview of all agents and the reports for all agents.

## Features (paper section 3.1)

| Section | What it does |
|---|---|
| **Agent** | Agent profile (edit), cards for listings / clients / upcoming viewings / completed deals, next viewings |
| **Properties** | Add, edit, delete, view, search and filter listings. Types: House and Lot, Vacant Lot, Condominium, each with subtypes. Up to 4 photos per property, shown as a thumbnail and in a photo gallery |
| **Clients** | Register buyers with budget and preferences, search by name or contact number, **Find Matches** |
| **Viewing** | Schedule visits (client, property, date, time), mark Completed or Cancelled. No double booking |
| **Deals** | Record deals with payment method (PAG-IBIG, Bank Financing, Cash). Pending → property Reserved, Completed → property Sold, Cancelled → Available |
| **Reports** | Property Listings, Status Summary, Client Records, Viewing Schedule, Deal Records, Sales Summary, Payment Method Summary. **Print** or **Export CSV** |

## How the code is organized

```
ui/       LoginFrame, MainDashboard, AdminDashboard   (JFrames, extend BaseFrame)
          AgentPanel, PropertiesPanel, ClientsPanel,
          ViewingPanel, DealsPanel, ReportsPanel      (JPanels, extend BasePanel)
          PhotoGalleryDialog, Theme
service/  UserService, PropertyService, ClientService, ViewingService, DealService,
          ReportGenerator, Report                      (rules, validation, messages)
dao/      CrudDAO (interface), PropertyDAO, ClientDAO, ViewingDAO, DealDAO, UserDAO, DBConnection
model/    Person (abstract) -> Agent, Client
          Property (abstract) -> HouseAndLot, VacantLot, Condominium
          User, PropertyPhoto, Viewing, PaymentMethod, Deal
```

- The screens never write SQL. They call a service through `AppContext`, the service checks the input, and the
  DAO talks to MySQL with `PreparedStatement` (`?` placeholders, so typed text cannot change the SQL).
- A service returns `true`/`false` (or `null`); `getMessage()` explains the problem in plain words.

## Four pillars of OOP (paper section IV)

| Pillar | Where |
|---|---|
| **Encapsulation** | All fields are private. `Property.setPrice()` refuses 0 or less, `setLotArea()` refuses negative values, `setStatus()` only accepts Available/Reserved/Sold. `User` has **no getter for the password hash**; it only offers `checkPassword()` |
| **Inheritance** | `Agent` and `Client` extend `Person`. `HouseAndLot`, `VacantLot`, `Condominium` extend `Property`. Panels extend `BasePanel` (a `JPanel`); dashboards extend `BaseFrame` (a `JFrame`). Services extend `BaseService`, DAOs extend `BaseDAO` |
| **Polymorphism** | `getDetails()` gives a different description for each property type; the gallery and the DAO work with one `Property` type. `MainDashboard` keeps six different panels in one `BasePanel[]` and calls `refresh()`. **Overloading:** `PropertyDAO.search(String keyword)` and `search(double min, double max)` |
| **Abstraction** | `Person` and `Property` are abstract; `Property` forces `getDetails()`. The `CrudDAO` interface defines add, update, delete, getAll without saying how |

## Validation (paper section 5.3)

Required fields · prices and budgets must be numbers above zero (`NumberFormatException` is caught) · lot area not
below zero · login error message · at most 4 photos, and each file must be a real image · no second viewing for the
same property at the same date and time · no second active deal for a property · a property or client with viewings
or deals cannot be deleted · database problems show a message instead of crashing.

## Setup (each member)

1. **Pull** the `real-main32` branch in Eclipse, then **Refresh** (F5) and **Project → Clean**.
2. Start **MySQL** (and Apache) in the **XAMPP Control Panel**.
3. Open **http://localhost/phpmyadmin** → **Import** → choose `db/schema.sql` → **Import**.
   This **replaces** the old `hanapbahay` database with the new tables from the paper.
4. Run `src/com/hanapbahay/Main.java` (**Run As → Java Application**).

Test logins: `agent1 / agent123` (Maria Santos), `agent2 / agent123` (Paolo Reyes), `admin / admin123`.
Choose the matching **Account type** on the login screen. Passwords are stored as SHA-256 hashes.

Photos added in the app are copied into the `photos/` folder of the project. Only their paths are saved in MySQL,
and the folder is not uploaded to GitHub, so photos stay on the computer where they were added.

## Database tables (paper section 3.4)

`users`, `agents`, `properties`, `property_photos`, `clients`, `viewings`, `payment_methods`, `deals`.
See `db/schema.sql` for every column and the foreign keys.
