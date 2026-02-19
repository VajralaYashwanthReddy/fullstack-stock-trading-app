import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";
import StockChart from "../components/StockChart";
import { useAuth } from "../context/AuthContext";

export default function StockDetails() {
    const { symbol } = useParams();
    const { user, refreshUser } = useAuth(); // 🔥 important

    const [stock, setStock] = useState(null);
    const [history, setHistory] = useState([]);
    const [qty, setQty] = useState(1);
    const [loading, setLoading] = useState(false);

    // ===============================
    // LOAD STOCK PRICE
    // ===============================
    const loadStock = async () => {
        try {
            const res = await api.get(`/api/stocks/quote/${symbol}`);
            setStock(res.data);
            updateChart(res.data.currentPrice);
        } catch (err) {
            console.error("Error loading stock", err);
        }
    };

    // ===============================
    // RIGHT-END MOVING GRAPH
    // ===============================
    const updateChart = (newPrice) => {
        setHistory(prev => {
            const newPoint = {
                time: prev.length > 0
                    ? prev[prev.length - 1].time + 1
                    : 0,
                price: newPrice
            };

            const updated = [...prev, newPoint];

            if (updated.length > 40) {
                updated.shift();
            }

            return updated;
        });
    };

    // ===============================
    // LIVE UPDATE EVERY SECOND
    // ===============================
    useEffect(() => {
        loadStock();

        const interval = setInterval(() => {
            loadStock();
        }, 1000);

        return () => clearInterval(interval);
    }, [symbol]);

    // ===============================
    // BUY
    // ===============================
    const handleBuy = async () => {
    if (!user) return alert("Please login");
    if (qty <= 0) return alert("Invalid quantity");

    try {
        setLoading(true);

        const res = await api.post("/api/trading/buy", {
            userId: user.id,
            symbol,
            quantity: Number(qty)
        });

        alert(res.data.message);

        await refreshUser(); // ✅ wallet updates here

    } catch (err) {
        alert(err.response?.data?.error || "Buy failed");
    } finally {
        setLoading(false);
    }
};

    // ===============================
    // SELL
    // ===============================
    const handleSell = async () => {
        if (!user) return alert("Please login");
        if (qty <= 0) return alert("Invalid quantity");

        try {
            setLoading(true);

            const res = await api.post("/api/trading/sell", {
                userId: user.id,
                symbol: symbol,
                quantity: Number(qty)
            });

            alert(res.data.message);

            await refreshUser(); // 🔥 update wallet at top

        } catch (err) {
            alert(err.response?.data || "Sell failed");
        } finally {
            setLoading(false);
        }
    };

    if (!stock) return <p>Loading...</p>;

    return (
        <div className="grid grid-cols-4 gap-6">

            {/* LEFT SIDE */}
            <div className="col-span-3">

                <h1 className="text-3xl mb-4">
                    {symbol} - ₹{stock.currentPrice.toFixed(2)}
                </h1>

                <StockChart data={history} />
            </div>

            {/* RIGHT SIDE */}
            <div className="bg-card p-6 rounded-xl">

                <input
                    type="number"
                    value={qty}
                    min="1"
                    onChange={(e) => setQty(Number(e.target.value))}
                    className="w-full p-3 mb-4 bg-slate-700 rounded"
                />

                <button
                    onClick={handleBuy}
                    disabled={loading}
                    className="w-full bg-green-500 p-3 rounded mb-3"
                >
                    {loading ? "Processing..." : "Buy"}
                </button>

                <button
                    onClick={handleSell}
                    disabled={loading}
                    className="w-full bg-red-500 p-3 rounded"
                >
                    {loading ? "Processing..." : "Sell"}
                </button>
            </div>
        </div>
    );
}
