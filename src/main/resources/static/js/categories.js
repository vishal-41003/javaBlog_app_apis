const token = localStorage.getItem("token");

window.onload = function(){
    loadCategories();
};

async function createCategory(){

    let title = document.getElementById("title").value;
    let description = document.getElementById("description").value;

    let response = await fetch("http://localhost:8080/api/categories",{

        method:"POST",

        headers:{
            "Content-Type":"application/json",
            "Authorization":"Bearer "+token
        },

        body:JSON.stringify({
            categoryTitle:title,
            categoryDescription:description
        })

    });

    if(response.ok){

        document.getElementById("message").innerText="Category Created";

        loadCategories();

    }else{

        document.getElementById("message").innerText="Error creating category";

    }

}

async function loadCategories(){

    let response = await fetch("http://localhost:8080/api/categories");

    let categories = await response.json();

    let container = document.getElementById("categoryList");

    container.innerHTML="";

    categories.forEach(cat=>{

        let div = document.createElement("div");

        div.className="category-card";

        div.innerHTML=`
        <h3>${cat.categoryTitle}</h3>
        <p>${cat.categoryDescription}</p>
        <button class="delete-btn" onclick="deleteCategory(${cat.categoryId})">
        Delete
        </button>
        `;

        container.appendChild(div);

    });

}

async function deleteCategory(id){

    let response = await fetch(
    `http://localhost:8080/api/categories/${id}`,{

        method:"DELETE",

        headers:{
            "Authorization":"Bearer "+token
        }

    });

    if(response.ok){

        loadCategories();

    }else{

        alert("Error deleting category");

    }

}