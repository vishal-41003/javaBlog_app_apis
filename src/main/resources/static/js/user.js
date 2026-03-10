const token = localStorage.getItem("token");

window.onload = function () {
    loadUsers();
};

async function loadUsers() {
    let response = await fetch("http://localhost:8080/api/users", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const users = await response.json();

    let container = document.getElementById("usersList");
    container.innerHTML = "";

    if (!users || users.length === 0) {
        container.innerHTML = "<tr><td colspan='5'>No users available.</td></tr>";
        return;
    }

    users.forEach(user => {
        let tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.about.length > 50 ? user.about.slice(0,50) + "..." : user.about}</td>
            <td>
                <button class="delete-btn" onclick="deleteUser(${user.id})">Delete</button>
            </td>
        `;

        container.appendChild(tr);
    });
}

async function createUser() {
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let about = document.getElementById("about").value;

    let res = await fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ name, email, password, about })
    });

    let message = document.getElementById("message");

    if (res.ok) {
        message.innerText = "User created successfully";
        loadUsers();
    } else {
        message.innerText = "Error creating user";
    }
}

async function deleteUser(id) {
    let response = await fetch(`http://localhost:8080/api/users/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    if (response.ok) {
        loadUsers();
    } else {
        alert("Error deleting user");
    }
}

// Logout
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}