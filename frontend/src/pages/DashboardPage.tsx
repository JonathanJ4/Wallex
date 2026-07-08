import { useEffect, useState } from "react";
import "./DashboardPage.css";
import type { Transaction } from "../types/Transaction";

function DashboardPage() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/transactions")
      .then((response) => response.json())
      .then((data) => setTransactions(data))
      .catch((error) => console.error("Failed to fetch transactions", error));
  }, []);

  const totalIncome = transactions
    .filter((transaction) => transaction.type === "Income")
    .reduce((sum, transaction) => sum + transaction.amount, 0);

  const totalExpenses = transactions
    .filter((transaction) => transaction.type === "Expense")
    .reduce((sum, transaction) => sum + transaction.amount, 0);

  const netSavings = totalIncome - totalExpenses;

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

           {transactions.map((transaction) => (
        <div className="transaction-row" key={transaction.id}>
          <span>{transaction.transactionDate}</span>
          <span>{transaction.merchant}</span>
          <span>{transaction.category}</span>
          <span>{transaction.type}</span>
          <span
            className={
              transaction.type === "Income" ? "income-text" : "expense-text"
            }
          >
            {transaction.type === "Income" ? "+" : "-"}$
            {transaction.amount.toFixed(2)}
          </span>
        </div>
      ))}
        </section>
      </section>
    </main>
  );
}

export default DashboardPage;