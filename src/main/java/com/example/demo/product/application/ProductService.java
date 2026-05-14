package com.example.demo.product.application;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.product.domain.Product;
import com.example.demo.product.infrastructure.ProductRepository;
import com.example.demo.product.presentation.dto.CreateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 透過 Stored Procedure 建立商品，並將 DB 錯誤代碼映射為可讀的商業錯誤訊息。
     *
     * @param request 商品建立請求資料
     * @return 建立完成且重新查詢後的商品資料
     * @throws BusinessException 當輸入資料或資料庫操作不符合商業規則時拋出
     */
    @Transactional
    public Product createProduct(CreateProductRequest request) {
        try {
            productRepository.createProductBySp(
                    request.getProductId(),
                    request.getProductName(),
                    request.getPrice().intValueExact(),
                    request.getQuantity());
        } catch (ArithmeticException exception) {
            throw new BusinessException("價格必須為整數");
        } catch (DataAccessException exception) {
            String message = exception.getMostSpecificCause() != null
                    ? exception.getMostSpecificCause().getMessage()
                    : exception.getMessage();

            if (message != null && message.contains("INVALID_PRODUCT_ID")) {
                throw new BusinessException("商品編號格式錯誤");
            }
            if (message != null && message.contains("INVALID_PRODUCT_NAME")) {
                throw new BusinessException("商品名稱格式錯誤");
            }
            if (message != null && message.contains("INVALID_PRICE")) {
                throw new BusinessException("價格必須大於 0");
            }
            if (message != null && message.contains("INVALID_QUANTITY")) {
                throw new BusinessException("庫存不可小於 0");
            }
            throw new BusinessException("建立商品失敗");
        }

        return productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException("建立商品失敗"));
    }
}
