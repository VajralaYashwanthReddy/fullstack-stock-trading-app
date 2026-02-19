import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Layout({ children }) {
    const { user, logout } = useAuth();

    return (
        <div className="flex h-screen bg-dark text-white">

            {/* Sidebar */}
            <aside className="w-64 bg-card p-6 border-r border-slate-700">
                <h2 className="text-2xl font-bold mb-6 text-primary">
                    MRU Trading Platform
                </h2>

                <nav className="space-y-4">
                    <Link to="/dashboard" className="block hover:text-primary">
                        Dashboard
                    </Link>

                    <Link to="/portfolio" className="block hover:text-primary">
                        Portfolio
                    </Link>

                    <Link to="/profile" className="block hover:text-primary">
                        Profile
                    </Link>
                </nav>
            </aside>

            {/* Main */}
            <div className="flex flex-col flex-1">

                {/* Header */}
                <header className="bg-card p-4 flex justify-between items-center border-b border-slate-700">
                    <div>
                        <span className="font-semibold">
                            Welcome, {user?.username}
                        </span>

                        {/* ✅ FIXED FIELD NAME */}
                        <div className="text-sm text-slate-400">
                            Wallet: ₹{user?.balance?.toFixed(2)}

                        </div>
                    </div>

                    <button
                        onClick={logout}
                        className="text-danger hover:text-red-400"
                    >
                        Logout
                    </button>
                </header>

                {/* Page Content */}
                <main className="p-8 overflow-auto flex-1">
                    {children}
                </main>

            </div>
        </div>
    );
}
