import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Register() {
    const navigate = useNavigate();
    const { register } = useAuth();

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleRegister = async () => {
        try {
            await register(username, email, password);
            alert("Registration successful!");
            navigate("/");
        } catch (err) {
            console.error(err.response?.data || err.message);
            alert("Registration failed!");
        }
    };

    return (
        <div className="flex items-center justify-center h-screen">
            <div className="bg-card p-8 rounded-xl w-96">
                <h2 className="text-2xl mb-6">Register</h2>

                <input
                    placeholder="Username"
                    className="w-full p-3 mb-4 bg-slate-700 rounded"
                    onChange={(e) => setUsername(e.target.value)}
                />

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
                    onClick={handleRegister}
                    className="w-full bg-primary p-3 rounded"
                >
                    Register
                </button>
            </div>
        </div>
    );
}
