import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer
} from "recharts";

export default function StockPage() {

    const { symbol } = useParams();

    const [price, setPrice] = useState(0);
    const [chartData, setChartData] = useState([]);

    const loadStock = async () => {
        try {
            const quoteRes = await api.get(`/api/stocks/quote/${symbol}`);
            setPrice(quoteRes.data.currentPrice);

            const historyRes = await api.get(`/api/stocks/history/${symbol}`);

            const timestamps = historyRes.data.t;
            const prices = historyRes.data.c;

            const formatted = timestamps.map((time, index) => ({
                time: new Date(time * 1000).toLocaleTimeString(),
                price: prices[index]
            }));

            setChartData(formatted);

        } catch (err) {
            console.error("Error loading stock", err);
        }
    };

    useEffect(() => {
        loadStock();
    }, [symbol]);

    return (
        <div>

            <h1 className="text-2xl font-bold mb-6">
                {symbol} - ₹{price}
            </h1>

            <div className="bg-card p-6 rounded-lg">

                <ResponsiveContainer width="100%" height={400}>
                    <LineChart data={chartData}>
                        <XAxis dataKey="time" hide />
                        <YAxis domain={['auto', 'auto']} />
                        <Tooltip />
                        <Line
                            type="monotone"
                            dataKey="price"
                            stroke="#22c55e"
                            strokeWidth={2}
                            dot={false}
                        />
                    </LineChart>
                </ResponsiveContainer>

            </div>

        </div>
    );
}
