export type DataEntry = Record<string, string | number>;


export interface ChatHistory {
  initialQuery: string;
  entries: HistoryEntry[];
}

export type ViewType = "TABLE" | "BAR" | "PIE" | "LINE";

export interface HistoryEntry {
  query: string;
  time: Date;
  data: DataEntry[] | null;
  type: ViewType;
  conversationId: string | null;
  sqlQuery: string | null;
}