import { Routes, Route, BrowserRouter as Router, useNavigate } from 'react-router-dom';
import './App.css';
import { useEffect, useState } from 'react';
import jwtDecode from 'jwt-decode';
import { useAuth } from './components/auth_context';
import { Button } from 'semantic-ui-react';
import MatchList from './components/Matches/MatchList'; // Component voor het tonen van matches
import TeamForm from './components/Teams/TeamForm';     // Component voor het aanmaken van teams

function Header() {
    return (
        <header style={{ padding: '10px 0', textAlign: 'center', background: '#f4f4f4' }}>
            <h1>Football</h1>
            <nav>
                <a href="/matches" style={{ margin: '0 10px' }}>Matches</a>
                <a href="/teams" style={{ margin: '0 10px' }}>Create Team</a>
            </nav>
        </header>
    );
}

function Main() {
    return (
        <div className="content">
            <Routes>
                <Route path="/matches" element={<MatchList />} /> {/* Route voor het tonen van matches */}
                <Route path="/teams" element={<TeamForm />} />   {/* Route voor het aanmaken van teams */}
                <Route path="/" element={<Home />} />            {/* Home component */}
            </Routes>
        </div>
    );
}

function App() {
    const { login } = useAuth();
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    function handleCallbackResponse(response) {
        login(response.credential);
        const userData = jwtDecode(response.credential);
        setUser(userData.name);
        document.getElementById("signIn").hidden = true;
    }

    function handleLogout() {
        // Logout logica
        setUser(null);
        google.accounts.id.renderButton(
            document.getElementById("signIn"), { theme: "outline", size: "large" }
        );
        navigate('/');
    }

    useEffect(() => {
        /* global google */
        google.accounts.id.initialize({
            client_id: process.env.REACT_APP_GOOGLE_CLIENTID,
            callback: handleCallbackResponse
        });

        google.accounts.id.renderButton(
            document.getElementById("signIn"), { theme: "outline", size: "large" }
        );
    }, [user]);

    return (
        <Router>
            <div className='main'>
                <Header />
                <div id='signIn' style={{ marginBottom: '20px', marginTop: '20px' }}></div>
                {user && (
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '10px' }}>
                        <p style={{ margin: '0' }}>Welcome, {user}</p>
                        <Button onClick={handleLogout} style={{ marginLeft: '10px' }}>Logout</Button>
                    </div>
                )}
                <Main />
            </div>
        </Router>
    );
}

export default App;
