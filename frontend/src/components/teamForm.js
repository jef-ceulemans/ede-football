import React, { useState } from "react";
import ApiGateway from "../../api/ApiGateway";

const TeamForm = () => {
    const [teamName, setTeamName] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await ApiGateway.postTeam({ name: teamName });
            alert("Team created successfully!");
            setTeamName(""); // Reset het formulier
        } catch (error) {
            console.error("Error creating team", error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h1>Create a Team</h1>
            <input
                type="text"
                value={teamName}
                onChange={(e) => setTeamName(e.target.value)}
                placeholder="Team Name"
                required
            />
            <button type="submit">Create Team</button>
        </form>
    );
};

export default TeamForm;
