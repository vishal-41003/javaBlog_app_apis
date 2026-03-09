const postId = new URLSearchParams(window.location.search).get("postId");
const token = localStorage.getItem("token");

window.onload = loadPost;


// LOAD EXISTING POST
async function loadPost(){

    try{

        let response = await fetch(
            `http://localhost:8080/api/posts/${postId}`,
            {
                headers:{
                    "Authorization":"Bearer " + token
                }
            });

        if(response.status === 401){
            alert("Session expired. Please login again.");
            localStorage.removeItem("token");
            window.location.href="/html/login.html";
            return;
        }

        let post = await response.json();

        document.getElementById("title").value = post.title;
        document.getElementById("content").value = post.content;

        if(post.imageName){
            document.getElementById("previewImage").src =
                `http://localhost:8080/api/posts/image/${post.imageName}`;
        }

    }catch(error){
        console.error("Load Post Error:", error);
    }

}


// UPDATE POST
async function updatePost(){

    let title = document.getElementById("title").value;
    let content = document.getElementById("content").value;
    let imageFile = document.getElementById("image").files[0];

    try{

        // UPDATE TEXT DATA
        let response = await fetch(
            `http://localhost:8080/api/posts/${postId}`,
            {
                method:"PUT",
                headers:{
                    "Content-Type":"application/json",
                    "Authorization":"Bearer " + token
                },
                body:JSON.stringify({
                    title:title,
                    content:content
                })
            });

        if(!response.ok){
            document.getElementById("message").innerText =
                "Failed to update post";
            return;
        }


        // UPLOAD IMAGE IF SELECTED
        if(imageFile){

            let formData = new FormData();
            formData.append("image", imageFile);

            await fetch(
                `http://localhost:8080/api/posts/image/upload/${postId}`,
                {
                    method:"POST",
                    headers:{
                        "Authorization":"Bearer " + token
                    },
                    body:formData
                });
        }


        document.getElementById("message").innerText =
            "Post updated successfully";


        setTimeout(()=>{
            window.location.href="/html/dashboard.html";
        },1500);


    }catch(error){

        console.error("Update Error:", error);

        document.getElementById("message").innerText =
            "Server error";

    }

}


// IMAGE PREVIEW BEFORE UPLOAD
document.getElementById("image").addEventListener("change", function(){

    let file = this.files[0];

    if(file){

        let reader = new FileReader();

        reader.onload = function(e){
            document.getElementById("previewImage").src = e.target.result;
        }

        reader.readAsDataURL(file);
    }

});