// 로그인 버튼 클릭 시 처리
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
                })
                    .then(authResponse => {
                        if (authResponse.ok) {
                            authResponse.json().then(data => {
                                document.getElementById("login-form").style.display = "none";
                                document.getElementById("welcome-message").style.display = "block";
                                document.getElementById("username").innerText = data.username ? data.username : userId;
                                document.getElementById("point").innerText = data.points !== undefined ? data.points : "0";

                                if (data.nicknameColor) {
                                    document.getElementById("username").style.color = data.nicknameColor;
                                }

                                if (data.teamLogo) {
                                    updateLoginLogo(data.teamLogo);
                                }

                                sessionStorage.setItem("isAuthenticated", "true");
                                sessionStorage.setItem("points", data.points);
                                sessionStorage.setItem("nicknameColor", data.nicknameColor);
                                sessionStorage.setItem("teamLogo", data.teamLogo);
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

// 닉네임 앞에 로고를 추가하거나 숨기는 함수
function updateLoginLogo(logoFileName) {
    const logoElement = document.getElementById("user-logo");
    if (logoFileName) {
        logoElement.innerHTML = `<img src="images/${logoFileName}" alt="Team Logo" style="width: 20px; height: 20px; margin-right: 5px;" />`;
        logoElement.style.display = "inline";
    } else {
        logoElement.style.display = "none";
    }
}

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

// 로그인 후 또는 새로고침 시 최신 정보를 서버에서 받아와 세션 스토리지에 저장
function fetchAndUpdateUserData() {
    fetch("/api/check-auth", {
        method: "GET",
        credentials: "include"
    })
        .then(response => {
            if (response.ok) {
                response.json().then(data => {
                    sessionStorage.setItem("userId", data.userId);
                    sessionStorage.setItem("points", data.points);
                    sessionStorage.setItem("nicknameColor", data.nicknameColor);
                    sessionStorage.setItem("teamLogo", data.teamLogo);
                    sessionStorage.setItem("isAuthenticated", "true");

                    document.getElementById("login-form").style.display = "none";
                    document.getElementById("welcome-message").style.display = "block";
                    document.getElementById("username").innerText = data.username;
                    document.getElementById("point").innerText = data.points;
                    document.getElementById("username").style.color = data.nicknameColor || "";
                    updateLoginLogo(data.teamLogo);
                });
            } else {
                document.getElementById("login-form").style.display = "block";
                document.getElementById("welcome-message").style.display = "none";
                sessionStorage.clear();
            }
        }).catch(error => {
        console.error("인증 상태 확인 중 오류 발생:", error);
        sessionStorage.clear();
        document.getElementById("login-form").style.display = "block";
        document.getElementById("welcome-message").style.display = "none";
    });
}


document.querySelectorAll(".team-logo").forEach(logo => {
    logo.addEventListener("click", function() {
        const logoFileName = this.getAttribute("data-logo");
        const currentPoints = parseInt(sessionStorage.getItem("points") || "0", 10);
        const userId = sessionStorage.getItem("userId");

        const confirmChange = confirm("로고를 변경하시겠습니까?");
        if (!confirmChange) {
            return;
        }

        if (currentPoints >= 300) {
            fetch("/api/update-team-logo", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ userId: userId, logo: logoFileName, points: currentPoints - 300 }),
                credentials: "include"
            })
                .then(response => response.json())
                .then(data => {
                    if (data.message === "로고가 업데이트되었습니다.") {
                        sessionStorage.setItem("points", currentPoints - 300);
                        sessionStorage.setItem("teamLogo", logoFileName);
                        document.getElementById("point").innerText = currentPoints - 300;

                        updateLoginLogo(logoFileName);

                        alert("로고가 성공적으로 변경되었습니다!");
                    } else {
                        alert("업데이트 실패: " + data.message);
                    }
                })
                .catch(error => {
                    console.error("로고 업데이트 중 오류 발생:", error);
                    alert("서버와의 통신에 문제가 발생했습니다.");
                });
        } else {
            alert("포인트가 부족합니다. 300포인트 이상 필요합니다.");
        }
    });
});


// 닉네임 앞에 로고를 추가하거나 숨기는 함수
function updateLoginLogo(logoFileName) {
    const logoElement = document.getElementById("user-logo");

    if (logoFileName) {
        logoElement.innerHTML = `<img src="images/${logoFileName}" alt="Team Logo" style="width: 20px; height: 20px; margin-right: 5px;" />`;
        logoElement.style.display = "inline";
    } else {
        logoElement.style.display = "none";
    }
}

window.onload = function() {
    if (sessionStorage.getItem("isAuthenticated") === "true") {
        fetch("/api/check-auth", {
            method: "GET",
            credentials: "include"
        })
            .then(response => response.json())
            .then(data => {
                if (data.teamLogo) {
                    sessionStorage.setItem("teamLogo", data.teamLogo);
                    updateLoginLogo(data.teamLogo);
                }
                document.getElementById("login-form").style.display = "none";
                document.getElementById("welcome-message").style.display = "block";
                document.getElementById("username").innerText = data.username;
                document.getElementById("point").innerText = data.points;
            })
            .catch(error => console.error("인증 상태 확인 중 오류 발생:", error));
    } else {
        document.getElementById("login-form").style.display = "block";
        document.getElementById("welcome-message").style.display = "none";
    }
};


// 페이지 로드 시 항상 최신 정보 가져오기
window.onload = function() {
    if (sessionStorage.getItem("isAuthenticated") === "true") {
        fetchAndUpdateUserData();
    } else {
        document.getElementById("login-form").style.display = "block";
        document.getElementById("welcome-message").style.display = "none";
    }
};

// 닉네임 색상 변경 시에도 서버에서 최신 정보를 다시 받아와 업데이트
document.getElementById("change-color-btn").addEventListener("click", function() {
    let currentPoints = parseInt(sessionStorage.getItem("points") || "0", 10);
    const userId = sessionStorage.getItem("userId");
    const randomColor = getRandomColor();

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
                    fetchAndUpdateUserData(); // 업데이트 후 최신 정보 다시 불러오기
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
                    // 업데이트 성공 후 최신 정보로 UI 갱신
                    document.getElementById("shop-message").innerText = `닉네임 색상이 변경되었습니다! 새로운 색상: ${randomColor}`;
                    document.getElementById("shop-message").style.color = randomColor;

                    // 최신 포인트를 반영
                    sessionStorage.setItem("points", currentPoints - 50);
                    document.getElementById("point").innerText = currentPoints - 50;
                    document.getElementById("username").style.color = randomColor;
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