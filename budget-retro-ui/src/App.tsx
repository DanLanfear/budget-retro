import {useMemo, useState} from "react";
import {BrowserRouter, Route, Routes,} from "react-router-dom";
// import {CalendarDays, CheckCircle2, FileUp, Search,} from "lucide-react";
import "./index.css";
import Navbar from "./components/Navbar.tsx";

type DataObject = {
    id: string;
    name: string;
    category: string;
    date: string;
    status: string;
};

const initialObjects: DataObject[] = [
    {
        id: "OBJ-001",
        name: "Object Alpha",
        category: "Type A",
        date: "2026-01-12",
        status: "Active",
    },
    {
        id: "OBJ-002",
        name: "Object Beta",
        category: "Type B",
        date: "2026-02-18",
        status: "Active",
    },
    {
        id: "OBJ-003",
        name: "Object Gamma",
        category: "Type A",
        date: "2026-02-25",
        status: "Pending",
    },
];

function DataTable({rows}: { rows: DataObject[] }) {
    return (
        <div className="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                {rows.map((item) => (
                    <tr key={item.id}>
                        <td className="muted">{item.id}</td>
                        <td className="cell-name">{item.name}</td>
                        <td>{item.category}</td>
                        <td>{item.date}</td>
                        <td>
                <span
                    className={`status ${
                        item.status === "Active" ? "active" : "pending"
                    }`}
                >
                  {item.status}
                </span>
                        </td>
                    </tr>
                ))}
                {rows.length === 0 && (
                    <tr>
                        <td colSpan={5} className="empty">
                            No objects found.
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

function ImportPage({onSubmit,}: { onSubmit: (rows: DataObject[]) => void; }) {
    const [preview, setPreview] = useState<DataObject[]>([]);
    const [filename, setFilename] = useState("");
    const [message, setMessage] = useState("");

    async function handleFile(file?: File) {
        if (!file) return;

        setMessage("");
        setFilename(file.name);

        try {
            const text = await file.text();
            let rows: DataObject[];

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

            if (rows.some((row) => !row.name || !row.date)) {
                throw new Error("Every object must have a name and date.");
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

function ObjectsPage({rows}: { rows: DataObject[] }) {
    const [search, setSearch] = useState("");
    const [month, setMonth] = useState("");

    const filtered = useMemo(() => {
        return rows.filter((item) => {
            const matchesSearch = [
                item.id, item.name, item.category, item.status,
            ].some((value) =>
                value.toLowerCase().includes(search.toLowerCase())
            );

            const matchesMonth = !month || item.date.startsWith(month);
            return matchesSearch && matchesMonth;
        });
    }, [rows, search, month]);

    return (
        <section>
            <div className="page-heading">
                <div>
                    <h1>All objects</h1>
                    <p>Browse and filter your saved records.</p>
                </div>
                <span className="record-count">{rows.length} total records</span>
            </div>

            <div className="filters">
                <label className="search-box">
                    {/*<Search size={18}/>*/}
                    <input
                        placeholder="Search objects..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </label>

                <label className="month-filter">
                    {/*<CalendarDays size={17}/>*/}
                    <input
                        type="month"
                        value={month}
                        onChange={(e) => setMonth(e.target.value)}
                    />
                </label>
            </div>

            <DataTable rows={filtered}/>
            <p className="table-footer">
                Showing {filtered.length} of {rows.length} records
            </p>
        </section>
    );
}

function DashboardPage({rows}: { rows: DataObject[] }) {
    return (
        <section>
            <div className="page-heading">
                <div>
                    <h1>Dashboard</h1>
                    <p>Your data at a glance.</p>
                </div>
            </div>
            <div className="stats-grid">
                <div className="stat-card">
                    <span>Total objects</span>
                    <strong>{rows.length}</strong>
                </div>
                <div className="stat-card">
                    <span>Active objects</span>
                    <strong>{rows.filter((r) => r.status === "Active").length}</strong>
                </div>
                <div className="stat-card">
                    <span>Pending objects</span>
                    <strong>{rows.filter((r) => r.status === "Pending").length}</strong>
                </div>
            </div>
            <h2>Recent objects</h2>
            <DataTable rows={[...rows].slice(-5).reverse()}/>
        </section>
    );
}

function App() {
    const [objects, setObjects] = useState(initialObjects);

    function addObjects(newRows: DataObject[]) {
        setObjects((previous) => [...previous, ...newRows]);
    }

    return (
        <BrowserRouter>
            <div className="app-shell">
                <Navbar/>
                <main className="main-content">
                    <Routes>
                        <Route
                            path="/"
                            element={<ImportPage onSubmit={addObjects}/>}
                        />
                        <Route
                            path="/objects"
                            element={<ObjectsPage rows={objects}/>}
                        />
                        <Route
                            path="/dashboard"
                            element={<DashboardPage rows={objects}/>}
                        />
                    </Routes>
                </main>
            </div>
        </BrowserRouter>
    );
}

export default App;
