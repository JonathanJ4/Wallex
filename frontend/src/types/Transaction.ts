export interface Transaction{
    id: number;
    merchant: string;
    category: string;
    amount: number;
    type: "Income" | "Expense";
    date: string;
}