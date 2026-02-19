import { useState } from "react";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

export default function Profile() {
    const { user, refreshUser } = useAuth();
    const [amount, setAmount] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [username, setUsername] = useState(user?.username || "");
    const [email, setEmail] = useState(user?.email || "");

    // ==========================
    // UPDATE PROFILE
    // ==========================
    const updateProfile = async () => {
        try {
            await api.put(`/api/users/update-profile/${user.id}`, {
                username,
                email
            });

            await refreshUser();
            alert("Profile updated successfully");

        } catch (err) {
            console.error(err);
            alert("Failed to update profile");
        }
    };

    // ==========================
    // ADD FUNDS (🔥 FIXED)
    // ==========================
const addFunds = async () => {
    if (!amount || Number(amount) <= 0) {
        alert("Enter valid amount");
        return;
    }

    try {
        await api.post("/api/users/add-funds", {
            userId: user.id,
            amount: Number(amount),
        });

        await refreshUser();   // 🔥 THIS UPDATES WALLET LIVE

        alert("Funds added successfully!");
        setAmount("");

    } catch (err) {
        console.error(err);
        alert("Failed to add funds");
    }
};



    // ==========================
    // CHANGE PASSWORD
    // ==========================
    const changePassword = async () => {
        if (!newPassword) {
            alert("Enter new password");
            return;
        }

        try {
            await api.post("/api/users/change-password", {
                userId: user.id,
                newPassword,
            });

            alert("Password updated successfully");
            setNewPassword("");

        } catch (err) {
            alert("Failed to change password");
        }
    };

    return (
        <div className="flex justify-center">
            <div className="bg-card p-8 rounded-lg w-96 shadow-lg">

                <h2 className="text-xl font-bold mb-4">Profile</h2>

                <p><strong>Id:</strong> {user?.id}</p>

                <div className="mt-3">
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="w-full p-2 rounded bg-slate-700 mb-2"
                        placeholder="Username"
                    />

                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="w-full p-2 rounded bg-slate-700 mb-2"
                        placeholder="Email"
                    />

                    <button
                        onClick={updateProfile}
                        className="w-full bg-primary p-2 rounded"
                    >
                        Update Profile
                    </button>
                </div>

                <hr className="my-6 border-slate-600" />

                <p><strong>Wallet:</strong> ₹{user?.balance?.toFixed(2)}</p>

                <div className="mt-3">
                    <input
                        type="number"
                        placeholder="Add Amount"
                        className="w-full p-2 rounded bg-slate-700 mb-2"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                    />

                    <button
                        onClick={addFunds}
                        className="w-full bg-green-600 p-2 rounded"
                    >
                        Add Money
                    </button>
                </div>

                <hr className="my-6 border-slate-600" />

                <div>
                    <input
                        type="password"
                        placeholder="New Password"
                        className="w-full p-2 rounded bg-slate-700 mb-2"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                    />

                    <button
                        onClick={changePassword}
                        className="w-full bg-primary p-2 rounded"
                    >
                        Change Password
                    </button>
                </div>

            </div>
        </div>
    );
}
