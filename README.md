# HanapBahay

Real Estate Listing, Inquiry, and Sales Monitoring System (Java Swing + MySQL, Eclipse + WindowBuilder).

## Progress

| Week | Status | What was done |
|---|---|---|
| 1 | Done | Project structure, model classes, database schema, user-account backend (login and register rules), Login screen, main window with account dashboard |
| 2 | Next | Property listings: search, filter, add, update, delete |
| 3 | Planned | Client inquiries, Register screen |
| 4 | Planned | Sales recording and sales report |
| 5 | Planned | MySQL connection for all modules, testing |

## Who owns what

| Package / folder | Owner | What goes there |
|---|---|---|
| `src/com/hanapbahay/ui` | **UI member** | WindowBuilder screens (JFrames, JPanels) and `Theme` (colors and fonts) |
| `src/com/hanapbahay/service` | **Backend member** | Business rules: login, validation, recording sales, sales totals |
| `src/com/hanapbahay/dao`, `db/`, `lib/` | **Database member** | DAO interfaces, MySQL classes, `schema.sql`, JDBC driver |
| `src/com/hanapbahay/model` | **Everyone (agree first)** | Shared classes: `User` (`Admin`, `Agent`, `Client`), `Property`, `Inquiry`, `Sale` |
| `AppContext.java` | Everyone | Connects the layers. Swap sample DAOs for MySQL DAOs here |

**Rule:** only edit files in your own area. If you need a change in someone else's area, ask them first. This avoids Git merge conflicts.

## How the layers talk

```
UI (ui/)  ──calls──▶  Services (service/)  ──calls──▶  DAO interfaces (dao/)
                                                          ├─ sample/  data in memory (works now)
                                                          └─ MySQL classes (Database member)
```

- The UI **never** writes SQL. It only calls `AppContext.userService()` (more services are added each week).
- Service methods return `true`/`false` (or `null` for login). The reason is in `getMessage()`, which the UI shows in a `JOptionPane`.
- Until the MySQL classes are done, the app runs on **sample data** (`dao/sample/`), which resets every run.

Test logins (sample data): `admin / admin123`, `agent1 / agent123`, `client1 / client123`

## Week 1: where each OOP concept is used

| Concept | Module | Where to find it |
|---|---|---|
| Class, object, `new` | 3 | Every class; objects created in `Main`, `AppContext`, `SampleUserDao` |
| Constructor, `this` | 3 | `User`, `Property`, `UserService` |
| Constructor chaining `this(...)` | 3 | `Property(title, location, type, price, agentId)`, `Inquiry(propertyId, clientId, message)` |
| Access modifiers (`public`, `private`, `protected`) | 3 | `BaseService.message` is `protected`; model fields are `private` |
| Static variables and methods | 3 | `AppContext.userService()`, `DBConnection.getConnection()` |
| Encapsulation (private fields + getters/setters) | 4 | All model classes; `Property.setPrice()` validates like `setAge()` in the slides |
| Inheritance (`extends`) | 4 | `Admin`, `Agent`, `Client` extend `User`; `UserService` extends `BaseService` |
| `super(...)` | 4 | `Admin`, `Agent`, `Client` constructors |
| `final` constants | 4 | `Property.AVAILABLE/RESERVED/SOLD`, `UserService.MIN_PASSWORD_LENGTH`, `Theme` colors |
| Method overriding (`@Override`) | 4, 5 | `getRole()`, `canManageListings()`, `canViewAllSales()` in `Admin`, `Agent`, `Client` |
| Runtime polymorphism | 5 | After login, the Dashboard calls `user.getRole()`, `user.canManageListings()` and `user.canViewAllSales()`; each role gives a different answer |
| Abstract class and abstract method | 5 | `User` with `abstract String getRole()` |
| Interface (`implements`) | 5 | `UserDao`, `PropertyDao`, `InquiryDao`, `SaleDao`; `SampleUserDao implements UserDao` |

## First-time setup (each member)

1. In Eclipse: **File → Import → Git → Projects from Git → Clone URI**, paste the GitHub repo URL, and finish the wizard.
2. Run `src/com/hanapbahay/Main.java` (**Run As → Java Application**). The login screen should appear.
3. When the team switches to MySQL: install MySQL (or XAMPP), run `db/schema.sql`, and set your MySQL user/password in `DBConnection.java`.

## Daily workflow

1. **Pull** first (right-click project → Team → Pull), then **Refresh** (F5) so Eclipse sees new files.
2. Work on your part and test it.
3. **Commit** with a clear message (Team → Commit), then **Push**.

## Database member: switching to MySQL

1. Add the MySQL Connector/J jar (see `lib/README.txt`).
2. Create classes such as `dao/mysql/MySqlUserDao implements UserDao`, using `DBConnection.getConnection()`.
   Read the `role` column to create the right object: `new Admin(...)`, `new Agent(...)` or `new Client(...)`.
3. In `AppContext.java`, replace `new SampleUserDao()` with `new MySqlUserDao()`.
