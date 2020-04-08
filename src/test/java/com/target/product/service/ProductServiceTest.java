package com.target.product.service;

import com.target.product.model.ProductDescription;
import com.target.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    private ProductDescription productDescription;
    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Before
    public void setup() {
        productDescription = new ProductDescription();
        productService = new ProductService(productRepository);
    }

    @Test
    public void createProductSuccess() throws Exception {
        this.resetData();
        doReturn(productDescription).when(productRepository).save(productDescription);

        ProductDescription pr = productService.createProduct(productDescription);
        assertEquals(productDescription, pr);
        verify(productRepository, times(1)).save(productDescription);
        verify(productRepository, times(0)).deleteById(Mockito.anyString());
    }

    @Test
    public void findAllProducts() {
        this.resetData();
        List<ProductDescription> p = new ArrayList<>();
        p.add(productDescription);
        doReturn(p).when(productRepository).findAll();

        List<ProductDescription> pr = productService.findAll();

        assertEquals(pr.size(),1);
        verify(productRepository, times(1)).findAll();
        verify(productRepository, times(0)).deleteById(Mockito.anyString());
    }

    @Test
    public void findById() {
        this.resetData();
        Optional<ProductDescription> returnProduct = Optional.of(productDescription);
        doReturn(returnProduct).when(productRepository).findById(Mockito.anyString());

        Optional<ProductDescription> pr = productService.findById(productDescription.getProduct_id());

        assertEquals(productDescription, pr.get());
        verify(productRepository, times(1)).findById(Mockito.anyString());
        verify(productRepository, times(0)).deleteById(Mockito.anyString());
    }

    @Test
    public void updateProductByIdSuccess() throws Exception {
        this.resetData();

        Optional<ProductDescription> returnProduct = Optional.of(productDescription);
        doReturn(productDescription).when(productRepository).save(Mockito.any(ProductDescription.class));
        doReturn(returnProduct).when(productRepository).findById(Mockito.anyString());

        Boolean result = productService.updateById(productDescription.getProduct_id(), productDescription);

        assertEquals(true, result);
        verify(productRepository, times(1)).findById(Mockito.anyString());
        verify(productRepository, times(1)).save(Mockito.any(ProductDescription.class));
        verify(productRepository, times(0)).deleteById(Mockito.anyString());
    }

    @Test
    public void updateProductByIdNotFound() throws Exception {
        this.resetData();

        doThrow(new NoSuchElementException()).when(productRepository).findById(Mockito.anyString());

        boolean result = productService.updateById(productDescription.getProduct_id(), productDescription);

        assertFalse(result);
        verify(productRepository, times(1)).findById(Mockito.anyString());
        verify(productRepository, times(0)).save(Mockito.any(ProductDescription.class));
    }

    @Test(expected = RuntimeException.class)
    public void updateProductByIdException() throws Exception {
        this.resetData();

        Optional<ProductDescription> returnProduct = Optional.of(productDescription);
        doReturn(returnProduct).when(productRepository).findById(Mockito.anyString());
        doThrow(new RuntimeException()).when(productRepository).save(productDescription);

        productService.updateById(productDescription.getProduct_id(), productDescription);

        verify(productRepository, times(1)).findById(Mockito.anyString());
        verify(productRepository, times(1)).save(Mockito.any(ProductDescription.class));
    }

    private void resetData() {
        productDescription.setProduct_id("123456");
        productDescription.setSellerId("123");
        productDescription.setTitle("dummy");
    }
}
