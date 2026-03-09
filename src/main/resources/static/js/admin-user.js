async function loadUsers(){

    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8080/api/users/",{
        headers:{
            "Authorization":"Bearer " + token
        }
    });

    const users = await response.json();

    const table = document.getElementById("userTable");

    table.innerHTML = "";

    users.forEach(user => {

        const row = `
        <tr>
            <td>${user.userId}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.about}</td>
        </tr>
        `;

        table.innerHTML += row;

    });

}

loadUsers();