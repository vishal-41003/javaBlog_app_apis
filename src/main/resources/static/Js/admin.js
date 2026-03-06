function loadAdminData(){
    loadUsers();
}

function loadUsers(){

    fetch("http://localhost:8080/api/users",{

        headers:{
            "Authorization":"Bearer "+localStorage.getItem("token")
        }

    })

        .then(res=>res.json())

        .then(data=>{

            let html="<h3>Users</h3>";

            data.forEach(user=>{

                html+=`
<div class="card">
<p><b>Name:</b> ${user.name}</p>
<p><b>Email:</b> ${user.email}</p>
<p><b>Role:</b> ${user.roles}</p>
</div>
`;

            });

            document.getElementById("adminData").innerHTML=html;

        });

}


function loadPosts(){

    fetch("http://localhost:8080/api/posts")

        .then(res=>res.json())

        .then(data=>{

            let html="<h3>Posts</h3>";

            data.content.forEach(post=>{

                html+=`
<div class="card">
<h4>${post.title}</h4>
<p>${post.content}</p>
<button onclick="deletePost(${post.postId})">Delete</button>
</div>
`;

            });

            document.getElementById("adminData").innerHTML=html;

        });

}


function deletePost(id){

    fetch("http://localhost:8080/api/posts/"+id,{

        method:"DELETE",

        headers:{
            "Authorization":"Bearer "+localStorage.getItem("token")
        }

    })

        .then(()=>{
            alert("Post Deleted");
            loadPosts();
        });

}