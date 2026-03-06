
document.addEventListener("DOMContentLoaded", function(){

    const role = localStorage.getItem("role");

    if(role === "ROLE_ADMIN"){
        document.getElementById("adminLink").style.display="block";
    }

});
function login(){

    fetch("http://localhost:8080/api/auth/login",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            username:document.getElementById("username").value,
            password:document.getElementById("password").value
        })
    })

        .then(res=>res.json())
        .then(data=>{

            localStorage.setItem("token",data.token);
            localStorage.setItem("role",data.role);

            alert("Login Successful");

            window.location.href="index.html";

        });

}

function register(){

    fetch("http://localhost:8080/api/auth/register",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            name:document.getElementById("name").value,
            username:document.getElementById("username").value,
            password:document.getElementById("password").value
        })
    })

        .then(res=>res.json())
        .then(data=>{
            alert("User Registered");
            window.location.href="login.html";
        });

}
function checkAdmin(){

    const role = localStorage.getItem("role");

    if(role === "ROLE_ADMIN"){
        document.getElementById("adminLink").style.display="block";
    }

}