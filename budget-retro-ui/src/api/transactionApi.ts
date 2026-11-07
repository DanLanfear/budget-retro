import {createApiClient} from "./apiClient.ts";


const transactionApi = createApiClient("/api/transactions");


export async function getTransactions() {
    try {
        return transactionApi.get();
    } catch (error) {
        console.error("Error fetching transactions:", error);
        throw error;
    }
}