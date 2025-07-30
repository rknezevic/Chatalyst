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
} from "chart.js";
import type { DataEntry, ViewType } from "../types";

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
  type: ViewType;
}

export const ChartView = ({ data, type }: ChartViewProps) => {
  if (!data || data.length === 0) return null;

  const keys = Object.keys(data[0]);
  const labelKey = keys[0];
  const valueKey = keys[1];

  const chartData = {
    labels: data.map((d) => String(d[labelKey])),
    datasets: [
      {
        label: "Vrijednost",
        data: data.map((d) =>
          typeof d[valueKey] === "number" ? (d[valueKey] as number) : 0
        ),
        backgroundColor: ["#FCEF91", "#FB9E3A", "#E6521F", "#ea1414ff"],
      },
    ],
  };

  return (
    <div className="w-full h-64">
      {type === "BAR" ? (
        <>
          <div className="text-sm text-gray-500">{valueKey}</div>
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
        </>
      ) : type === "PIE" ? (
        <Pie
          data={chartData}
          options={{
            maintainAspectRatio: false,
            responsive: true,
            elements: {
              arc: {
                borderWidth: 1,
                borderColor: "#f7f7f7",
              },
            },
          }}
        />
      ) : (
        <>
          <div className="text-sm text-gray-500">{valueKey}</div>
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
        </>
      )}
    </div>
  );
};
