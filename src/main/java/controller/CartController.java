package controller;

import model.Cart;
import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import service.ProductService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product/order/{id}", method = RequestMethod.GET)
    public ModelAndView loadOrderForm(@PathVariable("id") Integer id, HttpSession session) {
        ModelAndView orderForm = null;
        int index, quantity;
        List<Cart> cartList;
        Cart cart = new Cart();
        Product product = productService.findById(id);
        try {
            if (session.getAttribute("cart") == null) {
                cartList = new ArrayList<>();

                cart.setProduct(product);
                cart.setAmount(1);
                cartList.add(cart);
            } else {
                cartList = (List<Cart>) session.getAttribute("cart");
                index = productService.isExist(id, session, "cart");
                if (index == -1) {
                    cart.setProduct(product);
                    cart.setAmount(1);
                    cartList.add(cart);
                } else {
                    quantity = cartList.get(index).getAmount() + 1;
                    cartList.get(index).setAmount(quantity);
                }
            }
            session.setAttribute("cart", cartList);
            orderForm = new ModelAndView("cart");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return orderForm;
    }
}