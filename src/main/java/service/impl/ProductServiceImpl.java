package service.impl;

import model.Cart;
import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import repository.ProductRepository;
import service.ProductService;

import javax.servlet.http.HttpSession;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findOne(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        productRepository.delete(id);
    }

    @Override
    public int isExist(Integer id, HttpSession session, String sessionName) {
        List<Cart> cartList = (List<Cart>) session.getAttribute(sessionName);
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getProduct().getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
