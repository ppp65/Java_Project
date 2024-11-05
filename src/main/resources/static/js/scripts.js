document.getElementById("button-login").addEventListener("click", function() {
    const userId = document.getElementById("user_id").value;
    const password = document.getElementById("password").value;

    fetch("/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `user_id=${userId}&password=${password}`,
        credentials: "include"
    })
        .then(response => {
            if (response.ok) {
                fetch("/api/check-auth", {
                    method: "GET",
                    credentials: "include"
                }).then(authResponse => {
                    if (authResponse.ok) {
                        authResponse.json().then(data => {
                            document.getElementById("login-form").style.display = "none";
                            document.getElementById("welcome-message").style.display = "block";
                            document.getElementById("username").innerText = data.username ? data.username : userId;
                            document.getElementById("point").innerText = data.points !== undefined ? data.points : "0";
                            sessionStorage.setItem("isAuthenticated", "true");
                            sessionStorage.setItem("points", data.points);
                        });
                    } else {
                        alert("인증 실패. 다시 로그인하세요.");
                    }
                }).catch(error => {
                    console.error("인증 상태 확인 중 오류 발생:", error);
                });
            } else {
                alert("로그인 실패. 아이디와 비밀번호를 확인하세요.");
            }
        });
});

document.getElementById("logout").addEventListener("click", function() {
    fetch("/api/logout", {
        method: "POST",
        credentials: "include"
    }).then(() => {
        document.getElementById("login-form").style.display = "block";
        document.getElementById("welcome-message").style.display = "none";

        document.getElementById("username").innerText = "";
        sessionStorage.removeItem("isAuthenticated");
    });
});

window.onload = function() {
    if (sessionStorage.getItem("isAuthenticated") === "true") {
        fetch("/api/check-auth", {
            method: "GET",
            credentials: "include"
        }).then(response => {
            if (response.ok) {
                response.json().then(data => {
                    document.getElementById("login-form").style.display = "none";
                    document.getElementById("welcome-message").style.display = "block";
                    document.getElementById("username").innerText = data.username ? data.username : "";

                    const points = sessionStorage.getItem("points") || data.points;
                    document.getElementById("point").innerText = points;
                });
            } else {
                document.getElementById("login-form").style.display = "block";
                document.getElementById("welcome-message").style.display = "none";
                sessionStorage.removeItem("isAuthenticated");
                sessionStorage.removeItem("points");
            }
        }).catch(error => {
            console.error("인증 상태 확인 중 오류 발생:", error);
            document.getElementById("login-form").style.display = "block";
            document.getElementById("welcome-message").style.display = "none";
            sessionStorage.removeItem("isAuthenticated");
            sessionStorage.removeItem("points");
        });
    } else {
        document.getElementById("login-form").style.display = "block";
        document.getElementById("welcome-message").style.display = "none";
    }
};


function sendToSoccerAI() {
    var input = document.getElementById("searchInput").value;
    localStorage.setItem("soccerAIQuestion", input);
    window.location.href = "soccer%20ai.html";
}
