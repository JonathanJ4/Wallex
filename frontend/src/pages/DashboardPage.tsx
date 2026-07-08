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
      {/* keep your current JSX here */}

      {/* update summary values like this */}
      <h3>${totalIncome.toFixed(2)}</h3>
      <h3>${totalExpenses.toFixed(2)}</h3>
      <h3>${netSavings.toFixed(2)}</h3>

      {/* transaction rows */}
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
    </main>
  );
}



export default DashboardPage;