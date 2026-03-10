const token = localStorage.getItem("token");

// Redirect if token not found
if (!token) {
    window.location.href = "login.html";
}

window.onload = function () {
    loadCategories();
    loadPosts();
};


// Load categories
async function loadCategories() {
    try {
        let res = await fetch("http://localhost:8080/api/categories", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!res.ok) {
            console.error("Error loading categories");
            return;
        }

        let data = await res.json();
        let categories = data.content || data; // supports paginated or normal response

        let select = document.getElementById("category");
        if (!select) return;

        select.innerHTML = "";

        categories.forEach(cat => {
            let option = document.createElement("option");
            option.value = cat.categoryId;
            option.text = cat.categoryTitle;
            select.appendChild(option);
        });

    } catch (error) {
        console.error("Server error while loading categories", error);
    }
}


// Load posts
async function loadPosts() {
    try {

        let res = await fetch("http://localhost:8080/api/posts", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!res.ok) {
            console.error("Error loading posts");
            return;
        }

        let data = await res.json();
        let posts = data.content || data;

        let container = document.getElementById("postsList");
        container.innerHTML = "";

        if (!posts || posts.length === 0) {
            container.innerHTML = "<p>No posts available</p>";
            return;
        }

        posts.forEach(post => {

            let card = document.createElement("div");
            card.className = "post-card";

            card.innerHTML = `
                <h3>${post.title}</h3>
                <p>${post.content}</p>
                <p>By <strong>${post.user?.name || "Unknown"}</strong></p>
            `;

            container.appendChild(card);

        });

    } catch (error) {
        console.error("Server error while loading posts", error);
    }
}


// Create post
async function createPost() {

    let title = document.getElementById("title").value;
    let content = document.getElementById("content").value;
    let categoryId = document.getElementById("category").value;
    let message = document.getElementById("message");

    if (!title || !content) {
        message.innerText = "Please fill all fields";
        return;
    }

    try {

        let res = await fetch(`http://localhost:8080/api/user/posts/${categoryId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({ title, content })
        });

        if (res.ok) {

            message.innerText = "Post Created Successfully";

            document.getElementById("title").value = "";
            document.getElementById("content").value = "";

            loadPosts();

        } else {
            message.innerText = "Error creating post";
        }

    } catch (error) {
        message.innerText = "Server error";
    }
}


// Logout
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}