const API = "http://localhost:8080/api"

const token = localStorage.getItem("token")

async function loadDashboard(){

    const userRes = await fetch(API+"/users",{
        headers:{
            "Authorization":"Bearer "+token
        }
    })

    const users = await userRes.json()

    document.getElementById("usersCount").innerText = users.length



    const postRes = await fetch(API+"/posts",{
        headers:{
            "Authorization":"Bearer "+token
        }
    })

    const posts = await postRes.json()

    document.getElementById("postsCount").innerText = posts.length



    const catRes = await fetch(API+"/categories",{
        headers:{
            "Authorization":"Bearer "+token
        }
    })

    const categories = await catRes.json()

    document.getElementById("categoriesCount").innerText = categories.length

}

loadDashboard()