

// Chạy khi DOM sẵn sàng
document.addEventListener('DOMContentLoaded', function () {
    // ===== Bootstrap validation =====
    (function () {
        'use strict';
        var forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();

    // ===== Toggle show/hide password =====
    var pass = document.getElementById('password');
    var toggle = document.getElementById('togglePass');
    if (toggle && pass) {
        toggle.addEventListener('click', function () {
            var isText = pass.getAttribute('type') === 'text';
            pass.setAttribute('type', isText ? 'password' : 'text');
            toggle.textContent = isText ? 'Hiện' : 'Ẩn';
        });
    }
});
