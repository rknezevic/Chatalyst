export interface DataEntry {
  label: string;
  value: number;
}

export type ViewType = "text" | "table" | "bar" | "pie" | "line";

export interface HistoryEntry {
  query: string;
  time: Date;
  data: DataEntry[] | null;
  type: ViewType;
}