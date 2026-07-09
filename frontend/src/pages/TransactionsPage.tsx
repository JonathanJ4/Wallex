import { useEffect, useMemo, useState } from "react";
import type { ChangeEvent } from "react";
import type { Transaction } from "../types/Transaction";
import "./TransactionsPage.css";
function TransactionsPage(){

  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [searchText, setSearchText] = useState("");
  const [uploadMessage, setUploadMessage] = useState("");
  const [isUploading, setIsUploading] = useState(false);

  useEffect(() => {
    fetchTransactions();
  }, []);

  function fetchTransactions() {
    fetch("http://localhost:8080/transactions")
      .then((response) => response.json())
      .then((data) => setTransactions(data))
      .catch((error) => {
        console.error("Failed to fetch transactions", error);
      });
  }

  function handleFileChange(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];

    if (!file) {
      return;
    }

    setSelectedFile(file);
    setUploadMessage("");
  }

  function handleUpload() {
    if (!selectedFile) {
      setUploadMessage("Please choose a PDF file first.");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedFile);

    setIsUploading(true);
    setUploadMessage("Uploading PDF...");

    fetch("http://localhost:8080/transactions/import/pdf/import", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("PDF upload failed");
        }

        return response.json();
      })
      .then(() => {
        setUploadMessage("PDF imported successfully.");
        setSelectedFile(null);
        fetchTransactions();
      })
      .catch((error) => {
        console.error(error);
        setUploadMessage("Failed to import PDF.");
      })
      .finally(() => {
        setIsUploading(false);
      });
  }

  const filteredTransactions = useMemo(() => {
    const search = searchText.toLowerCase();

    return transactions.filter((transaction) => {
      return (
        transaction.merchant.toLowerCase().includes(search) ||
        (transaction.category ?? "").toLowerCase().includes(search) ||
        transaction.type.toLowerCase().includes(search) ||
        transaction.description.toLowerCase().includes(search) ||
        transaction.transactionDate.toLowerCase().includes(search) ||
        transaction.amount.toString().includes(search)
      );
    });
  }, [transactions, searchText]);

  return (
    <main className="transactions-page">
      <aside className="transactions-sidebar">
        <h1 className="transactions-logo">WALLEX</h1>

        <nav className="transactions-nav">
          <a href="/dashboard">Dashboard</a>
          <a href="/transactions" className="active">
            Transactions
          </a>
          <a href="#">Budgets</a>
          <a href="#">Categories</a>
          <a href="#">Reports</a>
          <a href="#">AI Assistant</a>
          <a href="#">Settings</a>
        </nav>
      </aside>

      <section className="transactions-content">
        <header className="transactions-header">
          <div>
            <h2>Transactions</h2>
            <p>Upload statements, search, and review your spending history.</p>
          </div>
        </header>

        <section className="upload-card">
          <div>
            <h3>Import PDF Statement</h3>
            <p>Select a bank statement PDF and import its transactions.</p>
          </div>

          <div className="upload-actions">
            <label className="file-label">
              Choose PDF
              <input type="file" accept=".pdf" onChange={handleFileChange} />
            </label>

            <button
              className="upload-button"
              onClick={handleUpload}
              disabled={isUploading}
            >
              {isUploading ? "Uploading..." : "Upload"}
            </button>
          </div>

          {selectedFile && (
            <p className="selected-file">Selected: {selectedFile.name}</p>
          )}

          {uploadMessage && <p className="upload-message">{uploadMessage}</p>}
        </section>

        <section className="transactions-table-card">
          <div className="transactions-table-header">
            <div>
              <h3>All Transactions</h3>
              <p>{filteredTransactions.length} transactions found</p>
            </div>

            <input
              className="search-input"
              type="text"
              placeholder="Search transactions..."
              value={searchText}
              onChange={(event) => setSearchText(event.target.value)}
            />
          </div>

          <div className="transaction-row table-head">
            <span>Date</span>
            <span>Merchant</span>
            <span>Category</span>
            <span>Type</span>
            <span>Description</span>
            <span>Amount</span>
          </div>

          {filteredTransactions.map((transaction) => (
            <div className="transaction-row" key={transaction.id}>
              <span>{transaction.transactionDate}</span>
              <span>{transaction.merchant}</span>
              <span>{transaction.category}</span>
              <span>{transaction.type}</span>
              <span>{transaction.description}</span>
              <span
                className={
                  transaction.type === "Income"
                    ? "income-text"
                    : "expense-text"
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

export default TransactionsPage;


