import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../api/axios";

export default function Portfolio() {
    const { user } = useAuth();
    const [data, setData] = useState([]);

    const loadPortfolio = async () => {
        if (!user) return;

        const res = await api.get(`/api/trading/portfolio/${user.id}`);
        setData(res.data);
    };

    useEffect(() => {
        loadPortfolio();
        const interval = setInterval(loadPortfolio, 5000);
        return () => clearInterval(interval);
    }, [user]);

    return (
        <div>
            <h1 className="text-2xl mb-6">My Portfolio</h1>

            <div className="space-y-4">
                {data.map(stock => (
                    <div
                        key={stock.symbol}
                        className="bg-card p-4 rounded-lg flex justify-between"
                    >
                        <div>
                            <h2 className="font-semibold">
                                {stock.symbol}
                            </h2>
                            <p>Qty: {stock.quantity}</p>
                            <p>Buy Price: ₹{stock.averagePrice}</p>
                            <p>Live Price: ₹{stock.currentPrice}</p>
                        </div>

                        <div
                            className={`font-bold text-lg ${
                                stock.profitLoss >= 0
                                    ? "text-green-400"
                                    : "text-red-400"
                            }`}
                        >
                            ₹{stock.profitLoss.toFixed(2)}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
