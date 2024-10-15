// 회원가입 관련 코드 (유지)
document.getElementById("button-signup").addEventListener("click", function () {
    const nickname = document.getElementById("nickname").value;
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    // 비밀번호가 동일한지 확인
    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    // 서버로 아이디와 닉네임 중복 확인 요청
    checkDuplicate(nickname, username, function (isDuplicate) {
        if (isDuplicate) {
            alert("아이디 또는 닉네임이 이미 존재합니다.");
        } else {
            // 비밀번호가 일치하고 중복이 없을 때 회원가입 처리
            registerUser(nickname, username, password);
        }
    });
});

// 서버로 중복 확인 요청하는 함수
function checkDuplicate(nickname, username, callback) {
    fetch('/check-duplicate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            nickname: nickname,
            username: username,
        }),
    })
        .then(response => response.json())
        .then(data => {
            callback(data.isDuplicate);
        })
        .catch(error => {
            alert("중복 확인 중 오류가 발생했습니다.");
            console.error('Error:', error);
        });
}

// 회원가입 요청을 서버로 보내는 함수
function registerUser(nickname, username, password) {
    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            nickname: nickname,
            username: username,
            password: password,
        }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("회원가입이 완료되었습니다.");
                window.location.href = '/login'; // 회원가입 후 로그인 페이지로 이동
            } else {
                alert("회원가입 중 오류가 발생했습니다.");
            }
        })
        .catch(error => {
            alert("회원가입 요청 중 오류가 발생했습니다.");
            console.error('Error:', error);
        });
}

// 아래에 로그인 후 닉네임 표시 관련 코드 추가
document.getElementById("button-login").addEventListener("click", function () {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // 로그인 요청을 서버에 보냄
    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 로그인 성공 시: 로그인 폼을 숨기고 닉네임을 표시
                document.getElementById("login-form").style.display = "none";  // 로그인 폼 숨기기
                document.getElementById("welcome-message").style.display = "block";  // 닉네임 표시 영역 보이기
                document.getElementById("nickname").textContent = data.nickname;  // 닉네임 업데이트
            } else {
                alert("로그인 실패: 아이디와 비밀번호를 확인하세요.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

// 로그아웃 처리 (로그아웃 버튼을 클릭하면 세션을 종료하고 로그인 폼으로 돌아감)
document.getElementById("logout").addEventListener("click", function () {
    // 서버에 로그아웃 요청
    fetch('/logout', {
        method: 'POST',
    })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공 시: 로그인 폼을 다시 보여줌
                document.getElementById("login-form").style.display = "block";  // 로그인 폼 보이기
                document.getElementById("welcome-message").style.display = "none";  // 닉네임 영역 숨기기
                document.getElementById("username").value = "";  // 입력 필드 초기화
                document.getElementById("password").value = "";  // 입력 필드 초기화
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
