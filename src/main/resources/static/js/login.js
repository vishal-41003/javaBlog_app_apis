async function login(){

    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    try{

        let response = await fetch("http://localhost:8080/api/v1/auth/login",{

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify({
                username:email,
                password:password
            })

        });

        if(response.ok){

            let data = await response.json();

            localStorage.setItem("token",data.token);
            localStorage.setItem("username",data.username);
            localStorage.setItem("role",data.role);

            // Role based redirect
            if(data.role === "ROLE_ADMIN"){
                window.location.href="admin-dashboard.html";
            }else{
                window.location.href="dashboard.html";
            }

        }else{

            document.getElementById("error").innerText="Invalid Email or Password";

        }

    }catch(error){

        document.getElementById("error").innerText="Server Error";

    }

}