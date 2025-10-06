import { initAddressPicker } from './addressPicker.js';
(function(){
    // --- Cấu hình chung ---
    const VAT_RATE = 0.10;               // 10% VAT
    const SHIP_BASE = 30000;             // phí ship mặc định
    const FREESHIP_MIN = 1000000;        // miễn ship nếu tạm tính >= 1,000,000đ

    // --- Helper định dạng ---
    function formatVND(n){
    try {
    return new Intl.NumberFormat('vi-VN').format(Math.round(n)) + ' đ';
} catch(e){
    // fallback
    return (Math.round(n)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + ' đ';
}
}

    // --- Đọc danh sách item để tính subtotal ---
    function getSubtotal(){
    const rows = document.querySelectorAll('#checkoutItems .list-group-item[data-price][data-qty]');
    let sum = 0;
    rows.forEach(r=>{
    const price = Number(r.getAttribute('data-price')) || 0;
    const qty   = Number(r.getAttribute('data-qty'))   || 0;
    sum += price * qty;
});
    return sum;
}

    // --- Tính giảm giá từ mã coupon ---
    function calcDiscount(subtotal, couponCode){
    if(!couponCode) return 0;
    const code = couponCode.trim().toUpperCase();

    // Ví dụ rule:
    // GIAM5   => giảm 5% trên subtotal
    // FIX50K  => giảm 50,000đ
    // FREESHIP=> không giảm tiền hàng, nhưng sẽ miễn phí ship (xử lý dưới)
    switch(code){
    case 'GIAM5':
    return Math.floor(subtotal * 0.05);
    case 'FIX50K':
    return Math.min(50000, subtotal);
    case 'FREESHIP':
    return 0;
    default:
    return 0;
}
}

    // --- Tính phí ship (có ưu đãi) ---
    function calcShipping(subtotal, discount, couponCode){
    // Miễn ship nếu vượt ngưỡng hoặc dùng FREESHIP
    const code = (couponCode||'').trim().toUpperCase();
    if(subtotal >= FREESHIP_MIN) return 0;
    if(code === 'FREESHIP') return 0;
    return SHIP_BASE;
}

    // --- Tính VAT trên (subtotal - discount) ---
    function calcTax(subtotal, discount){
    const base = Math.max(0, subtotal - discount);
    return Math.floor(base * VAT_RATE);
}

    // --- Cập nhật UI + input ẩn ---
    function updateUI({subtotal, discount, shipping, tax, total}){
    // Text
    const $ = id => document.getElementById(id);
    if($('subtotalText')) $('subtotalText').innerText = formatVND(subtotal);
    if($('discountText')) $('discountText').innerText = formatVND(discount);
    if($('shippingText')) $('shippingText').innerText = formatVND(shipping);
    if($('taxText'))      $('taxText').innerText      = formatVND(tax);
    if($('totalText'))    $('totalText').innerText    = formatVND(total);

    // Hidden values để submit server
    if($('subtotalVal')) $('subtotalVal').value = Math.round(subtotal);
    if($('discountVal')) $('discountVal').value = Math.round(discount);
    if($('shippingVal')) $('shippingVal').value = Math.round(shipping);
    if($('taxVal'))      $('taxVal').value      = Math.round(tax);
    if($('totalVal'))    $('totalVal').value    = Math.round(total);
}

    // --- Hàm tổng tính lại ---
    function recalc(){
    const subtotal = getSubtotal();
    const couponCode = (document.getElementById('couponInput')?.value)||'';
    const discount = calcDiscount(subtotal, couponCode);
    const shipping = calcShipping(subtotal, discount, couponCode);
    const tax      = calcTax(subtotal, discount);
    const total    = Math.max(0, subtotal - discount) + shipping + tax;

    updateUI({subtotal, discount, shipping, tax, total});
}

    // --- Gắn event ---
    const couponInput = document.getElementById('couponInput');
    const applyBtn    = document.getElementById('applyCouponBtn');

    if(couponInput){
    couponInput.addEventListener('input', recalc);
}
    if(applyBtn){
    applyBtn.addEventListener('click', recalc);
}

    // Nếu có gì đó thay đổi ở items (SPA/HTMX), bạn có thể gọi recalc() lại.

    // Khởi tạo lần đầu
    recalc();
})();
document.addEventListener('DOMContentLoaded', () => {
    initAddressPicker({
        provinceEl: document.getElementById('addressProvince'),
        wardEl:     document.getElementById('addressWard'),
        streetEl:   document.getElementById('addressStreet'),
        addressEl:  document.getElementById('address'),

        // Không dùng group nào thì cho null
        groupDistrict: null,
        groupWard:     null,

        // GHI TÊN vào 3 input ẨN để submit
        provinceNameHidden: document.getElementById('provinceHidden'),
        districtNameHidden: document.getElementById('districtHidden'),
        wardNameHidden:     document.getElementById('wardHidden'),
    });
});
