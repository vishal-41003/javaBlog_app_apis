const API = "http://localhost:8080/api"
const token = localStorage.getItem("token")

async function loadDashboard() {
    // Users
    const userRes = await fetch(API + "/users", {
        headers: { "Authorization": "Bearer " + token }
    });
    const users = await userRes.json();
    document.getElementById("usersCount").innerText = users.length;

    // Posts (handle pagination response)
    const postRes = await fetch(API + "/posts?pageNumber=0&pageSize=1000", {
        headers: { "Authorization": "Bearer " + token }
    });
    const postData = await postRes.json();
    // total posts from pagination object
    const totalPosts = postData.totalElements !== undefined ? postData.totalElements : postData.content.length;
    document.getElementById("postsCount").innerText = totalPosts;

    // Categories
    const catRes = await fetch(API + "/categories", {
        headers: { "Authorization": "Bearer " + token }
    });
    const categories = await catRes.json();
    document.getElementById("categoriesCount").innerText = categories.length;
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    window.location.href = "/html/login.html";
}

loadDashboard();