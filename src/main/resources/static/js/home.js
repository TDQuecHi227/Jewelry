document.addEventListener("DOMContentLoaded", () => {
    console.log("Trang chủ PNJ demo loaded ✔️");

    document.querySelectorAll(".btn-add").forEach(btn => {
        btn.addEventListener("click", () => {
            alert("Sản phẩm đã được thêm vào giỏ hàng 🛒");
        });
    });
});

// ===== Collection carousel + filter products by collection =====
document.addEventListener('DOMContentLoaded', () => {
    const track = document.getElementById('collectionTrack');
    if (!track) return;

    const items = Array.from(track.querySelectorAll('.collection-item'));
    const dotsWrap = document.getElementById('collectionDots');
    const prevBtn = document.querySelector('.col-prev');
    const nextBtn = document.querySelector('.col-next');

    // create dots
    items.forEach((it, idx) => {
        const b = document.createElement('button');
        b.type = 'button';
        if (idx === 0) b.classList.add('active');
        b.addEventListener('click', () => setActive(idx));
        dotsWrap.appendChild(b);
    });

    let activeIndex = 0;

    function setActive(index){
        activeIndex = Math.max(0, Math.min(index, items.length-1));

        const spacing = 120; // px offset per item from center

        items.forEach((it,i)=>{
            it.classList.remove('active');
            const delta = i - activeIndex;
            const tx = delta * spacing; // px
            // anchor at center (-50%) then shift by tx
            const scale = delta === 0 ? 1 : Math.max(0.82, 1 - Math.abs(delta) * 0.08);
            it.style.transform = `translateX(calc(-50% + ${tx}px)) scale(${scale})`;
            it.style.zIndex = String(100 - Math.abs(delta));
            if (delta === 0) it.classList.add('active');
        });

        // update dots
        const dots = Array.from(dotsWrap.children);
        dots.forEach((d, i)=> d.classList.toggle('active', i === activeIndex));

        // disable nav at edges
        if (prevBtn) prevBtn.disabled = activeIndex <= 0;
        if (nextBtn) nextBtn.disabled = activeIndex >= items.length - 1;

        // filter products by collection
        const activeEl = items[activeIndex];
        const colId = activeEl && activeEl.dataset ? activeEl.dataset.collectionId : undefined;
        document.querySelectorAll('.product-card').forEach(card => {
            const pid = card.dataset.collectionId;
            card.style.display = (!pid || !colId || pid === colId) ? '' : 'none';
        });
    }

    // nav
    prevBtn && prevBtn.addEventListener('click', ()=> setActive(activeIndex-1));
    nextBtn && nextBtn.addEventListener('click', ()=> setActive(activeIndex+1));

    // click on item
    items.forEach((it, idx)=> it.addEventListener('click', ()=> setActive(idx)));

    // initial
    // ensure layout after images load
    items.forEach(it=>{
        const img = it.querySelector('img');
        if (img && !img.complete) img.addEventListener('load', ()=> setActive(activeIndex));
    });
    // recalc on resize
    window.addEventListener('resize', ()=> setActive(activeIndex));

    // small defer to allow layout/fonts/images
    setTimeout(()=> setActive(3), 60);
});

// ===== Countdown (giữ code bạn đang dùng; dưới đây là bản gọn) =====
function tick() {
    document.querySelectorAll(".fs-countdown").forEach(el => {
        const t = new Date(el.dataset.time).getTime();
        let diff = t - Date.now();
        if (diff < 0) diff = 0;
        const h = Math.floor(diff/3600000);
        const m = Math.floor((diff%3600000)/60000);
        const s = Math.floor((diff%60000)/1000);
        el.querySelector('[data-part="h"]').textContent = String(h).padStart(2,'0');
        el.querySelector('[data-part="m"]').textContent = String(m).padStart(2,'0');
        el.querySelector('[data-part="s"]').textContent = String(s).padStart(2,'0');
    });
}
setInterval(tick, 1000);
tick();

