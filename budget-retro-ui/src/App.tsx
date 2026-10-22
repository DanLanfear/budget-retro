import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import Navbar from "./components/Navbar.tsx";

function App() {
    return (
        <BrowserRouter>
            {/*
            GLOBAL LAYOUT WRAPPER
            Anything placed outside <Routes> (like a global Footer) renders on every single page.
            */}
            <div className="app-shell">
                <Navbar/>
                <main className="main-content">
                    <Routes>
                        <Route
                            path="/"
                            // element={<ImportPage onSubmit={addObjects}/>}
                        />
                        <Route
                            path="/objects"
                            // element={<ObjectsPage rows={objects}/>}
                        />
                        <Route
                            path="/dashboard"
                            // element={<DashboardPage rows={objects}/>}
                        />

                        {/* FUTURE: Authentication Routes
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        */}

                        {/* Catch-all: Redirects any broken links back to the home page */}
                        <Route path="*" element={<Navigate to="/" replace/>}/>
                    </Routes>
                </main>
            </div>
        </BrowserRouter>
    );
}

export default App
