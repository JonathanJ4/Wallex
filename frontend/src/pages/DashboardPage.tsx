import "./DashboardPage.css";

function DashboardPage(){

  return (
    <main className="dashboard-page">
      <aside className="sidebar">
        <h1 className="sidebar-logo">Wallex</h1>

        <nav className="sidebar-nav">
          <button className="nav-item active ">Dashboard</button>
          <button className="nav-item">Transactions</button>
          <button className="nav-item">Subscriptions</button>
        </nav>
      </aside>

      <section className="dashboard-content">
        <div className="dashboard-header">
          <div>
            <h2>Dashboard</h2>
            <p>Welcome back, Jonathan</p>
          </div>

          <button className="month-button">July 2026</button>
        </div>
          <section className="summary-grid">
          <div className="summary-card">
            <p>Total Income</p>
            <h3>$4,250.00</h3>
          </div>

          <div className="summary-card">
            <p>Total Expenses</p>
            <h3>$1,890.45</h3>
          </div>

          <div className="summary-card">
            <p>Net Savings</p>
            <h3>$2,359.55</h3>
          </div>
          </section>
        </section>
    </main>
  )
}