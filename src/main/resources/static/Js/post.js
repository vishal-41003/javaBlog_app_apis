function createPost(){

    fetch("http://localhost:8080/api/posts",{

        method:"POST",

        headers:getAuthHeader(),

        body:JSON.stringify({

            title:document.getElementById("title").value,
            content:document.getElementById("content").value

        })

    })

        .then(res=>res.json())

        .then(data=>{

            alert("Post Created");

            window.location.href="index.html";

        });

}