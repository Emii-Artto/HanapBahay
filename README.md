# HanapBahay

Real Estate Listing, Inquiry, and Sales Monitoring System (Java Swing + MySQL, Eclipse + WindowBuilder).

## Who owns what

| Package / folder | Owner | What goes there |
|---|---|---|
| `src/com/hanapbahay/ui` | **UI member** | WindowBuilder screens (JFrames, JPanels, dialogs) |
| `src/com/hanapbahay/service` | **Backend member** | Business rules: login, validation, recording sales, sales totals |
| `src/com/hanapbahay/dao`, `db/`, `lib/` | **Database member** | MySQL classes, `schema.sql`, JDBC driver |
| `src/com/hanapbahay/model` | **Everyone (agree first)** | Shared classes: `User` (`Admin`, `Agent`, `Client`), `Property`, `Inquiry`, `Sale` |
| `AppContext.java` | Everyone | Connects the layers. Swap sample DAOs for MySQL DAOs here |

**Rule:** only edit files in your own area. If you need a change in someone else's area, ask them first. This avoids Git merge conflicts.

## How the layers talk

```
UI (ui/)  ──calls──▶  Services (service/)  ──calls──▶  DAO interfaces (dao/)
                                                          ├─ sample/  data in memory (works now)
                                                          └─ MySQL classes (Database member)
```

- The UI **never** writes SQL. It only calls `AppContext.userService()`, `AppContext.propertyService()`, etc.
- Service methods return `true`/`false` (or `null` for login). The reason is in `getMessage()`, which the UI shows in a `JOptionPane`.
- Until the MySQL classes are done, the app runs on **sample data** (`dao/sample/`), which resets every run.

Test logins (sample data): `admin / admin123`, `agent1 / agent123`, `client1 / client123`

## Where each OOP concept is used (Modules 3 to 5)

| Concept | Module | Where to find it |
|---|---|---|
| Class, object, `new` | 3 | Every class; objects created in `Main`, `AppContext`, `SamplePropertyDao` |
| Constructor, `this` | 3 | `Property`, `User`, every service constructor |
| Constructor chaining `this(...)` | 3 | `Property(title, location, type, price, agentId)`, `Inquiry(propertyId, clientId, message)` |
| Access modifiers (`public`, `private`, `protected`) | 3 | `BaseService.message` is `protected`; `InquiryService.changeStatus()` is `private` |
| Static variables and methods | 3 | `AppContext` (shared services), `DBConnection.getConnection()` |
| Return types (`boolean`, `double`, objects, `void`) | 3 | Service methods, e.g. `addListing()` returns `boolean`, `getTotalSales()` returns `double` |
| Encapsulation (private fields + getters/setters) | 4 | All model classes; `Property.setPrice()` validates like `setAge()` in the slides |
| Inheritance (`extends`) | 4 | `Admin`, `Agent`, `Client` extend `User`; all services extend `BaseService` |
| `super(...)` | 4 | `Admin`, `Agent`, `Client` constructors |
| Method overriding (`@Override`) | 4, 5 | `getRole()`, `canManageListings()`, `canViewAllSales()` in `Admin`, `Agent`, `Client`; `toString()` in `User` and `Property` (shown in drop-downs) |
| `final` constants | 4 | `Property.AVAILABLE/RESERVED/SOLD`, `UserService.MIN_PASSWORD_LENGTH`, `DBConnection.URL` |
| Method overloading (compile-time polymorphism) | 5 | `PropertyService.searchListings(...)` x2, `SaleService.getTotalSales(...)` x2 |
| Runtime polymorphism | 5 | `User user = new Admin(...)`; `user.getWelcomeMessage()` runs the subclass's `getRole()`. Screens call `user.canManageListings()` to show the CRUD form only to Agents/Admins, and `user.canViewAllSales()` so an Admin sees all sales and an Agent only their own |
| Abstract class and abstract method | 5 | `User` with `abstract String getRole()` |
| Interface (`implements`) | 5 | `UserDao`, `PropertyDao`, `InquiryDao`, `SaleDao`, implemented by the `Sample...Dao` classes |
| Exception handling (`try`/`catch`) | Project matrix | `BaseService.toNumber()` catches `NumberFormatException` when a price isn't a number; `DBConnection.getConnection()` catches `SQLException` |

## Screens (`ui/`)

`MainFrame` is one window with a dark sidebar menu, a header (page title + role badge) and a page area.
Clicking a menu button shows one of the `...Panel` pages inside the page area.
All colors and fonts come from `Theme.java`, so every screen looks the same.

| File | Type | Who sees it | What it does |
|---|---|---|---|
| `Theme` | constants | (all screens) | Shared colors, fonts, field border and table style |
| `LoginFrame` | window | Everyone | Log in, or open "Create one" |
| `RegisterFrame` | window | New users | Create a Client account (checks blanks, length, matching passwords, duplicates) |
| `MainFrame` | window | Everyone | Sidebar menu + header + page area. Sales is hidden from Clients |
| `DashboardPanel` | page | Everyone | 5 summary cards (available, reserved, sold, inquiries, sales total) and a listings table |
| `ListingsPanel` | page | Everyone | Search by title/location, filter by price. Agents/Admins: add, update, delete. Clients: send an inquiry |
| `InquiriesPanel` | page | Everyone | Agents/Admins: all inquiries, filter by status, mark responded/closed. Clients: "My inquiries" |
| `SalesPanel` | page | Agents, Admins | Record a sale (property becomes SOLD), summary totals, and the sales report |

## First-time setup (each member)

1. In Eclipse: **File → Import → Git → Projects from Git → Clone URI**, paste the GitHub repo URL, and finish the wizard.
2. Run `src/com/hanapbahay/Main.java` (**Run As → Java Application**). The login screen should appear.
3. When the team switches to MySQL: install MySQL (or XAMPP), run `db/schema.sql`, and set your MySQL user/password in `DBConnection.java`.

## Daily workflow

1. **Pull** first (right-click project → Team → Pull).
2. Work on your part and test it.
3. **Commit** with a clear message (Team → Commit), then **Push**.

## Database member: switching to MySQL

1. Add the MySQL Connector/J jar (see `lib/README.txt`).
2. Create classes such as `dao/mysql/MySqlUserDao implements UserDao`, using `DBConnection.getConnection()`.
   Read the `role` column to create the right object: `new Admin(...)`, `new Agent(...)` or `new Client(...)`.
3. In `AppContext.java`, replace `new SampleUserDao()` with `new MySqlUserDao()` (one at a time is fine).
