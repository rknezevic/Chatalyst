import { Bar, Pie, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  ArcElement,
  BarElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  type ChartType,
} from "chart.js";
import type { DataEntry } from "../types";

ChartJS.register(
  ArcElement,
  BarElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
);

interface ChartViewProps {
  data: DataEntry[];
  type: ChartType;
}

export const ChartView = ({ data, type }: ChartViewProps) => {
  const chartData = {
    labels: data.map((d) => d.label),
    datasets: [
      {
        label: "Vrijednost",
        data: data.map((d) => d.value),
        backgroundColor: ["#FCEF91", "#FB9E3A", "#E6521F", "#ea1414ff"],
      },
    ],
  };

  return (
    <div className="w-full h-64">
      {type === "bar" ? (
        <Bar
          data={chartData}
          options={{
            maintainAspectRatio: false,
            responsive: true,
            plugins: {
              legend: {
                display: false,
              },
            },
          }}
        />
      ) : type === "pie" ? (
        <Pie
          data={chartData}
          options={{
            maintainAspectRatio: false,
            responsive: true,
            elements: {
              arc: {
                borderWidth: 1, // Thickness of border
                borderColor: "#f7f7f7", // Light gray (or any color you want)
              },
            },
          }}
        />
      ) : (
        <Line
          data={chartData}
          options={{
            maintainAspectRatio: false,
            responsive: true,
            elements: {
              line: {
                tension: 0.1,
                borderWidth: 3,
              },
              point: {
                radius: 5,
                hoverRadius: 7,
                borderWidth: 1,
                borderColor: "#000",
              },
            },
            plugins: {
              legend: {
                display: false,
              },
            },
          }}
        />
      )}
    </div>
  );
};
