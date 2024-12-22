import React, { useEffect, useState } from "react";
import ApiGateway from "../../api/ApiGateway"; // Importeer je API Gateway

const MatchList = () => {
    const [matches, setMatches] = useState([]);

    useEffect(() => {
        const fetchMatches = async () => {
            try {
                const response = await ApiGateway.getMatches(); // Verander naar jouw API-call
                setMatches(response.data);
            } catch (error) {
                console.error("Error fetching matches", error);
            }
        };
        fetchMatches();
    }, []);

    return (
        <div>
            <h1>Matches</h1>
            <ul>
                {matches.map((match) => (
                    <MatchItem key={match.id} match={match} />
                ))}
            </ul>
        </div>
    );
};

export default MatchList;
