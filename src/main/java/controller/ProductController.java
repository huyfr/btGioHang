package controller;

import model.Product;
import model.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.ProductService;

import java.io.File;


@Controller
@PropertySource("classpath:uploadFile.properties")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    Environment environment;

    @RequestMapping(value = "/product/overview", method = RequestMethod.GET)
    public ModelAndView loadListProduct(@PageableDefault(value = 2) Pageable pageable) {
        ModelAndView listProduct = null;
        Page<Product> products;
        try {
            products = productService.findAll(pageable);
            listProduct = new ModelAndView("index");
            listProduct.addObject("products", products);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return listProduct;
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.GET)
    public ModelAndView loadFormCreate() {
        ModelAndView createForm = null;
        ProductForm newProductForm;
        try {
            newProductForm = new ProductForm();
            createForm = new ModelAndView("create");
            createForm.addObject("newProductForm", newProductForm);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return createForm;
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public ModelAndView createProduct(@ModelAttribute("newProductForm") ProductForm productForm, @PageableDefault(value = 2) Pageable pageable) {
        ModelAndView created = null;
        MultipartFile multipartFile;
        String fileName, fileUpload;
        File file;
        Product newProduct;
        try {
            multipartFile = productForm.getImage();
            fileName = multipartFile.getOriginalFilename();
            fileUpload = environment.getProperty("file_upload").toString();
            file = new File(fileUpload, fileName);
            FileCopyUtils.copy(productForm.getImage().getBytes(), file);

            newProduct = new Product();
            newProduct.setName(productForm.getName());
            newProduct.setDescription(productForm.getDescription());
            newProduct.setPrice(productForm.getPrice());
            newProduct.setImage(fileName);

            productService.save(newProduct);

            created = loadListProduct(pageable);
            created.addObject("alert", "Thêm sản phẩm mới thành công");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return created;
    }

}
