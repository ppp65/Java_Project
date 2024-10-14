/* JavaScript: scripts.js */
function showSection(sectionId) {
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.classList.add('hidden');
    });
    document.getElementById(sectionId).classList.remove('hidden');
}

// 기본으로 순위 섹션을 보여주기 위해 호출
document.addEventListener('DOMContentLoaded', () => {
    showSection('ranking');
});

// scripts.js

// 모달 열기
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

// 모달 닫기
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    const loginModal = document.getElementById('loginModal');
    const signupModal = document.getElementById('signupModal');

    if (event.target == loginModal) {
        closeModal('loginModal');
    }
    if (event.target == signupModal) {
        closeModal('signupModal');
    }
}
