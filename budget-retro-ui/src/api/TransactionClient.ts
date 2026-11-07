import {createApiClient} from "./apiClient.ts";


const transactionClient = createApiClient("/api/transactions");


export async function getTransactions() {
    try {
        return transactionClient.get();
    } catch (error) {
        console.error("Error fetching transactions:", error);
        throw error;
    }
}