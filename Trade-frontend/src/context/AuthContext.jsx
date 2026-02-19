import { createContext, useContext, useEffect, useState } from "react";
import api from "../api/axios";

const AuthContext = createContext();

export function AuthProvider({ children }) {

    // ============================
    // INITIAL LOAD FROM STORAGE
    // ============================
    const [user, setUser] = useState(() => {
        const saved = localStorage.getItem("user");
        return saved ? JSON.parse(saved) : null;
    });

    const [loading, setLoading] = useState(true);

    // ============================
    // LOGIN
    // ============================
    const login = async (email, password) => {
        const res = await api.post("/api/users/login", {
            usernameOrEmail: email,
            password,
        });

        const loggedUser = res.data;

        // Save token
        localStorage.setItem("token", loggedUser.token);

        // Save user
        setUser(loggedUser);
        localStorage.setItem("user", JSON.stringify(loggedUser));
    };

    // ============================
    // REGISTER
    // ============================
    const register = async (username, email, password) => {
        await api.post("/api/users/register", {
            username,
            email,
            password,
        });
    };

    // ============================
    // REFRESH USER (🔥 IMPORTANT)
    // ============================
    const refreshUser = async () => {
        if (!user?.id) return;

        try {
            const res = await api.get(`/api/users/${user.id}`);

            const updatedUser = {
                ...user,
                ...res.data   // merge updated data
            };

            setUser(updatedUser);
            localStorage.setItem("user", JSON.stringify(updatedUser));

        } catch (err) {
            console.error("Refresh failed:", err);
        }
    };

    // ============================
    // LOGOUT
    // ============================
    const logout = () => {
        setUser(null);
        localStorage.removeItem("user");
        localStorage.removeItem("token");
    };

    // ============================
    // AUTO LOAD ON APP START
    // ============================
    useEffect(() => {
        const init = async () => {
            if (user?.id) {
                await refreshUser();
            }
            setLoading(false);
        };

        init();
        // eslint-disable-next-line
    }, []);

    // ============================
    // AUTO SYNC STORAGE
    // ============================
    useEffect(() => {
        if (user) {
            localStorage.setItem("user", JSON.stringify(user));
        }
    }, [user]);

    return (
        <AuthContext.Provider
            value={{
                user,
                setUser,
                login,
                register,
                logout,
                refreshUser,
                loading
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);
