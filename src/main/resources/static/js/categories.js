const token = localStorage.getItem("token");

window.onload = function() {
    loadCategories();
};

// Create category
async function createCategory() {
    let title = document.getElementById("title").value;
    let description = document.getElementById("description").value;

    let response = await fetch("http://localhost:8080/api/categories", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            categoryTitle: title,
            categoryDescription: description
        })
    });

    let message = document.getElementById("message");
    if (response.ok) {
        message.innerText = "Category Created Successfully";
        loadCategories();
    } else {
        message.innerText = "Error creating category";
    }
}

// Load categories in table
async function loadCategories() {
    let response = await fetch("http://localhost:8080/api/categories", {
        headers: { "Authorization": "Bearer " + token }
    });

    let container = document.getElementById("categoriesList");
    container.innerHTML = "";

    if (!response.ok) {
        container.innerHTML = `<tr><td colspan="4">Error loading categories</td></tr>`;
        return;
    }

    let categories = await response.json();

    categories.forEach(cat => {
        let tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${cat.categoryId}</td>
            <td>${cat.categoryTitle}</td>
            <td>${cat.categoryDescription}</td>
            <td>
                <button class="delete-btn" onclick="deleteCategory(${cat.categoryId})">Delete</button>
            </td>
        `;
        container.appendChild(tr);
    });
}

// Delete category
async function deleteCategory(id) {
    let response = await fetch(`http://localhost:8080/api/categories/${id}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token }
    });

    if (response.ok) {
        loadCategories();
    } else {
        alert("Error deleting category");
    }
}

// Logout
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}