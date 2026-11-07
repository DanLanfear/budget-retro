import {useCallback, useEffect, useMemo, useState} from "react";
import {getTransactions} from "../api/transactionApi.ts";

export function TransactionsPage() {
    const [search, setSearch] = useState("");
    const [month, setMonth] = useState("");
    const [rows, setRows] = useState([]);

    const fetchData = useCallback(async () => {
        const data = await getTransactions();
        setRows(data);
    }, []);

    useEffect(() => {
        fetchData();
    }, [fetchData])

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

            {/*<DataTable rows={filtered}/>*/}
            <p className="table-footer">
                Showing {filtered.length} of {rows.length} records
            </p>
        </section>
    );
}