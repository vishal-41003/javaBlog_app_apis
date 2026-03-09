async function createPost(){

    let title = document.getElementById("title").value;
    let content = document.getElementById("content").value;
    let categoryId = document.getElementById("categoryId").value;
    let imageFile = document.getElementById("imageFile").files[0];

    let userId = 1;

    let token = localStorage.getItem("token");

    try{

        // STEP 1: CREATE POST
        let response = await fetch(
            `http://localhost:8080/api/users/${userId}/categories/${categoryId}/posts`,
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
            });

        if(!response.ok){
            document.getElementById("message").innerText =
                "Failed to create post";
            return;
        }

        let data = await response.json();
        let postId = data.postId;

        // STEP 2: UPLOAD IMAGE (if selected)
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
            "Post Created Successfully";

        // redirect to dashboard
        window.location.href = "/html/dashboard.html";

    }catch(error){

        document.getElementById("message").innerText =
            "Server error";

    }

}