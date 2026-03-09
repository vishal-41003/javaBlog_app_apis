const token = localStorage.getItem("token");

window.onload = function(){
    loadCategories();
    loadPosts();
};

async function loadCategories(){

    let res = await fetch("http://localhost:8080/api/categories");

    let categories = await res.json();

    let select = document.getElementById("category");

    categories.forEach(cat=>{
        let option = document.createElement("option");
        option.value = cat.categoryId;
        option.text = cat.categoryTitle;
        select.appendChild(option);
    });
}

async function createPost(){

    let title = document.getElementById("title").value;
    let content = document.getElementById("content").value;
    let categoryId = document.getElementById("category").value;

    let res = await fetch(`http://localhost:8080/api/user/posts/${categoryId}`,{

        method:"POST",

        headers:{
            "Content-Type":"application/json",
            "Authorization":"Bearer "+token
        },

        body:JSON.stringify({
            title:title,
            content:content
        })

    });

    if(res.ok){

        document.getElementById("message").innerText="Post Created Successfully";

        loadPosts();

    }else{
        document.getElementById("message").innerText="Error creating post";
    }
}

async function loadPosts(){

    let res = await fetch("http://localhost:8080/api/posts");

    let posts = await res.json();

    let container = document.getElementById("posts");

    container.innerHTML="";

    posts.forEach(post=>{

        let card = document.createElement("div");
        card.className="post-card";

        card.innerHTML = `
        <h3>${post.title}</h3>
        <p>${post.content}</p>
        <small>By ${post.user.name}</small>
        `;

        container.appendChild(card);

    });
}