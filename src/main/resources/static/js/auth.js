// /js/auth.js
document.addEventListener('DOMContentLoaded', function () {
    // Bootstrap validation
    const forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Nếu có confirm password thì kiểm tra khớp
    const password = document.getElementById('password');
    const confirm = document.getElementById('confirmPassword');
    if (password && confirm) {
        confirm.addEventListener('input', function () {
            if (confirm.value !== password.value) {
                confirm.setCustomValidity("Mật khẩu không khớp");
            } else {
                confirm.setCustomValidity("");
            }
        });
    }
});
