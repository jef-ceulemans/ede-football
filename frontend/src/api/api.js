import axios from "axios";

const baseURL = process.env.REACT_APP_BASE_URL;

class ApiGateway {
    // MatchService
    static getAllMatches = async () => {
        return await axios.get(`${baseURL}/matches/all`);
    };

    static getMatch = async (id) => {
        return await axios.get(`${baseURL}/matches?matchId=${id}`);
    };

    static createMatch = async (match, token) => {
        return axios.post(`${baseURL}/matches`, match, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
    };

    static updateMatchStatus = async (id, status, score, token) => {
        return axios.put(
            `${baseURL}/matches/status`,
            { id, status, score },
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            }
        );
    };

    static deleteMatch = async (id, token) => {
        try {
            const response = await axios.delete(`${baseURL}/matches?matchId=${id}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            });
            return response.data;
        } catch (error) {
            console.error('Error deleting match', error);
            throw error;
        }
    };

    // NewsService
    static getAllNews = async () => {
        return await axios.get(`${baseURL}/news/all`);
    };

    static getNewsByAuthor = async (author) => {
        return await axios.get(`${baseURL}/news`, { params: { author } });
    };

    static createNews = async (news, token) => {
        return axios.post(`${baseURL}/news`, news, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
    };

    static updateNews = async (news, token) => {
        return axios.put(`${baseURL}/news`, news, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
    };

    // TeamService
    static getAllTeams = async () => {
        return await axios.get(`${baseURL}/teams/all`);
    };

    static getTeam = async (id) => {
        return await axios.get(`${baseURL}/teams?teamId=${id}`);
    };

    static registerTeam = async (team, token) => {
        return axios.post(`${baseURL}/teams`, team, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
    };

    static updateTeam = async (id, team, token) => {
        return axios.put(`${baseURL}/teams?teamId=${id}`, team, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
    };

    static deleteTeam = async (id, token) => {
        try {
            const response = await axios.delete(`${baseURL}/teams?teamId=${id}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            });
            return response.data;
        } catch (error) {
            console.error('Error deleting team', error);
            throw error;
        }
    };
}

export default ApiGateway;
