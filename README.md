# HanapBahay

**A Real Estate Listing, Inquiry, and Sales Monitoring System** — Java Swing (Eclipse WindowBuilder) + MySQL (XAMPP).
Built from the group's project proposal (BSIT-2103, Group 4: Guce, Mañibo, Vergara) and the OOP project matrix,
on top of the `real-main2` code (UI member's dark-and-gold design, `dao.mysql` classes, `AppContext`, services).

## Who uses the system

- **Agents** log in and manage their properties, clients (buyers), viewings, deals and reports.
- **Buyers have no account.** In **Client Browse Mode** the agent hands the device to the buyer, who can only browse,
  search and filter *Available* properties and see their photos. Leaving the mode needs the agent's password.
- **Admin** (extra, not in the paper): an overview of all agents and the reports for all agents.

## Features (paper 3.1)

| Section | What it does |
|---|---|
| **Agent** | Profile (edit), cards for listings / clients / upcoming viewings / completed deals, next viewings |
| **Properties** | Add, edit, delete, view, search and filter listings (House and Lot, Vacant Lot, Condominium + subtypes). Up to 4 photos each: thumbnail in the table and a photo gallery |
| **Clients** | Register buyers with budget and preferences, search by name or contact number, **Find Matches** |
| **Viewing** | Schedule visits (client, property, date, time), mark Completed or Cancelled, no double booking |
| **Deals** | PAG-IBIG / Bank Financing / Cash. Pending → property Reserved, Completed → Sold, Cancelled → Available |
| **Reports** | Property Listings, Status Summary, Client Records, Viewing Schedule, Deal Records, Sales Summary, Payment Method Summary — **Print** or **Export CSV** |

## How the code is organized (matrix: GUI → business logic → data access → RDBMS)

```
login/          LoginFrame, RegisterDialog, BrandPanel                      (GUI, WindowBuilder)
dash/           MainDashboard, AdminDashboard                               (JFrames)
                AgentPanel, PropertiesPanel, ClientsPanel, ViewingPanel,
                DealsPanel, ReportsPanel, AdminOverviewPanel                (JPanels, implement DashboardSection)
                PropertyFormDialog, ClientFormDialog, ViewingFormDialog,
                DealFormDialog, ProfileDialog, PhotoGalleryDialog, MatchesDialog   (JDialogs)
                AppButton, AppTable, StatCard, HeroBanner, ... Theme        (reusable styled components)
com.hanapbahay.service   UserService, PropertyService, ClientService, ViewingService,
                         DealService, ReportGenerator, Report                (rules, validation, messages)
com.hanapbahay.dao       CrudDAO (interface), UserDao, PropertyDao, ClientDao, ViewingDao, DealDao, DBConnection
com.hanapbahay.dao.mysql MySqlUserDao, MySqlPropertyDao, MySqlClientDao, MySqlViewingDao, MySqlDealDao
com.hanapbahay.model     Person -> Agent, Client;  Property -> HouseAndLot, VacantLot, Condominium;
                         User, PropertyPhoto, Viewing, PaymentMethod, Deal
```

- **WindowBuilder:** every screen builds its components in a no-argument constructor (absolute layout in forms,
  Border/Grid/Flow/Card layouts in windows), with anonymous listener classes and no lambdas. Right-click a screen →
  **Open With → WindowBuilder Editor** → **Design** tab. Data is loaded only after login (`startSession`, `setAgent`,
  `refresh`), so the designer opens without the database.
- The screens never write SQL. They call a service through `AppContext`; the service checks the input and calls a DAO
  interface; the `dao.mysql` class runs the SQL with `PreparedStatement` (`?` placeholders block SQL injection).

## Four pillars of OOP (paper section IV / matrix B1–B4)

| Pillar | Where |
|---|---|
| **Encapsulation** | Private fields everywhere. `Property.setPrice()` refuses 0 or less, `setLotArea()` refuses negatives, `setStatus()` only accepts Available/Reserved/Sold. `User` has **no getter for the password hash** — only `checkPassword()` |
| **Inheritance** | `Agent`, `Client` extend `Person`; `HouseAndLot`, `VacantLot`, `Condominium` extend `Property`; `MySql…Dao` extend `MySqlBaseDao`; services extend `BaseService`; styled components extend Swing classes (`AppButton` IS-A `JButton`) |
| **Polymorphism** | `getDetails()` describes each property type differently (gallery, DAO). `MainDashboard` keeps six panels in one `DashboardSection[]` and calls `refresh()`. `CrudDAO` methods are implemented differently by each MySQL class. **Overloading:** `PropertyDao.search(String)` and `search(double, double)` |
| **Abstraction** | Abstract `Person` and `Property` (`getDetails()` is abstract). Interfaces `CrudDAO` and `DashboardSection` say *what* is done, not *how* |

## Validation (paper 5.3 / matrix E2)

Required fields · prices and budgets must be numbers above zero (`NumberFormatException` caught) · lot area not
below zero · login errors · at most 4 photos and each must be a real image · no second viewing for the same property
at the same date and time · no second active deal for a property · a property or client with viewings or deals cannot
be deleted · database problems show a message instead of crashing.

## Setup (each member)

1. **Pull** the `real-main32` branch in Eclipse, then **Refresh** (F5) and **Project → Clean**.
2. Start **MySQL** (and Apache) in the **XAMPP Control Panel**.
3. Open **http://localhost/phpmyadmin** → **Import** → choose `db/schema.sql` → **Import**.
   This **replaces** the old `hanapbahay` database with the paper's tables.
4. Run `src/com/hanapbahay/Main.java` (**Run As → Java Application**).

Test logins: `agent1 / agent123` (Maria Santos), `agent2 / agent123` (Paolo Reyes), `admin / admin123`.
Choose the matching **Account type**. Passwords are stored as SHA-256 hashes.

Photos added in the app are copied into the project's `photos/` folder; only their paths are saved in MySQL and the
folder is not uploaded to GitHub.

## Database tables (paper 3.4)

`users`, `agents`, `properties`, `property_photos`, `clients`, `viewings`, `payment_methods`, `deals` —
see `db/schema.sql` for every column, primary key and foreign key.
