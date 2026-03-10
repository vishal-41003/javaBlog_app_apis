async function createPost(){

    let title = document.getElementById("title").value;
    let content = document.getElementById("content").value;
    let categoryId = document.getElementById("categoryId").value;
    let imageFile = document.getElementById("imageFile").files[0];

    let token = localStorage.getItem("token");

    if(!token){
        window.location.href = "/html/login.html";
        return;
    }

    if(!title || !content){
        document.getElementById("message").innerText =
            "Title and Content are required";
        return;
    }

    try{

        // STEP 1: CREATE POST
        let response = await fetch(
            `http://localhost:8080/api/posts/categories/${categoryId}`,
            {
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                    "Authorization":"Bearer " + token
                },
                body:JSON.stringify({
                    title:title,
                    content:content
                })
            }
        );

        if(!response.ok){
            document.getElementById("message").innerText =
                "Failed to create post";
            return;
        }

        let data = await response.json();
        let postId = data.postId;

        // STEP 2: UPLOAD IMAGE (optional)
        if(imageFile){

            let formData = new FormData();
            formData.append("image", imageFile);

            let imageResponse = await fetch(
                `http://localhost:8080/api/posts/image/upload/${postId}`,
                {
                    method:"POST",
                    headers:{
                        "Authorization":"Bearer " + token
                    },
                    body:formData
                }
            );

            if(!imageResponse.ok){
                console.log("Image upload failed");
            }
        }

        document.getElementById("message").innerText =
            "Post Created Successfully";

        setTimeout(()=>{
            window.location.href = "/html/dashboard.html";
        },1000);

    }catch(error){

        console.error(error);

        document.getElementById("message").innerText =
            "Server error";
    }
}