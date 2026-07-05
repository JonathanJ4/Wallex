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
          <section className="main-grid">
          <div className="glass-card chart-card">
            <div className="card-header">
              <h3>Income vs Expenditure</h3>
              <button>Monthly</button>
            </div>

            <div className="fake-chart">
              <div className="bar income" style={{ height: "70%" }}></div>
              <div className="bar expense" style={{ height: "45%" }}></div>
              <div className="bar income" style={{ height: "85%" }}></div>
              <div className="bar expense" style={{ height: "55%" }}></div>
              <div className="bar income" style={{ height: "60%" }}></div>
              <div className="bar expense" style={{ height: "35%" }}></div>
            </div>
          </div>

          <div className="glass-card month-card">
            <h3>This Month</h3>
            <p className="big-number">$1,890.45</p>
            <span>Total spending</span>
          </div>
          
        </section>
          <section className="glass-card transactions-card">
          <div className="card-header">
            <h3>Recent Transactions</h3>
            <button>View All</button>
          </div>

          <div className="transaction-row table-head">
            <span>Date</span>
            <span>Merchant</span>
            <span>Category</span>
            <span>Type</span>
            <span>Amount</span>
          </div>

          <div className="transaction-row">
            <span>Jul 4, 2026</span>
            <span>Tim Hortons</span>
            <span>Food</span>
            <span>Expense</span>
            <span className="expense-text">-$7.90</span>
          </div>

          <div className="transaction-row">
            <span>Jul 3, 2026</span>
            <span>Salary</span>
            <span>Income</span>
            <span>Income</span>
            <span className="income-text">+$2,500.00</span>
          </div>

          <div className="transaction-row">
            <span>Jul 2, 2026</span>
            <span>Spotify</span>
            <span>Subscription</span>
            <span>Expense</span>
            <span className="expense-text">-$11.99</span>
          </div>
         </section>
        </section>
    </main>
  )
}

export default DashboardPage;