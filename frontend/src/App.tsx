import { useState } from "react";

function App() {
  const [query, setQuery] = useState<string>("");
  const [activeQuery, setActiveQuery] = useState<string>("");
  const [history, setHistory] = useState<string[]>([]);

  const sendQuery = () => {
    console.log(query);
    setActiveQuery(query);
    {
      query && setHistory((prev) => [query, ...prev]);
    }
  };

  const historyClicked = (q: string) => {
    setActiveQuery(q);
    setQuery(q);
  };

  const clearHistory = () => {
    setHistory([]);
  };

  return (
    <>
      <div className="min-h-screen bg-[#ebefff] p-6">
        <div className="max-w-6xl mx-auto bg-[#f7f7f7] rounded-2xl shadow-lg p-6">
          <div className="text-3xl font-bold mb-6 text-center text-[#8dbcc7]">
            Ask about your insurance data
          </div>
          <div className="flex gap-4 mb-4">
            <input
              type="text"
              placeholder="Your query"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="w-full p-2 border bg-[#f9f9f9] border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#a4ccd9]"
            />
            <button
              onClick={sendQuery}
              className="bg-[#a4ccd9] text-white px-4 py-2 rounded-md shadow hover:bg-[#8dbcc7] cursor-pointer transition whitespace-nowrap"
            >
              Send your query
            </button>
          </div>
          <div className="bg-[#f3f3f3] p-4 h-80 rounded-lg shadow-inner mb-6">
            <div>Tu ce ic grafovi</div>
            {activeQuery && <div>Current query: {activeQuery}</div>}
          </div>
          <div className="flex items-center justify-center gap-4 mb-4">
            <div className="text-2xl font-bold text-[#8dbcc7]">
              Query history
            </div>
            <button
              onClick={clearHistory}
              className="text-xl text-red-500 hover:text-red-600 cursor-pointer transition"
            >
              (clear)
            </button>
          </div>
          <div className="space-y-2">
            {history.length > 0 &&
              history.map((q, index) => (
                <div
                  key={index}
                  onClick={() => historyClicked(q)}
                  className="bg-[#f9f9f9] p-2 rounded shadow text-[#333] cursor-pointer hover:bg-[#f0f0f0] transition"
                >
                  {q}
                </div>
              ))}
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
