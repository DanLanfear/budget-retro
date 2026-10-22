import {useState} from "react";

function ImportPage({onSubmit,}: { onSubmit: (rows: Object[]) => void; }) {
    const [preview, setPreview] = useState<Object[]>([]);
    const [filename, setFilename] = useState("");
    const [message, setMessage] = useState("");

    async function handleFile(file?: File) {
        if (!file) return;

        setMessage("");
        setFilename(file.name);

        try {
            const text = await file.text();
            let rows: Object[];

            if (file.name.toLowerCase().endsWith(".json")) {
                const parsed = JSON.parse(text);
                if (!Array.isArray(parsed)) {
                    throw new Error("JSON must contain an array of objects.");
                }
                rows = parsed.map((item, i) => ({
                    id: String(item.id ?? `NEW-${i + 1}`),
                    name: String(item.name ?? ""),
                    category: String(item.category ?? ""),
                    date: String(item.date ?? ""),
                    status: String(item.status ?? "Pending"),
                }));
            } else if (file.name.toLowerCase().endsWith(".csv")) {
                const lines = text.trim().split(/\r?\n/);
                if (lines.length < 2) {
                    throw new Error("CSV must contain headers and at least one row.");
                }

                // Basic CSV parser for simple files without quoted commas.
                const headers = lines[0].split(",").map((h) =>
                    h.trim().toLowerCase()
                );

                rows = lines.slice(1).filter(Boolean).map((line, i) => {
                    const values = line.split(",");
                    const record = Object.fromEntries(
                        headers.map((header, index) => [
                            header,
                            (values[index] ?? "").trim(),
                        ])
                    );

                    return {
                        id: record.id || `NEW-${i + 1}`,
                        name: record.name || "",
                        category: record.category || "",
                        date: record.date || "",
                        status: record.status || "Pending",
                    };
                });
            } else {
                throw new Error("Please upload a .csv or .json file.");
            }

            // if (rows.some((row) => !row.name || !row.date)) {
            //     throw new Error("Every object must have a name and date.");
            // }

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
        onSubmit(preview);
        setMessage(`${preview.length} objects added to the local app state.`);
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

            {/*{preview.length > 0 ? (*/}
            {/*    <DataTable rows={preview}/>*/}
            {/*) : (*/}
            {/*    <div className="empty-card">*/}
            {/*        Your uploaded objects will appear here for review.*/}
            {/*    </div>*/}
            {/*)}*/}
        </section>
    );
}

export default ImportPage;