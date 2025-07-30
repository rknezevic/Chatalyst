import { useState } from "react";
import type { DataEntry, HistoryEntry, ChatHistory, ViewType } from "./types";
import { ChartView } from "./components/ChartView";
import { TableView } from "./components/TableView";

function App() {
  const [query, setQuery] = useState<string>("");
  const [activeQuery, setActiveQuery] = useState<string>("");
  const [history, setHistory] = useState<ChatHistory[]>([]);
  const [data, setData] = useState<DataEntry[] | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [firstQueryInChat, setfirstQueryInChat] = useState(true);
  const [convId, setConvId] = useState<string | null>(null);
  const [sqlQuery, setSqlQuery] = useState<string>("");

  const sendQuery = async () => {
    if (!query) return;
    setActiveQuery(query);
    setIsLoading(true);

    try {
      /*const response = await fetch("/api/chat-with-memory", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          input: query,
          conversationId: convId,
        }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error ${response.status}`);
      }*/

      const response = await fetch("http://localhost:3000/chatResponse");
      const json = await response.json();

      const newEntry: HistoryEntry = {
        query,
        time: new Date(),
        data: json.data,
        type: json.llmrEsponse.chartTypea as ViewType,
        conversationId: json.conversationId ?? convId,
        sqlQuery: json.llmrEsponse.sqlQuery ?? null,
      };

      setData(json.data);
      setSqlQuery(json.llmrEsponse.sqlQuery);
      setConvId(json.conversationId ?? convId);

      setHistory((prev) => {
        if (firstQueryInChat || prev.length === 0) {
          return [
            {
              initialQuery: query,
              entries: [newEntry],
            },
            ...prev,
          ];
        } else {
          const [latestChat, ...rest] = prev;
          return [
            {
              ...latestChat,
              entries: [...latestChat.entries, newEntry],
            },
            ...rest,
          ];
        }
      });
      setfirstQueryInChat(false);
    } catch (err) {
      console.error("Error getting data:", err);
    } finally {
      setIsLoading(false);
    }
  };

  const newQuery = async () => {
    setQuery("");
    setActiveQuery("");
    setData(null);
    setSqlQuery("");
    setfirstQueryInChat(true);
    setConvId(null);
  };

  const historyClicked = (chat: ChatHistory) => {
    const lastEntry = chat.entries[chat.entries.length - 1];

    setQuery(lastEntry.query);
    setActiveQuery(lastEntry.query);
    setData(lastEntry.data);
    setSqlQuery(lastEntry.sqlQuery ?? "");
    setConvId(lastEntry.conversationId);
    setfirstQueryInChat(false);
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
          <div className="flex gap-4 mb-2">
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
          <div className="flex justify-center mb-2">
            <button
              onClick={newQuery}
              className="bg-[#a4ccd9] text-white px-16 py-2 rounded-md shadow hover:bg-[#8dbcc7] cursor-pointer transition"
            >
              New chat
            </button>
          </div>
          <div className="bg-[#f3f3f3] p-4 rounded-lg shadow-inner mb-6">
            {isLoading ? (
              <div className="text-xl text-center">Loading...</div>
            ) : (
              <>
                {activeQuery && (
                  <div className="text-xl text-center">
                    Current query: {activeQuery}
                    <div className="border-t my-6 border-gray-300" />
                  </div>
                )}
                {data ? (
                  <>
                    <div>Last SQL Query recommended by AI: {sqlQuery}</div>
                    <div className="border-t my-6 border-gray-300" />
                    <TableView data={data} />
                    <div className="border-t my-6 border-gray-300" />
                    <ChartView data={data} type="BAR" />
                    <div className="border-t my-6 border-gray-300" />
                    <ChartView data={data} type="PIE" />
                    <div className="border-t my-6 border-gray-300" />
                    <ChartView data={data} type="LINE" />
                  </>
                ) : (
                  <div className="text-xl">
                    Results of your query will be shown here.
                  </div>
                )}
              </>
            )}
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
              history.map((chat, index) => (
                <div
                  key={index}
                  onClick={() => historyClicked(chat)}
                  className="flex justify-between bg-[#f9f9f9] p-2 rounded shadow text-[#333] cursor-pointer hover:bg-[#f0f0f0] transition"
                >
                  <div className="font-medium">{chat.initialQuery}</div>
                  <div className="text-sm text-gray-500 whitespace-nowrap">
                    {new Date(chat.entries[0].time).toLocaleString()}
                  </div>
                </div>
              ))}
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
