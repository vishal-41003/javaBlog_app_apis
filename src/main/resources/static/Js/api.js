const API_BASE = "http://localhost:8080/api";

function getToken() {
    return localStorage.getItem("token");
}

function getAuthHeader() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    };
}