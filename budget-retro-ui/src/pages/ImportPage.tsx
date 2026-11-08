import {useState} from "react";
import DataTable from "../components/DataTable.tsx";
import type {Transaction} from "../types/transaction.tsx";
import {processTransactionCsv} from "../api/transactionApi.ts";

function ImportPage() {
    const [preview, setPreview] = useState<Transaction[]>([]);
    const [filename, setFilename] = useState("");
    const [message, setMessage] = useState("");

    async function handleFile(file?: File) {
        if (!file) return;

        setMessage("");
        setFilename(file.name);

        try {
            const text = await file.text();
            let rows: Transaction[];

            if (file.name.toLowerCase().endsWith(".csv")) {
                const lines = text.trim().split(/\r?\n/);
                if (lines.length < 2) {
                    throw new Error("CSV must contain headers and at least one row.");
                }
                rows = await processTransactionCsv(file);
                setPreview(rows);
            } else {
                throw new Error("Please upload a .csv file.");
            }

            setPreview(rows);
        } catch (error) {
            setPreview([]);
            setMessage(
                error instanceof Error ? error.message : "Unable to read file."
            );
        }
    }

    function submit() {
        if (!preview.length) return;
        setMessage(`${preview.length} objects added.`);
        setPreview([]);
        setFilename("");
    }

    return (
        <section>
            <div className="page-heading">
                <div>
                    <h1>Import objects</h1>
                    <p>Upload, review, and submit your records.</p>
                </div>
            </div>

            <label className="upload-card">
                <span className="upload-icon"></span>
                {/*<span className="upload-icon"><FileUp size={28}/></span>*/}
                <strong>Choose a data file</strong>
                <span>CSV or JSON · Click to browse</span>
                <input
                    type="file"
                    accept=".csv,.json,application/json,text/csv"
                    onChange={(e) => handleFile(e.target.files?.[0])}
                />
            </label>

            {filename && <p className="file-name">Selected: {filename}</p>}
            {message && <p className="notice">{message}</p>}

            <div className="section-heading">
                <div>
                    <h2>Preview records</h2>
                    <p>{preview.length} records ready for review</p>
                </div>
                {preview.length > 0 && (
                    <button className="button-primary" onClick={submit}>
                        {/*<CheckCircle2 size={17}/>*/}
                        Submit {preview.length} objects
                    </button>
                )}
            </div>

            {preview.length > 0 ? (
                <DataTable rows={preview}/>
            ) : (
                <div className="empty-card">
                    Your uploaded objects will appear here for review.
                </div>
            )}
        </section>
    );
}

export default ImportPage;