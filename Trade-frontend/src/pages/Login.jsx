import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Login() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
  try {
    await login(email, password);
    navigate("/dashboard");
  } catch (err) {
    console.error("LOGIN ERROR:", err.response?.data || err.message);
    alert("Login failed. Check console.");
  }
};

    return (
        <div className="flex items-center justify-center h-screen">
            <div className="bg-card p-8 rounded-xl w-96">
                <h2 className="text-2xl mb-6">Login</h2>

                <input
                    placeholder="Email"
                    className="w-full p-3 mb-4 bg-slate-700 rounded"
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Password"
                    className="w-full p-3 mb-6 bg-slate-700 rounded"
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button
                    onClick={handleLogin}
                    className="w-full bg-primary p-3 rounded mb-4"
                >
                    Login
                </button>

                <Link to="/register" className="text-primary">
                    Register
                </Link>
            </div>
        </div>
    );
}
