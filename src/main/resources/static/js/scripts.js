document.getElementById("button-signup").addEventListener("click", function () {
    const userId = document.getElementById("userId").value; // HTML에 맞게 "userId"로 수정
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다!");
        return;
    }

    if (!userId || !username || !password) {
        alert("모든 필드를 입력해 주세요.");
        return;
    }

    const signupData = {
        userId: userId,  // userId 추가
        username: username,
        password: password
    };

    fetch("/api/auth/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(signupData)
    })
        .then(response => response.text())
        .then(data => {
            alert(data);
        })
        .catch(error => console.error("Error:", error));
});



function sendToSoccerAI() {
    var input = document.getElementById("searchInput").value;
    localStorage.setItem("soccerAIQuestion", input);
    window.location.href = "soccer%20ai.html";
}

fetch("/api/auth/current-user", {
    method: "GET",
    credentials: "include",  // 세션 쿠키를 포함하여 요청
    headers: {
        "Content-Type": "application/json"
    }
})
fetch("http://localhost:8080/perform_login", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({ userId: userId, password: password })
})
