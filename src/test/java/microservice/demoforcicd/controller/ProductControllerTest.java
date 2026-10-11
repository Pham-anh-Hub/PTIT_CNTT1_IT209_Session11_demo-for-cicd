package microservice.demoforcicd.controller;

import microservice.demoforcicd.dto.ProductRequest;
import microservice.demoforcicd.entity.Product;
import microservice.demoforcicd.exception.GlobalExceptionHandler;
import microservice.demoforcicd.exception.ResourceNotFoundException;
import microservice.demoforcicd.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private static final String SAMPLE_REQUEST_JSON = "{"
            + "\"name\":\"Bàn phím cơ AKKO\","
            + "\"description\":\"Switch switch blue\","
            + "\"price\":1200000.00,"
            + "\"quantity\":20,"
            + "\"active\":true"
            + "}";

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        sampleProduct = Product.builder()
                .id(1L)
                .name("Bàn phím cơ AKKO")
                .description("Switch switch blue")
                .price(new BigDecimal("1200000.00"))
                .quantity(20)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("GET /api/products - Thành công trả về danh sách sản phẩm")
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts(null)).thenReturn(Collections.singletonList(sampleProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Bàn phím cơ AKKO"));
    }

    @Test
    @DisplayName("GET /api/products/1 - Thành công trả về chi tiết sản phẩm")
    void testGetProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Bàn phím cơ AKKO"));
    }

    @Test
    @DisplayName("GET /api/products/99 - Trả về 404 khi không tìm thấy")
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Không tìm thấy sản phẩm có ID: 99"));
    }

    @Test
    @DisplayName("POST /api/products - Tạo mới sản phẩm thành công trả về 201")
    void testCreateProduct_Success() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SAMPLE_REQUEST_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Bàn phím cơ AKKO"));
    }

    @Test
    @DisplayName("PUT /api/products/1 - Cập nhật sản phẩm thành công trả về 200")
    void testUpdateProduct_Success() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(sampleProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SAMPLE_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/products/1 - Xóa sản phẩm thành công trả về 200")
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Xóa sản phẩm thành công"));
    }
}
