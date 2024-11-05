// 개선된 scripts.js 파일

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
                    sessionStorage.setItem("userId", data.userId);
                    sessionStorage.setItem("points", data.points);
                    sessionStorage.setItem("nicknameColor", data.nicknameColor);
                    sessionStorage.setItem("isAuthenticated", "true");

                    document.getElementById("login-form").style.display = "none";
                    document.getElementById("welcome-message").style.display = "block";
                    document.getElementById("username").innerText = data.username;
                    document.getElementById("point").innerText = data.points;
                    if (data.nicknameColor) {
                        document.getElementById("username").style.color = data.nicknameColor;
                    }
                });
            } else {
                document.getElementById("login-form").style.display = "block";
                document.getElementById("welcome-message").style.display = "none";
                sessionStorage.removeItem("isAuthenticated");
                sessionStorage.removeItem("points");
                sessionStorage.removeItem("nicknameColor");
            }
        }).catch(error => {
            console.error("인증 상태 확인 중 오류 발생:", error);
            document.getElementById("login-form").style.display = "block";
            document.getElementById("welcome-message").style.display = "none";
            sessionStorage.removeItem("isAuthenticated");
            sessionStorage.removeItem("points");
            sessionStorage.removeItem("nicknameColor");
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



function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

document.getElementById("change-color-btn").addEventListener("click", function() {
    let currentPoints = parseInt(sessionStorage.getItem("points") || "0", 10);
    const userId = sessionStorage.getItem("userId");
    const randomColor = getRandomColor();
    console.log("userId:", userId, "color:", randomColor);

    if (currentPoints >= 50) {

        fetch(`/api/update-nickname-color-and-points`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ userId: userId, color: randomColor, points: currentPoints - 50 })
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "닉네임 색상이 업데이트되었습니다.") {
                    currentPoints -= 50;
                    sessionStorage.setItem("points", currentPoints);
                    sessionStorage.setItem("nicknameColor", randomColor);
                    document.getElementById("point").innerText = currentPoints;
                    document.getElementById("username").style.color = randomColor;
                    document.getElementById("shop-message").innerText = `닉네임 색상이 변경되었습니다! 새로운 색상: ${randomColor}`;
                    document.getElementById("shop-message").style.color = randomColor;
                } else {
                    document.getElementById("shop-message").innerText = "업데이트 실패: " + data.message;
                    document.getElementById("shop-message").style.color = "red";
                }
            })
            .catch(error => {
                console.error("Error updating nickname color:", error);
                document.getElementById("shop-message").innerText = "서버와의 통신에 문제가 발생했습니다.";
                document.getElementById("shop-message").style.color = "red";
            });
    } else {
        document.getElementById("shop-message").innerText = "포인트가 부족합니다. 50포인트 이상 필요합니다.";
        document.getElementById("shop-message").style.color = "red";
    }
});
