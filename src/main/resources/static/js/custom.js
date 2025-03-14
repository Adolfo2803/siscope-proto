// src/main/resources/static/js/custom.js

document.addEventListener('DOMContentLoaded', function() {
    // Auto-cerrar alertas después de 5 segundos
    setTimeout(function() {
        let alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            let bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Activar todos los tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Focus automático en el primer campo de los formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        const firstInput = form.querySelector('input:not([type=hidden]), select, textarea');
        if (firstInput) {
            firstInput.focus();
        }
    });

    // Confirmación para eliminación de registros
    const deleteButtons = document.querySelectorAll('a[href*="eliminar"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Está seguro de eliminar este registro? Esta acción no puede deshacerse.')) {
                e.preventDefault();
            }
        });
    });

    // Validación de contraseñas coincidentes en el formulario de usuarios
    const passwordForm = document.querySelector('form[action*="/usuario"]');
    if (passwordForm) {
        const password = passwordForm.querySelector('#password');
        const confirmPassword = passwordForm.querySelector('#confirmPassword');

        if (password && confirmPassword) {
            passwordForm.addEventListener('submit', function(e) {
                if (password.value !== confirmPassword.value) {
                    e.preventDefault();
                    alert('Las contraseñas no coinciden. Por favor, inténtelo de nuevo.');
                    password.value = '';
                    confirmPassword.value = '';
                    password.focus();
                }
            });
        }
    }

    // Formato de fechas en campos de fecha
    const fechaInputs = document.querySelectorAll('input[type="date"]');
    fechaInputs.forEach(input => {
        if (!input.value) {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            input.value = `${year}-${month}-${day}`;
        }
    });

    // Verificación de rangos de fechas en formularios de ausencias
    const ausenciaForm = document.querySelector('form[action*="/ausencia"]');
    if (ausenciaForm) {
        const fechaInicio = ausenciaForm.querySelector('#fechaInicio');
        const fechaFin = ausenciaForm.querySelector('#fechaFin');

        if (fechaInicio && fechaFin) {
            ausenciaForm.addEventListener('submit', function(e) {
                if (new Date(fechaInicio.value) > new Date(fechaFin.value)) {
                    e.preventDefault();
                    alert('La fecha de inicio no puede ser posterior a la fecha de fin.');
                }
            });
        }
    }

    // Filtrado dinámico de tablas
    const filterInput = document.getElementById('tableFilter');
    if (filterInput) {
        filterInput.addEventListener('keyup', function() {
            const filterValue = filterInput.value.toLowerCase();
            const table = document.querySelector('table');
            const rows = table.querySelectorAll('tbody tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.indexOf(filterValue) > -1) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    }
});