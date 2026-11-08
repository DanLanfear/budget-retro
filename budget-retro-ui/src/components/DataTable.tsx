import type {Transaction} from "../types/transaction.tsx";

function DataTable({rows}: { rows: Transaction[] }) {
    return (
        <div className="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Description</th>
                    <th>Date</th>
                    <th>Category</th>
                </tr>
                </thead>
                <tbody>
                {rows.map((item) => (
                    <tr key={item.id}>
                        <td className="cell-name">{item.description}</td>
                        <td>{item.date}</td>
                        <td>{item.category}</td>
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

export default DataTable;