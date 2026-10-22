import {NavLink} from "react-router-dom";

function Navbar() {
    return (
        <header className="topbar">
            <NavLink to="/" className="brand">
                <span className="brand-icon"></span>
                {/*<span className="brand-icon"><Layers3 /></span>*/}
                <span>Budget Retro</span>
            </NavLink>

            <nav className="nav-links">
                <NavLink to="/" end>
                    Import & Submit
                </NavLink>
                <NavLink to="/objects">
                    All Objects
                </NavLink>
                <NavLink to="/dashboard">
                    Dashboard
                </NavLink>
            </nav>

            {/*<div className="profile">JD</div>*/}
        </header>
    );
}

export default Navbar;