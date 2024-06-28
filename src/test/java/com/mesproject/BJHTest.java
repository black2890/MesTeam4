package com.mesproject;
import com.mesproject.controller.OrdersController;
import com.mesproject.entity.Product;
import com.mesproject.entity.Vendor;
import com.mesproject.repository.ProductRepository;
import com.mesproject.repository.VendorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BJHTest {

    private MockMvc mockMvc;

    @InjectMocks
    private OrdersController ordersController;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VendorRepository vendorRepository;

    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();
    }

    @Test
    @DisplayName("수주현황조회")
    public void testOrders() throws Exception {
        // Given
        String productName = "Sample Product";
        String vendorName = "Sample Vendor";
        String quantity = "10";
        String deliveryDate = "2024-06-25";
        String deliveryAddress = "Sample Address";

        // Mock data for Product
        Product mockProduct = new Product();
        mockProduct.setProductId(1L); // Adjust as per your Product entity structure
        mockProduct.setProductName(productName); // Set product name for mock

        // Mock data for Vendor
        Vendor mockVendor = new Vendor();
        mockVendor.setVendorId(1L); // Adjust as per your Vendor entity structure
        mockVendor.setVendorName(vendorName); // Set vendor name for mock

        // Mock repository methods
        when(productRepository.findByProductName(productName)).thenReturn(mockProduct);
        when(vendorRepository.findByVendorName(vendorName)).thenReturn(mockVendor);

        // When/Then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/create-order")
                        .param("productName", productName)
                        .param("vendorName", vendorName)
                        .param("quantity", quantity)
                        .param("deliveryDate", deliveryDate)
                        .param("deliveryAddress", deliveryAddress))
                .andExpect(status().isOk()); // Assuming the endpoint returns HTTP 200 on success
    }

    @Test
    @DisplayName("수주현황관리")
    public void testCreate() throws Exception {

    }

    @Test
    @DisplayName("완제품 재고현황")
    public void testProductStock() throws Exception {

    }

    @Test
    @DisplayName("생산제품현황")
    public void testProducts() throws Exception {

    }

    @Test
    @DisplayName("업체별 수주현황")
    public void testProductWithVendor() throws Exception {

    }

    @Test
    @DisplayName("업체 현황")
    public void testVendors() throws Exception {

    }
}
