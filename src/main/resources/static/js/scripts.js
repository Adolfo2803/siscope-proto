/* src/main/resources/static/js/scripts.js */

// Función para activar los enlaces del navbar y sidebar
document.addEventListener('DOMContentLoaded', function() {
    // Obtener la URL actual
    var currentUrl = window.location.pathname;

    // Determinar qué pestaña está activa según la URL
    var activeTab = '';

    if (currentUrl.includes('/dashboard')) {
        activeTab = 'dashboard';
    } else if (currentUrl.includes('/enfermera')) {
        activeTab = 'enfermera';
    } else if (currentUrl.includes('/ausencia')) {
        activeTab = 'ausencia';
    } else if (currentUrl.includes('/concepto')) {
        activeTab = 'concepto';
    } else if (currentUrl.includes('/reporte')) {
        activeTab = 'reporte';
    } else if (currentUrl.includes('/usuario')) {
        activeTab = 'usuario';
    }

    // Activar los enlaces del navbar
    var navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    navLinks.forEach(function(link) {
        var href = link.getAttribute('href');
        if (href && href.includes('/' + activeTab)) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });

    // Activar los enlaces del sidebar
    var sidebarLinks = document.querySelectorAll('.sidebar .nav-link');
    sidebarLinks.forEach(function(link) {
        var href = link.getAttribute('href');
        if (href && href.includes('/' + activeTab)) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
});