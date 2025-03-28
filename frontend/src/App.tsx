import { BrowserRouter, Routes, Route, Navigate } from "react-router";
import { AuthProvider } from "./context/AuthContext";
import "./App.css";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Thread from "./pages/Thread";
import Header from "./components/Header";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Header></Header>

        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Home />} />
          <Route path="/page/:pageNum" element={<Home />} />
          <Route path="/thread/:title" element={<Thread />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
