package springboot.catchshop.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.QProduct;
import springboot.catchshop.dto.PageRequestDTO;
import springboot.catchshop.dto.PageResultDTO;
import springboot.catchshop.dto.ProductDTO;
import springboot.catchshop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
/**
 * ProductServiceImpl
 * author:김지연
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;


    //상품 등록
    @Override
    public Long addProduct(ProductDTO productDTO) {
        log.info("DTO====================");
        log.info(productDTO);
        log.info("=====================");
        
        Product entity = dtoToEntity(productDTO);
        log.info(entity);
        productRepository.save(entity);
        return entity.getId();
    }


    //상품 전체 조회
    @Override
    public PageResultDTO<ProductDTO, Product> readProducts(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());//상품 정렬

        //BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<Product> result = productRepository.findAll(pageable);


        Function<Product, ProductDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

//    @Override
//    public List<ProductDTO> searchProducts(String keyword) {
//        List<Product> products = productRepository.findByNameContaining(keyword);
//        List<ProductDTO> productDTOList = new ArrayList<>();
//
//        if(products.isEmpty()) return productDTOList;
//
//        for(Product product : products){
//            productDTOList.add(this.entityToDto(product));
//        }
//        return productDTOList;
//    }


    //상품 개별 조회
    @Override
    public ProductDTO readSingleProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow( () -> new IllegalStateException("상품이 존재하지 않습니다."));
        return entityToDto(product);
    }




    //상품 수정
    @Override
    public void updateProduct(ProductDTO dto) {
        Product product = productRepository.findById(dto.getId()).orElseThrow( () -> new IllegalStateException("상품이 존재하지 않습니다."));

        product.changeName(dto.getName());
        product.changeProductImg(dto.getProductImg());
        product.changePrice(dto.getPrice());
        product.changeStock(dto.getStock());

        productRepository.save(product);
    }

    //상품 삭제
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    //상품 검색
//    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
//        BooleanBuilder builder = new BooleanBuilder();
//        QProduct qProduct = QProduct.product;
//        String keyword = requestDTO.getKeyword();
//
//        BooleanExpression expression1 = qProduct.id.gt(0L);
//        BooleanExpression expression2 = qProduct.name.contains(keyword);
//        builder.and(expression1);
//        builder.and(expression2);
//        return builder;
//    }


}
