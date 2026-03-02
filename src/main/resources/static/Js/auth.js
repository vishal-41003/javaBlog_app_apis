function login() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Temporary fake login for now
    // Later replace with real backend call

    apiRequest("/auth/login", "POST", { email, password })
        .then(data => {

            // When JWT added, backend will return token
            localStorage.setItem("token", data.token);

            window.location.href = "dashboard.html";
        })
        .catch(() => alert("Login failed"));
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
}

function checkAuth() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "index.html";
    }
}