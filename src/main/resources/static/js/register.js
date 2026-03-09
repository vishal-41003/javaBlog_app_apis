async function register(){

    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let about = document.getElementById("about").value;

    try{

        let response = await fetch(
            "http://localhost:8080/api/v1/auth/register",
            {
                method:"POST",

                headers:{
                    "Content-Type":"application/json"
                },

                body:JSON.stringify({
                    name:name,
                    email:email,
                    password:password,
                    about:about
                })

            });

        if(response.ok){

            document.getElementById("message").innerText =
                "Registration successful! Redirecting to login...";

            setTimeout(()=>{
                window.location.href="login.html";
            },2000);

        }else{

            document.getElementById("message").innerText =
                "Registration failed";

        }

    }catch(error){

        document.getElementById("message").innerText =
            "Server error";

    }

}
window.onload = function () {

    document.querySelectorAll("input").forEach(input => {
        input.value = "";
    });

};