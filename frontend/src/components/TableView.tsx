interface TableViewProps {
  data: Record<string, any>[];
}

export const TableView = ({ data }: TableViewProps) => {
  const columns = Object.keys(data[0]).filter((key) => key !== "id");

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full border border-gray-300 rounded-lg overflow-hidden">
        <thead className="bg-[#a4ccd9] text-gray-800">
          <tr>
            {columns.map((col, colIndex) => (
              <th
                key={col}
                className={`py-2 px-4 border-b border-gray-200 text-left whitespace-nowrap ${
                  colIndex < columns.length - 1
                    ? "border-r-2 border-gray-200"
                    : ""
                }`}
              >
                {col}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((item, idx) => (
            <tr key={idx} className={idx % 2 === 0 ? "bg-white" : "bg-gray-50"}>
              {columns.map((col, colIndex) => (
                <td
                  key={col}
                  className={`py-2 px-4 border-b border-gray-200 whitespace-nowrap ${
                    colIndex < columns.length - 1
                      ? "border-r-2 border-gray-200"
                      : ""
                  }`}
                >
                  {typeof item[col] === "boolean"
                    ? item[col]
                      ? "Yes"
                      : "No"
                    : item[col]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
