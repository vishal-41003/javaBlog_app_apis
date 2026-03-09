const token = localStorage.getItem("token");

window.onload = function(){
    loadUsers();
};

async function createUser(){

    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let about = document.getElementById("about").value;

    let response = await fetch("http://localhost:8080/api/users",{

        method:"POST",

        headers:{
            "Content-Type":"application/json",
            "Authorization":"Bearer "+token
        },

        body:JSON.stringify({
            name:name,
            email:email,
            password:password,
            about:about
        })

    });

    if(response.ok){

        document.getElementById("message").innerText="User created successfully";

        loadUsers();

    }else{

        document.getElementById("message").innerText="Error creating user";

    }

}

async function loadUsers(){

    let response = await fetch("http://localhost:8080/api/users",{

        headers:{
            "Authorization":"Bearer "+token
        }

    });

    let users = await response.json();

    let container = document.getElementById("userList");

    container.innerHTML="";

    users.forEach(user=>{

        let div = document.createElement("div");

        div.className="user-card";

        div.innerHTML=`
        <h3>${user.name}</h3>
        <p>Email: ${user.email}</p>
        <p>${user.about}</p>

        <button class="delete-btn" onclick="deleteUser(${user.id})">
        Delete
        </button>
        `;

        container.appendChild(div);

    });

}

async function deleteUser(id){

    let response = await fetch(`http://localhost:8080/api/users/${id}`,{

        method:"DELETE",

        headers:{
            "Authorization":"Bearer "+token
        }

    });

    if(response.ok){

        loadUsers();

    }else{

        alert("Error deleting user");

    }

}