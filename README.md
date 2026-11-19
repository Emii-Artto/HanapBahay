# HanapBahay

Real Estate Listing, Inquiry, and Sales Monitoring System (Java Swing + MySQL, Eclipse + WindowBuilder).

## Who owns what

| Package / folder | Owner | What goes there |
|---|---|---|
| `src/com/hanapbahay/ui` | **UI member** | WindowBuilder screens (JFrames, JPanels, dialogs) |
| `src/com/hanapbahay/service` | **Backend member** | Business rules: validation, login, recording sales, sales totals |
| `src/com/hanapbahay/dao`, `db/`, `lib/` | **Database member** | MySQL DAO classes, `schema.sql`, JDBC driver |
| `src/com/hanapbahay/model` | **Everyone (agree first)** | Shared data classes: `User`, `Property`, `Inquiry`, `Sale` |
| `AppContext.java` | Everyone | Connects the layers. Swap stub DAOs for MySQL DAOs here |

**Rule:** only edit files in your own area. If you need a change in someone else's area (e.g. a new DAO method), ask them or agree in the group chat first. This avoids Git merge conflicts.

## How the layers talk

```
UI (ui/)  ──calls──▶  Services (service/)  ──calls──▶  DAO interfaces (dao/)
                                                          ├─ stub/   fake data (works now)
                                                          └─ MySQL versions (Database member)
```

- The UI **never** writes SQL. It only calls `AppContext.userService()`, `AppContext.propertyService()`, etc.
- Services throw `IllegalArgumentException` with a user-friendly message. The UI shows it in a `JOptionPane`.
- Until the MySQL DAOs are done, the app runs on **stub data** (`dao/stub/`), which resets every run.

Test logins (stub data): `admin / admin123`, `agent1 / agent123`, `client1 / client123`

## First-time setup (each member)

1. In Eclipse: **File → Import → Git → Projects from Git → Clone URI**, paste the GitHub repo URL, and finish the wizard.
2. Run `src/com/hanapbahay/Main.java` (**Run As → Java Application**). The login screen should appear.
3. When the team switches to MySQL:
   - Install MySQL (or XAMPP) and run `db/schema.sql`.
   - Copy `config/db.properties.example` to `config/db.properties` and fill in your own MySQL password. This file is **not** pushed to GitHub.

## Daily workflow

1. **Pull** first (right-click project → Team → Pull).
2. Work on your part and test it.
3. **Commit** with a clear message (Team → Commit), then **Push**.

## Database member: switching to MySQL

1. Add the MySQL Connector/J jar (see `lib/README.txt`).
2. Create classes such as `dao/mysql/MySqlUserDao implements UserDao`, using `DBConnection.getConnection()`.
3. In `AppContext.java`, replace `new StubUserDao()` with `new MySqlUserDao()` (one DAO at a time is fine).
