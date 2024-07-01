document.querySelectorAll('.sidebar-btn').forEach(button => {
    button.addEventListener('click', () => {
        const subMenu = button.nextElementSibling;
        const allSubMenus = document.querySelectorAll('.sub-menu');

        allSubMenus.forEach(menu => {
            if (menu !== subMenu) {
                menu.style.display = 'none';
            }
        });

        if (subMenu.style.display === 'block') {
            subMenu.style.display = 'none';
        } else {
            subMenu.style.display = 'block';
        }
    });
});
