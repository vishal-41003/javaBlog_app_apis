const params = new URLSearchParams(window.location.search);
const postId = params.get("id");

async function loadPost(){

    let token = localStorage.getItem("token");

    let response = await fetch(
        "http://localhost:8080/api/posts/" + postId,
        {
            headers:{
                "Authorization":"Bearer " + token
            }
        }
    );

    let post = await response.json();

    document.getElementById("title").innerText = post.title;
    document.getElementById("content").innerText = post.content;

}

function goBack(){
    window.location.href="dashboard.html";
}

window.onload = loadPost;