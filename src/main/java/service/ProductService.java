package service;

import model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;

public interface ProductService {
    public Page<Product> findAll(Pageable pageable);

    public Product findById(Integer id);

    public void save(Product product);

    public void delete(Integer id);

    public int isExist(Integer id, HttpSession session, String sessionName);
}