// ===== Tabs ngày: click để lọc sản phẩm theo ngày =====
const tabs = [...document.querySelectorAll(".fs-tab")];
const scroller = document.getElementById("fs-scroller");
const prevBtn = document.querySelector(".fs-prev");
const nextBtn = document.querySelector(".fs-next");

function filterByDay(dayIndex) {
    document.querySelectorAll(".flashsale-item").forEach(card => {
        const show = String(card.dataset.day) === String(dayIndex);
        card.style.display = show ? "" : "none";
    });
    // quay về đầu dải sản phẩm
    scroller.scrollTo({ left: 0, behavior: "instant" in scroller ? "instant" : "auto" });
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
        tabs.forEach(t => t.classList.remove("active"));
        tab.classList.add("active");
        filterByDay(idx);
    });
});

if (prevBtn && nextBtn) {
    prevBtn.addEventListener("click", () => {
        scroller.scrollBy({ left: -scroller.clientWidth * 0.9, behavior: "smooth" });
        setTimeout(updateNavButtons, 400);
    });
    nextBtn.addEventListener("click", () => {
        scroller.scrollBy({ left:  scroller.clientWidth * 0.9, behavior: "smooth" });
        setTimeout(updateNavButtons, 400);
    });
    scroller.addEventListener("scroll", updateNavButtons);
}

// Chọn mặc định ngày đầu (nếu chưa active)
const defaultTab = document.querySelector(".fs-tab.active") || tabs[0];
if (defaultTab) defaultTab.click();
else updateNavButtons();

// ========== FILTER ==========

