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