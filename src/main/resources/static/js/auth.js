import { initAddressPicker } from './addressPicker.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');
    if (!form) return;

    // ===== Helpers feedback =====
    function getFeedback(el) {
        let n = el.nextElementSibling, ok = null, err = null;
        while (n) {
            if (n.classList?.contains('valid-feedback')) ok = n;
            if (n.classList?.contains('invalid-feedback')) err = n;
            n = n.nextElementSibling;
        }
        return { ok, err };
    }

    const hideServerErrorFor = (el) => {
        const sev = document.querySelector(`[data-server-error="${el.id}"]`);
        if (sev) sev.classList.add('d-none');
    };

    // ===== Validators =====
    const validators = {
        email(el) {
            const v = el.value.trim().toLowerCase();
            if (!v) { el.setCustomValidity(''); return; }
            const gmailOnly = /^[A-Za-z0-9._%+-]+@gmail\.com$/i.test(v);
            el.setCustomValidity(gmailOnly ? '' : 'Chỉ chấp nhận email @gmail.com.');
        },
        phone(el) {
            const v = el.value.trim();
            if (!v) { el.setCustomValidity(''); return; }
            const vn = /^(03|05|07|08|09)[0-9]{8}$/;
            el.setCustomValidity(vn.test(v) ? '' : 'Số điện thoại Việt Nam không hợp lệ (VD: 0912345678).');
        },
        confirmPassword(el) {
            const pw = document.getElementById('password');
            el.setCustomValidity(pw && el.value === pw.value ? '' : 'Mật khẩu nhập lại chưa khớp.');
        },
        password(el) {
            const cf = document.getElementById('confirmPassword');
            if (cf && cf.value) {
                cf.setCustomValidity(el.value === cf.value ? '' : 'Mật khẩu nhập lại chưa khớp.');
                updateState(cf);
            }
        }
    };

    function updateState(el) {
        const { ok, err } = getFeedback(el);
        const val = (el.value ?? '').trim();
        el.classList.remove('is-valid', 'is-invalid');
        if (ok) ok.style.display = 'none';
        if (err) err.style.display = 'none';
        if (!val) { el.setCustomValidity(''); return; }

        const id = el.id;
        if (id === 'email') validators.email(el);
        if (id === 'phone') validators.phone(el);
        if (id === 'confirmPassword') validators.confirmPassword(el);
        if (id === 'password') validators.password(el);

        const good = el.checkValidity();
        if (good) {
            el.classList.add('is-valid');
            if (ok) ok.style.display = 'block';
            hideServerErrorFor(el);
        } else {
            el.classList.add('is-invalid');
            if (err) err.style.display = 'block';
        }
    }

    const fields = form.querySelectorAll('input, select, textarea');
    fields.forEach((el) => {
        el.addEventListener('input', () => { hideServerErrorFor(el); updateState(el); });
        el.addEventListener('blur', () => updateState(el));
    });

    form.addEventListener('submit', (e) => {
        let firstInvalid = null;
        fields.forEach((el) => {
            updateState(el);
            if (!firstInvalid && !el.checkValidity()) firstInvalid = el;
        });
        if (!form.checkValidity()) {
            e.preventDefault(); e.stopPropagation();
            if (firstInvalid) firstInvalid.focus();
        }
    });

    // ===== Địa chỉ =====
    initAddressPicker({
        provinceEl: document.getElementById('addressProvince'),
        wardEl: document.getElementById('addressWard'),
        streetEl: document.getElementById('addressStreet'),
        addressEl: document.getElementById('address'),
        groupDistrict: document.getElementById('groupDistrict'),
        groupWard: document.getElementById('groupWard'),
        provinceNameHidden: document.getElementById('provinceName'),
        districtNameHidden: document.getElementById('districtName'),
        wardNameHidden: document.getElementById('wardName'),
    });

    // ===== GỬI OTP =====
    const btn = document.getElementById('btnSendOtp');
    const email = document.getElementById('email');
    const help = document.getElementById('otpHelp');

    let timer = null, left = 0;
    function startCountdown(sec = 60) {
        left = sec;
        btn.disabled = true;
        help.textContent = `Đã gửi OTP. Vui lòng kiểm tra email. (${left}s)`;
        timer = setInterval(() => {
            left--;
            help.textContent = `Đã gửi OTP. (${left}s)`;
            if (left <= 0) {
                clearInterval(timer);
                btn.disabled = false;
                help.textContent = 'Bạn có thể gửi lại OTP.';
            }
        }, 1000);
    }

    btn?.addEventListener('click', async () => {
        const v = email.value.trim();
        if (!v) { help.textContent = 'Vui lòng nhập email trước khi gửi OTP.'; email.focus(); return; }
        if (!v.endsWith('@gmail.com')) { help.textContent = 'Chỉ chấp nhận email @gmail.com.'; email.focus(); return; }

        // ✅ Lấy CSRF token từ meta trong head
        const token = document.querySelector('meta[name="_csrf"]')?.content;
        const header = document.querySelector('meta[name="_csrf_header"]')?.content;
        const params = new URLSearchParams({ email: v });

        help.textContent = 'Đang gửi...';
        btn.disabled = true;

        try {
            const resp = await fetch('/register/request-otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    [header]: token // gửi kèm token CSRF
                },
                body: params
            });

            const text = await resp.text();

            if (resp.status === 403) {
                help.textContent = '⚠️ Bị chặn CSRF. Kiểm tra meta _csrf hoặc cấu hình SecurityConfig.';
                btn.disabled = false;
                return;
            }

            if (text === 'EXISTS') {
                help.textContent = '❌ Email đã tồn tại, hãy dùng email khác.';
                btn.disabled = false;
                return;
            }
            if (text === 'INVALID_EMAIL') {
                help.textContent = '❌ Email không hợp lệ.';
                btn.disabled = false;
                return;
            }
            if (text === 'OK') {
                startCountdown(60);
                help.textContent = '✅ OTP đã được gửi! Kiểm tra email của bạn.';
                return;
            }

            help.textContent = `❌ Gửi OTP thất bại (${resp.status}): ${text}`;
            btn.disabled = false;
        } catch (e) {
            help.textContent = '⚠️ Lỗi mạng: ' + e.message;
            btn.disabled = false;
        }
    });
});
