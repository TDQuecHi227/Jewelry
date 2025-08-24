document.addEventListener("DOMContentLoaded", () => {
    console.log("Trang chủ PNJ demo loaded ✔️");

    document.querySelectorAll(".btn-add").forEach(btn => {
        btn.addEventListener("click", () => {
            alert("Sản phẩm đã được thêm vào giỏ hàng 🛒");
        });
    });
});
