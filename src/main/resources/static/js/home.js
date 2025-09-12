document.addEventListener("DOMContentLoaded", () => {
    console.log("Trang chủ PNJ demo loaded ✔️");

    document.querySelectorAll(".btn-add").forEach(btn => {
        btn.addEventListener("click", () => {
            alert("Sản phẩm đã được thêm vào giỏ hàng 🛒");
        });
    });
});
document.addEventListener('click', function(e){
    const btn = e.target.closest('form[action^="/cart/add/"] button[type="submit"]');
    if(!btn) return;

    const form = btn.closest('form');
    e.preventDefault();

    // Lấy URL add + thêm ajax=1
    const url = form.getAttribute('action') + '?ajax=1';
    const csrfName = form.querySelector('input[type="hidden"]').getAttribute('name');
    const csrfVal  = form.querySelector('input[type="hidden"]').value;

    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ [csrfName]: csrfVal })
    })
        .then(r => r.json())
        .then(data => {
            if(data.ok){
                // cập nhật badge trên header
                let badge = document.querySelector('.cart-count');
                if(!badge){
                    const link = document.querySelector('a[href="/cart"], a[th\\:href="@{/cart}"]') || document.querySelector('.cart-link');
                    if(link){
                        badge = document.createElement('span');
                        badge.className = 'position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger cart-count';
                        link.classList.add('position-relative');
                        link.appendChild(badge);
                    }
                }
                if(badge){
                    badge.textContent = data.count;
                    badge.style.display = (data.count > 0) ? '' : 'none';
                }
            } else {
                window.location.href = '/login';
            }
        })
        .catch(() => window.location.reload());
});

// ===== Countdown (giữ bản gọn) =====
function tick() {
    document.querySelectorAll(".fs-countdown").forEach((el) => {
        const t = new Date(el.dataset.time).getTime();
        let diff = t - Date.now();
        if (diff < 0) diff = 0;

        const h = Math.floor(diff / 3600000);
        const m = Math.floor((diff % 3600000) / 60000);
        const s = Math.floor((diff % 60000) / 1000);

        const set = (sel, val) => {
            const node = el.querySelector(sel);
            if (node) node.textContent = String(val).padStart(2, "0");
        };
        set('[data-part="h"]', h);
        set('[data-part="m"]', m);
        set('[data-part="s"]', s);
    });
}

setInterval(tick, 1000);
tick();

// ===== Tabs ngày + scroller cho flash sale (nếu có) =====
(function initFlashSaleTabs() {
    const tabs = Array.from(document.querySelectorAll(".fs-tab"));
    const scroller = document.getElementById("fs-scroller");
    const prevBtn = document.querySelector(".fs-prev");
    const nextBtn = document.querySelector(".fs-next");

    if (!tabs.length || !scroller) return;

    function filterByDay(dayIndex) {
        document.querySelectorAll(".flashsale-item").forEach((card) => {
            const show = String(card.dataset.day) === String(dayIndex);
            card.style.display = show ? "" : "none";
        });
        scroller.scrollTo({left: 0, behavior: "auto"});
        updateNavButtons();
    }

    function updateNavButtons() {
        if (!prevBtn || !nextBtn) return;
        const max = scroller.scrollWidth - scroller.clientWidth;
        prevBtn.disabled = scroller.scrollLeft <= 0;
        nextBtn.disabled = scroller.scrollLeft >= max - 1;
    }

    tabs.forEach((tab, idx) => {
        tab.addEventListener("click", () => {
            tabs.forEach((t) => t.classList.remove("active"));
            tab.classList.add("active");
            filterByDay(idx);
        });
    });

    if (prevBtn && nextBtn) {
        prevBtn.addEventListener("click", () => {
            scroller.scrollBy({left: -scroller.clientWidth * 0.9, behavior: "smooth"});
            setTimeout(updateNavButtons, 400);
        });
        nextBtn.addEventListener("click", () => {
            scroller.scrollBy({left: scroller.clientWidth * 0.9, behavior: "smooth"});
            setTimeout(updateNavButtons, 400);
        });
        scroller.addEventListener("scroll", updateNavButtons);
    }

    const defaultTab = document.querySelector(".fs-tab.active") || tabs[0];
    if (defaultTab) defaultTab.click();
    else updateNavButtons();
})();
// js giup ap dung radio checkbox ngay lap tuc
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector(".filter-box");
    // Auto submit khi đổi checkbox, radio, select
    form.querySelectorAll("input[type=checkbox], input[type=radio], select")
        .forEach(el => el.addEventListener("change", () => form.submit()));
});


// ===== Detail page: gallery behavior (nếu có) =====
document.addEventListener("DOMContentLoaded", () => {
    const thumbs = document.querySelectorAll(".thumb-list img.thumb");
    const main = document.getElementById("mainImage");
    if (!thumbs.length || !main) return;

    thumbs.forEach((t) => t.classList.remove("active"));
    thumbs[0].classList.add("active");

    thumbs.forEach((img) => {
        img.addEventListener("click", () => {
            thumbs.forEach((i) => i.classList.remove("active"));
            img.classList.add("active");
            main.src = img.currentSrc || img.src;
            main.alt = img.alt || "main";
        });
    });
});


