// 사이드바 JS -START- //
let buttons = document.getElementsByClassName('sidebar-btn');

for (let i = 0; i < buttons.length; i++) {
    buttons[i].addEventListener('click', toggleSubMenu);
}

function toggleSubMenu() {
    let subMenu = this.nextElementSibling;
    if (subMenu.classList.contains('show')) {
        subMenu.classList.remove('show');
    } else {
        subMenu.classList.add('show');
    }
}
// 사이드바 JS -END- //