let page = 0;
let lastPage = false;
console.log("ROLE:", localStorage.getItem("role"));
window.onload = function () {

    const username = localStorage.getItem("username");
    const role = localStorage.getItem("role");
    console.log("current Role: ",role);

    if (username) {
        const userElement = document.getElementById("username");
        if (userElement) {
            userElement.innerText = "Welcome, " + username;
        }
    }

    if (role !== "ROLE_ADMIN") {
        const createBtn = document.getElementById("create-post-btn");
        if (createBtn) {
            createBtn.style.display = "none";
        }
    }
    loadPosts();
};

async function loadPosts() {

    let token = localStorage.getItem("token");

    try {

        let response = await fetch(
            "http://localhost:8080/api/posts?pageNumber=" + page + "&pageSize=5",
            {
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        // Handle expired token
        if (response.status === 401) {
            alert("Session expired. Please login again.");
            logout();
            return;
        }

        if (!response.ok) {
            console.error("Server Error:", response.status);
            return;
        }

        let data = await response.json();

        console.log("API Response:", data);

        displayPosts(data.content);

        lastPage = data.lastPage;

        document.getElementById("page-number").innerText =
            "Page " + (page + 1);

    } catch (error) {
        console.error("Error loading posts:", error);
    }
}

function displayPosts(posts) {

    const container = document.getElementById("post-container");
    const role = localStorage.getItem("role");

    container.innerHTML = "";

    if (!posts || posts.length === 0) {
        container.innerHTML = "<p>No posts found</p>";
        return;
    }

    posts.forEach(post => {

        const imageUrl = post.imageName
            ? `http://localhost:8080/api/posts/image/${post.imageName}`
            : "../images/default.png";

        const shortContent = post.content
            ? post.content.substring(0, 150)
            : "";

        const postCard = document.createElement("div");
        postCard.className = "post-card";

        let actions = "";

        // Only ADMIN can edit/delete
        if (role === "ROLE_ADMIN") {

            actions = `
                <div class="post-actions">

                    <button class="edit-btn" onclick="editPost(${post.postId})">
                        Edit
                    </button>

                    <button class="delete-btn" onclick="deletePost(${post.postId})">
                        Delete
                    </button>

                </div>
            `;
        }

        postCard.innerHTML = `
            <img class="post-image" src="${imageUrl}" alt="Post Image">

            <div class="post-title">${post.title}</div>

            <div class="post-content">
                ${shortContent}...
            </div>

            ${actions}
        `;

        container.appendChild(postCard);

    });
}

function nextPage() {

    if (lastPage) {
        alert("No more posts available");
        return;
    }

    page++;
    loadPosts();
}

function prevPage() {

    if (page === 0) {
        return;
    }

    page--;
    loadPosts();
}

function goToCreatePost() {
    window.location.href = "/html/create-post.html";
}

function editPost(postId) {
    window.location.href = `/html/edit-post.html?postId=${postId}`;
}

async function deletePost(postId) {

    if (!confirm("Are you sure you want to delete this post?")) {
        return;
    }

    let token = localStorage.getItem("token");

    try {

        let response = await fetch(
            `http://localhost:8080/api/posts/${postId}`,
            {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (response.status === 401) {
            alert("Session expired. Please login again.");
            logout();
            return;
        }

        if (!response.ok) {
            alert("Failed to delete post");
            return;
        }

        alert("Post deleted successfully");

        loadPosts();

    } catch (error) {
        console.error("Delete Error:", error);
    }
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    window.location.href = "/html/login.html";
}