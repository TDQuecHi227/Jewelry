package com.hhd.jewelry.config;

import com.hhd.jewelry.entity.Category;
import com.hhd.jewelry.entity.Collection;
import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.service.CategoryService;
import com.hhd.jewelry.service.CollectionService;
import com.hhd.jewelry.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("unused")
public class DataSeeder implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CollectionService collectionService;

    public DataSeeder(CategoryService categoryService, ProductService productService, CollectionService collectionService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.collectionService = collectionService;
    }

    @Override
    public void run(String... args) {
        // Delete
        // removeAllProducts();

        // Insert
        seedCategories();
        seedProducts();
        seedCollections();
    }

    void seedCategories() {
        createOrUpdateCategory("Vòng tay", "/images/categories/bracelets/category/Lac.jpg");
        createOrUpdateCategory("Trang sức vàng", "/images/categories/charms/category/TrangSucVang.jpg");
        createOrUpdateCategory("Bông tai", "/images/categories/earrings/category/BongTai.jpg");
        createOrUpdateCategory("Dây chuyền", "/images/categories/necklaces/category/DayChuyen.jpg");
        createOrUpdateCategory("Nhẫn", "/images/categories/rings/category/Nhan.jpg");
        createOrUpdateCategory("Đồng hồ", "/images/categories/watch/category/DongHo.jpg");
    }

    void seedCollections() {
        createOrUpdateCollection("Doraemon", "/images/collections/Doraemon_PNJ.jpg");
        createOrUpdateCollection("LNCV", "/images/collections/LNCV_PNJ.jpg");
        createOrUpdateCollection("Marvel", "/images/collections/Marvel_PNJ.jpg");
        createOrUpdateCollection("Summer", "/images/collections/Summer_PNJ.jpg");
        createOrUpdateCollection("VietNam", "/images/collections/VietNam_PNJ.jpg");
        createOrUpdateCollection("Disney", "/images/collections/Disney_PNJ.jpg");
    }

    void seedProducts() {
        Category bracelet = categoryService.getCategoryByName("Vòng tay");
        Category charm = categoryService.getCategoryByName("Trang sức vàng");
        Category earring = categoryService.getCategoryByName("Bông tai");
        Category necklace = categoryService.getCategoryByName("Dây chuyền");
        Category ring = categoryService.getCategoryByName("Nhẫn");
        Category watch = categoryService.getCategoryByName("Đồng hồ");

        Collection doraemonCollection = collectionService.getCollectionByName("Doraemon");
        Collection LNCVCollection = collectionService.getCollectionByName("LNCV");
        Collection marvelCollection = collectionService.getCollectionByName("Marvel");
        Collection summerCollection = collectionService.getCollectionByName("Summer");
        Collection vietNamCollection = collectionService.getCollectionByName("VietNam");
        Collection disneyCollection = collectionService.getCollectionByName("Disney");

        // Bracelets
        createOrUpdateProduct("XM00Y000751", "Lắc tay", "Đá ECZ", "Vàng 14K", "PNJ", 13732000, 0, "Nữ", 50, bracelet, disneyCollection);
        createOrUpdateProduct("DDDDW000944", "Lắc tay", "Kim cương", "Vàng 14K", "PNJ", 56001000, 0, "Nữ", 60, bracelet, disneyCollection);
        createOrUpdateProduct("0000C000334", "Lắc tay", null, "Vàng 14K", "PNJ", 65927000, 0, "Nam", 50, bracelet, marvelCollection);
        createOrUpdateProduct("0000W060278", "Lắc tay", null, "Bạc", "PNJ", 1095000, 0, "Nữ", 50, bracelet, disneyCollection);

        addImagesToProduct("XM00Y000751", List.of(
                "/images/categories/bracelets/products/bracelet1/1.jpg",
                "/images/categories/bracelets/products/bracelet1/2.jpg",
                "/images/categories/bracelets/products/bracelet1/3.jpg",
                "/images/categories/bracelets/products/bracelet1/4.jpg"
        ));

        addImagesToProduct("DDDDW000944", List.of(
                "/images/categories/bracelets/products/bracelet2/1.jpg",
                "/images/categories/bracelets/products/bracelet2/2.jpg",
                "/images/categories/bracelets/products/bracelet2/3.jpg",
                "/images/categories/bracelets/products/bracelet2/4.jpg",
                "/images/categories/bracelets/products/bracelet2/5.jpg"
        ));

        addImagesToProduct("0000C000334", List.of(
                "/images/categories/bracelets/products/bracelet3/1.jpg",
                "/images/categories/bracelets/products/bracelet3/2.jpg",
                "/images/categories/bracelets/products/bracelet3/3.jpg",
                "/images/categories/bracelets/products/bracelet3/4.jpg"
        ));

        addImagesToProduct("0000W060278", List.of(
                "/images/categories/bracelets/products/bracelet4/1.jpg",
                "/images/categories/bracelets/products/bracelet4/2.jpg",
                "/images/categories/bracelets/products/bracelet4/3.jpg",
                "/images/categories/bracelets/products/bracelet4/4.jpg",
                "/images/categories/bracelets/products/bracelet4/5.jpg"
        ));

        // Charms
        createOrUpdateProduct("0000C000008", "Hạt charm", null, "Bạc", "PNJ", 945000, 0, "Nữ", 50, charm, doraemonCollection);
        createOrUpdateProduct("0000Y060338", "Hạt charm", null, "Vàng 22K", "PNJ", 4312000, 0, "Nữ", 50, charm, disneyCollection);
        createOrUpdateProduct("XM00Y000023", "Hạt charm", "Kim cương", "Bạc", "PNJ", 755000, 0, "Unisex", 40, charm, vietNamCollection);
        createOrUpdateProduct("XMXMC000012", "Hạt charm", null, "Bạc", "PNJ", 655000, 0, "Nam", 50, charm, vietNamCollection);
        createOrUpdateProduct("0000Y000669", "Hạt charm", null, "Vàng 10K", "PNJ", 8272000, 0, "Nữ", 50, charm, doraemonCollection);
        createOrUpdateProduct("0000W000132", "Hạt charm", null, "Vàng Trắng 10K", "PNJ", 3488000, 0, "Nữ", 50, charm, summerCollection);

        addImagesToProduct("0000C000008", List.of(
                "/images/categories/charms/products/charm1/1.jpg",
                "/images/categories/charms/products/charm1/2.jpg",
                "/images/categories/charms/products/charm1/3.jpg",
                "/images/categories/charms/products/charm1/4.jpg",
                "/images/categories/charms/products/charm1/5.jpg"
        ));

        addImagesToProduct("0000Y060338", List.of(
                "/images/categories/charms/products/charm2/1.jpg",
                "/images/categories/charms/products/charm2/2.jpg",
                "/images/categories/charms/products/charm2/3.jpg",
                "/images/categories/charms/products/charm2/4.jpg",
                "/images/categories/charms/products/charm2/5.jpg"
        ));

        addImagesToProduct("XM00Y000023", List.of(
                "/images/categories/charms/products/charm3/1.jpg",
                "/images/categories/charms/products/charm3/2.jpg",
                "/images/categories/charms/products/charm3/3.jpg",
                "/images/categories/charms/products/charm3/4.jpg"
        ));

        addImagesToProduct("XMXMC000012", List.of(
                "/images/categories/charms/products/charm4/1.jpg",
                "/images/categories/charms/products/charm4/2.jpg",
                "/images/categories/charms/products/charm4/3.jpg"
        ));

        addImagesToProduct("0000Y000669", List.of(
                "/images/categories/charms/products/charm5/1.jpg",
                "/images/categories/charms/products/charm5/2.jpg",
                "/images/categories/charms/products/charm5/3.jpg",
                "/images/categories/charms/products/charm5/4.jpg",
                "/images/categories/charms/products/charm5/5.jpg"
        ));

        addImagesToProduct("0000W000132", List.of(
                "/images/categories/charms/products/charm6/1.jpg",
                "/images/categories/charms/products/charm6/2.jpg",
                "/images/categories/charms/products/charm6/3.jpg",
                "/images/categories/charms/products/charm6/4.jpg",
                "/images/categories/charms/products/charm6/5.jpg"
        ));

        // Earrings
        createOrUpdateProduct("0000Y060054", "Bông tai", null, "Bạc", "PNJ", 795000, 10, "Nữ", 50, earring, disneyCollection);
        createOrUpdateProduct("ZTXMW000104", "Bông tai", "Synthetic", "Vàng Trắng 14K", "PNJ", 5651000, 20, "Nữ", 50, earring, disneyCollection);
        createOrUpdateProduct("XM00X000057", "Bông tai", "ECZ", "Vàng 10K", "PNJ", 7995000, 0, "Nữ", 50, earring, LNCVCollection);
        createOrUpdateProduct("ZTXMW000035", "Bông tai", "Ruby", "Bạc", "PNJ", 1095000, 0, "Nữ", 50, earring, LNCVCollection);
        createOrUpdateProduct("XMXMX000021", "Bông tai", "Kim cương", "Bạc", "PNJ", 1995000, 0, "Nữ", 50, earring, LNCVCollection);

        addImagesToProduct("0000Y060054", List.of(
                "/images/categories/earrings/products/earring1/1.jpg",
                "/images/categories/earrings/products/earring1/2.jpg",
                "/images/categories/earrings/products/earring1/3.jpg",
                "/images/categories/earrings/products/earring1/4.jpg",
                "/images/categories/earrings/products/earring1/5.jpg"
        ));

        addImagesToProduct("ZTXMW000104", List.of(
                "/images/categories/earrings/products/earring2/1.jpg",
                "/images/categories/earrings/products/earring2/2.jpg",
                "/images/categories/earrings/products/earring2/3.jpg",
                "/images/categories/earrings/products/earring2/4.jpg"
        ));

        addImagesToProduct("XM00X000057", List.of(
                "/images/categories/earrings/products/earring3/1.jpg",
                "/images/categories/earrings/products/earring3/2.jpg",
                "/images/categories/earrings/products/earring3/3.jpg",
                "/images/categories/earrings/products/earring3/4.jpg",
                "/images/categories/earrings/products/earring3/5.jpg"
        ));

        addImagesToProduct("ZTXMW000035", List.of(
                "/images/categories/earrings/products/earring4/1.jpg",
                "/images/categories/earrings/products/earring4/2.jpg",
                "/images/categories/earrings/products/earring4/3.jpg",
                "/images/categories/earrings/products/earring4/4.jpg",
                "/images/categories/earrings/products/earring4/5.jpg"
        ));

        addImagesToProduct("XMXMX000021", List.of(
                "/images/categories/earrings/products/earring5/1.jpg",
                "/images/categories/earrings/products/earring5/2.jpg",
                "/images/categories/earrings/products/earring5/3.jpg",
                "/images/categories/earrings/products/earring5/4.jpg"
        ));

        // Necklaces
        createOrUpdateProduct("ZTXMX000003", "Dây chuyền", null, "Bạc", "PNJ", 995000, 0, "Nữ", 50, necklace, disneyCollection);
        createOrUpdateProduct("DD00W000723", "Dây chuyền", "Kim cương", "Vàng Trắng 14K", "PNJ", 19950000, 0, "Nam", 50, necklace, marvelCollection);
        createOrUpdateProduct("0000W060350", "Dây chuyền", null, "Vàng Trắng 18K", "PNJ", 1995000, 0, "Nữ", 50, necklace, summerCollection);
        createOrUpdateProduct("0000W001470", "Dây chuyền", null, "Vàng Trắng Ý 18K", "PNJ", 3995000, 0, "Nữ", 50, necklace, summerCollection);
        createOrUpdateProduct("0000Y012974", "Dây chuyền", null, "Vàng 24K", "PNJ", 5995000, 10, "Nam", 50, necklace, summerCollection);

        addImagesToProduct("ZTXMX000003", List.of(
                "/images/categories/necklaces/products/necklace1/1.jpg",
                "/images/categories/necklaces/products/necklace1/2.jpg",
                "/images/categories/necklaces/products/necklace1/3.jpg",
                "/images/categories/necklaces/products/necklace1/4.jpg",
                "/images/categories/necklaces/products/necklace1/5.jpg"
        ));

        addImagesToProduct("DD00W000723", List.of(
                "/images/categories/necklaces/products/necklace2/1.jpg",
                "/images/categories/necklaces/products/necklace2/2.jpg",
                "/images/categories/necklaces/products/necklace2/3.jpg",
                "/images/categories/necklaces/products/necklace2/4.jpg",
                "/images/categories/necklaces/products/necklace2/5.jpg"
        ));

        addImagesToProduct("0000W060350", List.of(
                "/images/categories/necklaces/products/necklace3/1.jpg",
                "/images/categories/necklaces/products/necklace3/2.jpg",
                "/images/categories/necklaces/products/necklace3/3.jpg",
                "/images/categories/necklaces/products/necklace3/4.jpg",
                "/images/categories/necklaces/products/necklace3/5.jpg"
        ));

        addImagesToProduct("0000W001470", List.of(
                "/images/categories/necklaces/products/necklace4/1.jpg",
                "/images/categories/necklaces/products/necklace4/2.jpg",
                "/images/categories/necklaces/products/necklace4/3.jpg",
                "/images/categories/necklaces/products/necklace4/4.jpg"
        ));

        addImagesToProduct("0000Y012974", List.of(
                "/images/categories/necklaces/products/necklace5/1.jpg",
                "/images/categories/necklaces/products/necklace5/2.jpg",
                "/images/categories/necklaces/products/necklace5/3.jpg",
                "/images/categories/necklaces/products/necklace5/4.jpg",
                "/images/categories/necklaces/products/necklace5/5.jpg"
        ));

        // Rings
        createOrUpdateProduct("DDDDW014103", "Nhẫn", "Kim cương", "Vàng Trắng 14K", "PNJ", 19804000, 0, "Nữ", 50, ring, disneyCollection);
        createOrUpdateProduct("DDDDC001972", "Nhẫn", "Kim cương", "Vàng 14K", "PNJ", 24934000, 0, "Nữ", 50, ring, disneyCollection);
        createOrUpdateProduct("DDMXY000006", "Nhẫn", "Kim cương", "Vàng 14K", "PNJ", 17400000, 0, "Nữ", 50, ring, summerCollection);
        createOrUpdateProduct("DDDDX000401", "Nhẫn", "Kim cương", "Vàng 14K", "PNJ", 15646000, 0, "Nữ", 50, ring, disneyCollection);
        createOrUpdateProduct("GNXM00W0014", "Nhẫn", "ECZ", "Vàng Trắng 10K", "PNJ", 9743000, 0, "Unisex", 50, ring, summerCollection);

        addImagesToProduct("DDDDW014103", List.of(
                "/images/categories/rings/products/ring1/1.jpg",
                "/images/categories/rings/products/ring1/2.jpg",
                "/images/categories/rings/products/ring1/3.jpg",
                "/images/categories/rings/products/ring1/4.jpg",
                "/images/categories/rings/products/ring1/5.jpg"
        ));

        addImagesToProduct("DDDDC001972", List.of(
                "/images/categories/rings/products/ring2/1.jpg",
                "/images/categories/rings/products/ring2/2.jpg",
                "/images/categories/rings/products/ring2/3.jpg",
                "/images/categories/rings/products/ring2/4.jpg",
                "/images/categories/rings/products/ring2/5.jpg"
        ));

        addImagesToProduct("DDMXY000006", List.of(
                "/images/categories/rings/products/ring3/1.jpg",
                "/images/categories/rings/products/ring3/2.jpg",
                "/images/categories/rings/products/ring3/3.jpg",
                "/images/categories/rings/products/ring3/4.jpg",
                "/images/categories/rings/products/ring3/5.jpg"
        ));

        addImagesToProduct("DDDDX000401", List.of(
                "/images/categories/rings/products/ring4/1.jpg",
                "/images/categories/rings/products/ring4/2.jpg",
                "/images/categories/rings/products/ring4/3.jpg",
                "/images/categories/rings/products/ring4/4.jpg",
                "/images/categories/rings/products/ring4/5.jpg"
        ));

        addImagesToProduct("GNXM00W0014", List.of(
                "/images/categories/rings/products/ring5/1.jpg",
                "/images/categories/rings/products/ring5/2.jpg",
                "/images/categories/rings/products/ring5/3.jpg"
        ));

        // Watch
        createOrUpdateProduct("WURAAWDDC38", "Đồng hồ", "ECZ", "Ceramic", "PNJ", 97110000, 0, "Unisex", 50, watch, summerCollection);
        createOrUpdateProduct("WFTIQWDDS20", "Đồng hồ", null, "Kim loại", "PNJ", 12230000, 0, "Nữ", 50, watch, summerCollection);
        createOrUpdateProduct("WFRAAWDDS33", "Đồng hồ", null, "Kim loại", "PNJ", 79690000, 0, "Nữ", 50, watch, summerCollection);
        createOrUpdateProduct("WFLOQWDDL37", "Đồng hồ", "Kim cương", "Da", "PNJ", 29400000, 0, "Nữ", 50, watch, LNCVCollection);
        createOrUpdateProduct("WFCIEJDDS30", "Đồng hồ", null, "Kim loại", "PNJ", 31185000, 0, "Nữ", 50, watch, LNCVCollection);

        addImagesToProduct("WURAAWDDC38", List.of(
                "/images/categories/watch/products/watch1/1.jpg",
                "/images/categories/watch/products/watch1/2.jpg",
                "/images/categories/watch/products/watch1/3.jpg",
                "/images/categories/watch/products/watch1/4.jpg",
                "/images/categories/watch/products/watch1/5.jpg"
        ));

        addImagesToProduct("WFTIQWDDS20", List.of(
                "/images/categories/watch/products/watch2/1.jpg",
                "/images/categories/watch/products/watch2/2.jpg",
                "/images/categories/watch/products/watch2/3.jpg",
                "/images/categories/watch/products/watch2/4.jpg"
        ));

        addImagesToProduct("WFRAAWDDS33", List.of(
                "/images/categories/watch/products/watch3/1.jpg",
                "/images/categories/watch/products/watch3/2.jpg",
                "/images/categories/watch/products/watch3/3.jpg",
                "/images/categories/watch/products/watch3/4.jpg",
                "/images/categories/watch/products/watch3/5.jpg"
        ));

        addImagesToProduct("WFLOQWDDL37", List.of(
                "/images/categories/watch/products/watch4/1.jpg",
                "/images/categories/watch/products/watch4/2.jpg",
                "/images/categories/watch/products/watch4/3.jpg",
                "/images/categories/watch/products/watch4/4.jpg"
        ));

        addImagesToProduct("WFCIEJDDS30", List.of(
                "/images/categories/watch/products/watch5/1.jpg",
                "/images/categories/watch/products/watch5/2.jpg",
                "/images/categories/watch/products/watch5/3.jpg",
                "/images/categories/watch/products/watch5/4.jpg",
                "/images/categories/watch/products/watch5/5.jpg"
        ));
    }

    private void createOrUpdateCategory(String name, String imageUrl) {
        Category category = categoryService.getCategoryByName(name);
        if (category == null) {
            category = new Category(null, name, imageUrl, null);
        }
        else {
            category.setImageUrl(imageUrl);
        }
        categoryService.save(category);
    }

    private void createOrUpdateCollection(String name, String imageUrl) {
        Collection collection = collectionService.getCollectionByName(name);
        if (collection == null) {
            collection = new Collection(null, name, imageUrl, null);
        }
        else {
            collection.setImageUrl(imageUrl);
        }
        collectionService.save(collection);
    }

    private void createOrUpdateProduct(
            String serialNumber,
            String name,
            String gemstone,
            String material,
            String brand,
            Integer price,
            Integer discount,
            String gender,
            Integer stockQuantity,
            Category category,
            Collection collection
    ) {
        Product product = productService.getProductBySerialNumber(serialNumber);

        if (product == null) {
            product = Product.builder()
                    .name(name)
                    .gemstone(gemstone)
                    .material(material)
                    .brand(brand)
                    .serialNumber(serialNumber)
                    .price(price)
                    .discount(discount != null ? discount : 0)
                    .order(0)
                    .gender(gender)
                    .stockQuantity(stockQuantity)
                    .category(category)
                    .collection(collection)
                    .build();
        } else {
            product.setName(name);
            product.setGemstone(gemstone);
            product.setMaterial(material);
            product.setBrand(brand);
            product.setPrice(price);
            product.setDiscount(discount != null ? discount : 0);
            product.setGender(gender);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            product.setCollection(collection);
        }

        productService.save(product);
    }

    private void addImagesToProduct(String serialNumber, List<String> imageUrls) {
        Product product = productService.getProductBySerialNumber(serialNumber);

        if (product != null) {
            List<String> existingImages = product.getImageUrls();

            List<String> newImages = imageUrls.stream()
                    .filter(url -> !existingImages.contains(url))
                    .toList();

            if (!newImages.isEmpty()) {
                existingImages.addAll(newImages);
                productService.save(product);
                System.out.println("✅ Đã thêm " + newImages.size() + " ảnh mới cho sản phẩm " + serialNumber);
            } else {
                System.out.println("ℹ️ Không có ảnh mới nào để thêm cho sản phẩm " + serialNumber);
            }
        } else {
            System.out.println("⚠️ Không tìm thấy sản phẩm với serialNumber: " + serialNumber);
        }
    }

    private void removeAllImagesFromProduct(String serialNumber) {
        Product product = productService.getProductBySerialNumber(serialNumber);

        if (product != null) {
            int count = product.getImageUrls().size();
            product.getImageUrls().clear();
            productService.save(product);
            System.out.println("🗑️ Đã xóa " + count + " ảnh của sản phẩm " + serialNumber);
        } else {
            System.out.println("⚠️ Không tìm thấy sản phẩm với serialNumber: " + serialNumber);
        }
    }

    private void removeImageFromProduct(String serialNumber, String imageUrl) {
        Product product = productService.getProductBySerialNumber(serialNumber);

        if (product != null) {
            boolean removed = product.getImageUrls().remove(imageUrl);
            if (removed) {
                productService.save(product);
                System.out.println("🗑️ Đã xóa ảnh " + imageUrl + " của sản phẩm " + serialNumber);
            } else {
                System.out.println("ℹ️ Không tìm thấy ảnh " + imageUrl + " trong sản phẩm " + serialNumber);
            }
        } else {
            System.out.println("⚠️ Không tìm thấy sản phẩm với serialNumber: " + serialNumber);
        }
    }

    private void removeAllProducts() {
        List<Product> products = productService.getAllProducts();
        int count = products.size();

        if (count > 0) {
            productService.deleteAll();
            productService.resetAutoIncrement();
            System.out.println("🗑️ Đã xóa " + count + " sản phẩm trong hệ thống.");
        } else {
            System.out.println("ℹ️ Không có sản phẩm nào để xóa.");
        }
    }
}
