package microservice.demoforcicd.service;

import microservice.demoforcicd.dto.ProductRequest;
import microservice.demoforcicd.entity.Product;
import microservice.demoforcicd.exception.ResourceNotFoundException;
import microservice.demoforcicd.repository.ProductRepository;
import microservice.demoforcicd.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Laptop Dell XPS")
                .description("Laptop Dell cao cấp")
                .price(new BigDecimal("25000000.00"))
                .quantity(10)
                .active(true)
                .build();

        sampleRequest = ProductRequest.builder()
                .name("Laptop Dell XPS")
                .description("Laptop Dell cao cấp")
                .price(new BigDecimal("25000000.00"))
                .quantity(10)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Lấy danh sách tất cả sản phẩm khi không có keyword")
    void testGetAllProducts_WithoutKeyword() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(sampleProduct));

        List<Product> result = productService.getAllProducts(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop Dell XPS");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Tìm kiếm sản phẩm theo keyword")
    void testGetAllProducts_WithKeyword() {
        when(productRepository.findByNameContainingIgnoreCase("Dell")).thenReturn(Arrays.asList(sampleProduct));

        List<Product> result = productService.getAllProducts("Dell");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop Dell XPS");
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Dell");
    }

    @Test
    @DisplayName("Lấy chi tiết sản phẩm thành công theo ID")
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop Dell XPS");
    }

    @Test
    @DisplayName("Ném ResourceNotFoundException khi không tìm thấy ID")
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Tạo mới sản phẩm thành công")
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product created = productService.createProduct(sampleRequest);

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Laptop Dell XPS");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Cập nhật sản phẩm thành công")
    void testUpdateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductRequest updateRequest = ProductRequest.builder()
                .name("Laptop Dell XPS 15")
                .description("Bản nâng cấp")
                .price(new BigDecimal("30000000.00"))
                .quantity(5)
                .active(true)
                .build();

        Product updated = productService.updateProduct(1L, updateRequest);

        assertThat(updated).isNotNull();
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Xóa sản phẩm thành công")
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(sampleProduct);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(sampleProduct);
    }
}
