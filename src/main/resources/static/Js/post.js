function createPost() {

    const userId = document.getElementById("userId").value;
    const categoryId = document.getElementById("categoryId").value;

    const post = {
        title: document.getElementById("title").value,
        content: document.getElementById("content").value
    };

    apiRequest(`/users/${userId}/categories/${categoryId}/posts`, "POST", post)
        .then(() => alert("Post Created"))
        .catch(() => alert("Error"));
}

function getPosts() {

    apiRequest("/posts")
        .then(data => {

            const div = document.getElementById("posts");
            div.innerHTML = "";

            data.content.forEach(post => {
                div.innerHTML += `
                    <div class="post">
                        <h3>${post.title}</h3>
                        <p>${post.content}</p>
                    </div>
                `;
            });
        });
}