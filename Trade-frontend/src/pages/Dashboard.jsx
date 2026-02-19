import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axios";

export default function Dashboard() {
    const navigate = useNavigate();

    const stockSymbols = [
        "TCS",
        "INFY",
        "RELIANCE",
        "HDFCBANK",
        "ITC",
        "WIPRO",
        "SBIN",
        "ICICIBANK",
        "LT",
        "BAJFINANCE"
    ];

    const [stocks, setStocks] = useState({});

    const fetchStock = async (symbol) => {
        try {
            const res = await api.get(`/api/stocks/quote/${symbol}`);
            return res.data;
        } catch (err) {
            console.error("Error fetching", symbol, err);
            return null;
        }
    };

    const loadStocks = async () => {
        const updated = {};

        await Promise.all(
            stockSymbols.map(async (symbol) => {
                const data = await fetchStock(symbol);
                if (data) {
                    updated[symbol] = data;
                }
            })
        );

        setStocks(updated);
    };

    useEffect(() => {
        loadStocks();

        const interval = setInterval(() => {
            loadStocks();
        }, 5000);

        return () => clearInterval(interval);
    }, []);

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">
                Market Watch - Live Stocks
            </h1>

            <div className="grid grid-cols-4 gap-6 mb-12">
                {stockSymbols.map((symbol) => {
                    const stock = stocks[symbol];

                    return (
                        <div
                            key={symbol}
                            onClick={() => navigate(`/stock/${symbol}`)}
                            className="bg-card p-6 rounded-lg cursor-pointer hover:scale-105 transition"
                        >
                            <h2 className="text-lg font-semibold">
                                {symbol}
                            </h2>

                            {stock ? (
                                <>
                                    {/* ✅ FIXED PROPERTY NAME */}
                                    <p className="text-xl font-bold text-white">
                                        ₹{stock.currentPrice?.toFixed(2)}
                                    </p>

                                    <p
                                        className={`text-sm ${
                                            stock.change >= 0
                                                ? "text-green-400"
                                                : "text-red-400"
                                        }`}
                                    >
                                        {stock.change >= 0 ? "+" : ""}
                                        {stock.change?.toFixed(2)} (
                                        {stock.changePercent >= 0 ? "+" : ""}
                                        {stock.changePercent?.toFixed(2)}%)
                                    </p>
                                </>
                            ) : (
                                <p className="text-slate-400">
                                    Loading...
                                </p>
                            )}
                        </div>
                    );
                })}
            </div>

            {/* MUTUAL FUNDS SECTION */}
            <h1 className="text-2xl font-bold mb-6">
                Mutual Funds
            </h1>

            <div className="grid grid-cols-3 gap-6">
                <FundCard name="SBI Bluechip Fund" />
                <FundCard name="Axis Growth Fund" />
                <FundCard name="HDFC Top 100 Fund" />
                <FundCard name="ICICI Prudential Fund" />
                <FundCard name="Nippon India Large Cap" />
                <FundCard name="Parag Parikh Flexi Cap" />
            </div>
        </div>
    );
}

function FundCard({ name }) {
    const [nav, setNav] = useState(120);

    useEffect(() => {
        const interval = setInterval(() => {
            const randomChange =
                (Math.random() * 2 - 1);
            setNav(prev =>
                Number(prev) + Number(randomChange)
            );
        }, 4000);

        return () => clearInterval(interval);
    }, []);

    return (
        <div className="bg-card p-6 rounded-lg hover:scale-105 transition">
            <h2 className="text-md font-semibold">{name}</h2>
            <p className="text-blue-400">
                NAV: ₹{nav.toFixed(2)}
            </p>
            <button className="mt-4 bg-primary px-4 py-2 rounded">
                Invest
            </button>
        </div>
    );
}
