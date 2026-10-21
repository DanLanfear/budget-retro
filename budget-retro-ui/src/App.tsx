import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import './App.css'

function App() {

    return (
        <BrowserRouter>
            {/*
        GLOBAL LAYOUT WRAPPER
        Anything placed outside <Routes> (like a global Footer) renders on every single page.
      */}
            <div>
                <Routes>
                    {/* Public Routes (Current Pages) */}
                    {/*<Route path="/" element={<Home/>}/>*/}
                    {/*<Route path="/upload" element={<Upload/>}/>*/}
                    {/*<Route path="/settings" element={<View/>}/>*/}

                    {/* FUTURE: Authentication Routes
                      <Route path="/login" element={<Login />} />
                      <Route path="/register" element={<Register />} />
                    */}

                    {/* Catch-all: Redirects any broken links back to the home page */}
                    <Route path="*" element={<Navigate to="/" replace/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App
