import {createApiClient} from "./apiClient.ts";
import type {Transaction} from "../types/transaction.tsx";


const transactionApi = createApiClient("/api/v1/transactions");


export async function getTransactions(): Promise<Transaction[]> {
    try {
        const response = await transactionApi.get();
        return response.data;
    } catch (error) {
        console.error("Error fetching transactions:", error);
        throw error;
    }
}