const BASE_URL = "http://localhost:8080/api";

function apiRequest(url, method = "GET", body = null) {

    const token = localStorage.getItem("token");

    return fetch(BASE_URL + url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": token ? `Bearer ${token}` : ""
        },
        body: body ? JSON.stringify(body) : null
    })
        .then(res => res.json());
}