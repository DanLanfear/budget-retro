import {createApiClient} from "./apiClient.ts";
import type {Transaction} from "../types/transaction.tsx";


const transactionApi = createApiClient("/api/v1/transactions");


export async function getTransactions(): Promise<Transaction[]> {
    try {
        const response = await transactionApi.get();
        return response.data;
    } catch (error) {
        console.error("Error fetching transactions: ", error);
        throw error;
    }
}

/**
 * Process transaction csv file and return a list of transactions from it
 * @param file the file with all the transactions
 */
export async function processTransactionCsv(file: File): Promise<Transaction[]> {
    try {
        const payload = new FormData();
        payload.append("file", file);
        const response = await transactionApi.post('/upload/transactions', payload);
        return response.data;
    } catch (error) {
        console.error("Error processing file: ", error);
        throw error;
    }
}