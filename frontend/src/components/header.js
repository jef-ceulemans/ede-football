import React from "react";
import { Link } from "react-router-dom";

const Header = () => (
    <header>
        <nav>
            <ul>
                <li><Link to="/matches">Matches</Link></li>
                <li><Link to="/teams">Create Team</Link></li>
            </ul>
        </nav>
    </header>
);

export default Header;
