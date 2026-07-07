import type { Transaction } from "../types/Transaction";

export const transactions: Transaction[] = [
  {
    id: 1,
    merchant: "Salary",
    category: "Income",
    amount: 2500,
    type: "Income",
    date: "2026-07-01",
  },
  {
    id: 2,
    merchant: "Tim Hortons",
    category: "Food",
    amount: 7.9,
    type: "Expense",
    date: "2026-07-04",
  },
  {
    id: 3,
    merchant: "Spotify",
    category: "Subscription",
    amount: 11.99,
    type: "Expense",
    date: "2026-07-02",
  },
  {
    id: 4,
    merchant: "Amazon",
    category: "Shopping",
    amount: 64.5,
    type: "Expense",
    date: "2026-07-03",
  },
  {
    id: 5,
    merchant: "Shell",
    category: "Transport",
    amount: 45.2,
    type: "Expense",
    date: "2026-06-30",
  },
];