// ======= FILTER (v2: chịu được cả #productGrid hoặc .product-grid) =======
document.addEventListener('DOMContentLoaded', () => {
    // 1) Bắt grid: ưu tiên #productGrid, nếu không có dùng .product-grid
    const grid = document.getElementById('productGrid') || document.querySelector('.product-grid');
    if (!grid) {
        console.warn('[Filter] Không thấy grid (#productGrid hoặc .product-grid).');
        return;
    }

    // 2) Lấy phần tử sản phẩm
    // - Nếu là layout Bootstrap, mỗi item nằm trong .col → show/hide trên .col
    // - Nếu là CSS grid cũ, item chính là .product-card → show/hide trực tiếp
    let cards = Array.from(grid.querySelectorAll('.product-card'));
    if (cards.length === 0) {
        // fallback: có thể bạn bọc card bằng .card khác
        cards = Array.from(grid.querySelectorAll('.card.product-card, .card.h-100'));
    }
    if (cards.length === 0) {
        console.warn('[Filter] Không thấy .product-card trong grid.');
        return;
    }

    // Mỗi "itemNode" là node để ẩn/hiện; "cardNode" là thẻ chứa data
    const items = cards.map(card => ({
        cardNode: card,
        itemNode: card.closest('.col') || card  // nếu không có .col thì chính nó
    }));

    // 3) Input control (nếu bạn chưa thêm UI filter thì các phần tử có thể null → code vẫn chạy)
    const qEl     = document.getElementById('q');             // ô tìm kiếm
    const minEl   = document.getElementById('minPrice');
    const maxEl   = document.getElementById('maxPrice');
    const sortEl  = document.getElementById('sort');
    const clearBtn= document.getElementById('btnClearFilters');

    // 4) Chuẩn hoá data-attr: nếu thiếu thì tự điền từ DOM
    const toNumber = (s) => parseInt(String(s).replace(/[^\d]/g, ''), 10) || 0;
    items.forEach(({cardNode}) => {
        if (!cardNode.dataset.name) {
            const nameEl =
                cardNode.querySelector('h3, .product-title, .card-title') ||
                cardNode.querySelector('[data-name-text]');
            cardNode.dataset.name = nameEl ? nameEl.textContent.trim() : '';
        }
        if (!cardNode.dataset.price) {
            const priceEl = cardNode.querySelector('.price, .price-new, .text-danger');
            if (priceEl) cardNode.dataset.price = toNumber(priceEl.textContent);
        }
    });

    // 5) Lấy danh mục (checkbox name="cat")
    const getCats = () =>
        Array.from(document.querySelectorAll('input[name="cat"]:checked'))
            .map(i => i.value.toLowerCase());

    const matchCat = (name, cats) =>
        cats.length === 0 ? true : cats.some(c => name.toLowerCase().includes(c));

    const currentMin = () => (minEl && minEl.value) ? parseInt(minEl.value,10) : 0;
    const currentMax = () => (maxEl && maxEl.value) ? parseInt(maxEl.value,10) : Number.POSITIVE_INFINITY;

    function filter() {
        const term = (qEl && qEl.value ? qEl.value : '').toLowerCase().trim();
        const cats = getCats();
        const min  = currentMin();
        const max  = currentMax();

        items.forEach(({cardNode, itemNode}) => {
            const name  = (cardNode.dataset.name || '').toLowerCase();
            const price = toNumber(cardNode.dataset.price);

            const okText  = term === '' || name.includes(term);
            const okCat   = matchCat(name, cats);
            const okPrice = price >= min && price <= max;

            itemNode.style.display = (okText && okCat && okPrice) ? '' : 'none';
        });

        sort(); // sắp xếp lại các item đang hiển thị
    }

    function sort() {
        if (!sortEl) return;
        const mode = sortEl.value;
        let visible = items.filter(({itemNode}) => itemNode.style.display !== 'none');

        const getP = n => toNumber(n.cardNode.dataset.price);
        const getN = n => (n.cardNode.dataset.name || '').toLowerCase();

        if (mode === 'priceAsc')  visible.sort((a,b)=>getP(a)-getP(b));
        if (mode === 'priceDesc') visible.sort((a,b)=>getP(b)-getP(a));
        if (mode === 'nameAsc')   visible.sort((a,b)=>getN(a).localeCompare(getN(b)));
        if (mode === 'nameDesc')  visible.sort((a,b)=>getN(b).localeCompare(getN(a)));

        // append theo wrapper thực tế (itemNode)
        visible.forEach(({itemNode}) => grid.appendChild(itemNode));
    }

    // Nút chọn nhanh khoảng giá
    document.querySelectorAll('.badge-range').forEach(btn=>{
        btn.addEventListener('click', ()=>{
            if (minEl) minEl.value = btn.dataset.min || '';
            if (maxEl) maxEl.value = btn.dataset.max || '';
            filter();
        });
    });

    // Events
    qEl     && qEl.addEventListener('input', filter);
    minEl   && minEl.addEventListener('input', filter);
    maxEl   && maxEl.addEventListener('input', filter);
    sortEl  && sortEl.addEventListener('change', filter);
    document.querySelectorAll('input[name="cat"]').forEach(cb=>cb.addEventListener('change', filter));
    clearBtn && clearBtn.addEventListener('click', ()=>{
        if (qEl) qEl.value = '';
        if (minEl) minEl.value = '';
        if (maxEl) maxEl.value = '';
        if (sortEl) sortEl.value = '';
        document.querySelectorAll('input[name="cat"]').forEach(cb=>cb.checked=false);
        filter();
    });

    // first run
    filter();
});
// === Detail page: gallery behavior ===
document.addEventListener('DOMContentLoaded', () => {
    const thumbs = document.querySelectorAll('.thumb-list img.thumb');
    const main   = document.getElementById('mainImage');
    if (!thumbs.length || !main) return;

    // set active cho thumb đầu
    thumbs.forEach(t => t.classList.remove('active'));
    thumbs[0].classList.add('active');

    // click đổi ảnh chính + active viền
    thumbs.forEach(img => {
        img.addEventListener('click', () => {
            thumbs.forEach(i => i.classList.remove('active'));
            img.classList.add('active');
            main.src = img.currentSrc || img.src;   // dùng currentSrc nếu có srcset
            main.alt = img.alt || 'main';
        });
    });
});


