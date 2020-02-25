package Ass.Controller;

import Ass.Model.Bill;
import Ass.Model.Item;
import Ass.Model.Products;
import Ass.Repository.BillPagingAndSortingRepository;
import Ass.Repository.ProductPagingAndSortingRepository;
import Ass.Service.BillService;
import Ass.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping(value = "pages/")
public class ProductController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductPagingAndSortingRepository productPagingAndSortingRepository;

    @Autowired
    private BillPagingAndSortingRepository billPagingAndSortingRepository;

    @Autowired
    private BillService billService;

    private File path = new File("C:\\Users\\nguye\\Downloads\\assj5-master\\src\\main\\webapp\\resources\\images");


    @GetMapping("index-product")
    public ModelAndView index(@PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("pages/index_product");
        Page<Products> page = productPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "index-cart", method = RequestMethod.GET)
    public ModelAndView index1() {
        ModelAndView modelAndView = new ModelAndView("/pages/index_cart");
        return modelAndView;
    }

    @RequestMapping("buy/{id}")
    public ModelAndView buy(@PathVariable("id") String id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("/pages/index_cart");
        if (session.getAttribute("cart") == null) {
            List<Item> cart = new ArrayList<Item>();
            cart.add(new Item(productsService.findByName(id), 1));
            session.setAttribute("cart", cart);
        } else {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            int index = this.exists(id, cart);
            if (index == -1) {
                cart.add(new Item(productsService.findByName(id), 1));
            } else {
                int quantity = cart.get(index).getQuantity() + 1;
                cart.get(index).setQuantity(quantity);
            }
            session.setAttribute("cart", cart);
        }
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        double total = 0;
        for (Item item : cart) {
            total += item.getProduct().getGiaSP() * item.getQuantity();
        }
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @RequestMapping("remove/{id}")
    public ModelAndView remove(@PathVariable("id") String id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("/pages/index_cart");
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        int index = this.exists(id, cart);
        cart.remove(index);
        session.setAttribute("cart", cart);
        double total = 0;
        for (Item item : cart) {
            total += item.getProduct().getGiaSP() * item.getQuantity();
        }
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @RequestMapping("orderPage/{id}")
    public ModelAndView orderPage(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("/pages/thanhtoan");
        modelAndView.addObject("product", productsService.findByName(id));
        modelAndView.addObject("bill", new Bill());
        return modelAndView;
    }

    @RequestMapping(value = "order/{idd}", method = RequestMethod.POST)
    public ModelAndView order(@PathVariable("idd") String id, @ModelAttribute("Bill") Bill bill, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("/pages/index_cart");
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        int index = this.exists(id, cart);
        bill.setTenSP(cart.get(index).getProduct().getTenSP());
        bill.setGiaSP(cart.get(index).getProduct().getGiaSP() + "");
        bill.setTongTien((cart.get(index).getProduct().getGiaSP() * cart.get(index).getQuantity()) + "");
        bill.setSoLuong(cart.get(index).getQuantity() + "");
        billService.save(bill);
        cart.remove(index);
        session.setAttribute("cart", cart);
        modelAndView.addObject("products", productsService.findAll());
        return modelAndView;
    }

    @RequestMapping("orderPageAll")
    public ModelAndView orderPage1() {
        ModelAndView modelAndView = new ModelAndView("/pages/thanhtoanAll");
        modelAndView.addObject("bill", new Bill());
        return modelAndView;
    }

    @RequestMapping(value = "orderAll")
    public ModelAndView orderAll(HttpSession session, @ModelAttribute("Bill") Bill bill) {
        ModelAndView modelAndView = new ModelAndView("/pages/index_cart");
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        List<Bill> bills = new ArrayList<Bill>();
        for (Item item : cart) {
            int index = this.exists(item.getProduct().getTenSP(), cart);
            bills.add(new Bill(
                    bill.getTenKH(),
                    bill.getDiaChi(),
                    bill.getSoDT(),
                    cart.get(index).getProduct().getTenSP(),
                    cart.get(index).getProduct().getGiaSP() + "",
                    cart.get(index).getQuantity() + "",
                    (cart.get(index).getProduct().getGiaSP() * cart.get(index).getQuantity()) + ""));
        }
        billService.saveAll(bills);
        cart.removeAll(cart);
        session.setAttribute("cart", cart);
        return modelAndView;
    }

    @RequestMapping("view-bill")
    public ModelAndView showBill(@PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/pages/bill");
        Page<Bill> page = billPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping("remove-bill/{id}")
    public ModelAndView removeBill(@PathVariable("id") Long id, @PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/pages/bill");
        Page<Bill> page = billPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        billService.remove(id);
        return modelAndView;
    }

    @RequestMapping("view-create-products")
    public ModelAndView showProductsUser(@PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/pages/productsUser");
        Page<Products> page = productPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @GetMapping("create-products")
    public ModelAndView viewCrateProducts() {
        ModelAndView modelAndView = new ModelAndView("/pages/create-product");
        modelAndView.addObject("product", new Products());
        return modelAndView;
    }

    @PostMapping("save-products")
    public ModelAndView crateProducts(@PageableDefault(size = 5) Pageable pageable,@RequestParam("files") CommonsMultipartFile files
                                      ,@ModelAttribute("Products") Products products) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/pages/productsUser");
        if(!files.isEmpty()){
            FileOutputStream fileOutputStream;
            fileOutputStream = new FileOutputStream(path+"\\"+ files.getOriginalFilename());
            fileOutputStream.write(files.getBytes());
            fileOutputStream.close();
            if (files.getOriginalFilename() != null) {
                products.setHinhAnh(files.getOriginalFilename());
            }
        }
        productsService.save(products);
        Page<Products> page = productPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @GetMapping("update-products/{id}")
    public ModelAndView viewUpdateProducts(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/pages/update-product");
        modelAndView.addObject("product", productsService.findById(id));
        return modelAndView;
    }

    @PostMapping("update-products/{id}")
    public ModelAndView updateProducts(@RequestParam("files") CommonsMultipartFile files,@PathVariable Long id, @PageableDefault(size = 5) Pageable pageable,
                                       @ModelAttribute("Products") Products products) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/pages/productsUser");
        if(!files.isEmpty()){
            FileOutputStream fileOutputStream;
            fileOutputStream = new FileOutputStream(path+"\\"+ files.getOriginalFilename());
            fileOutputStream.write(files.getBytes());
            fileOutputStream.close();
            if (files.getOriginalFilename() != null) {
                products.setHinhAnh(files.getOriginalFilename());
            }
        }
        else{
            Products products1 = productsService.findById(id);
            products.setHinhAnh(products1.getHinhAnh());
        }
        products.setId(id);
        productsService.save(products);
        Page<Products> page = productPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @GetMapping("remove-products/{id}")
    public ModelAndView removeProduct(@PathVariable("id") Long id,@PageableDefault(size = 5) Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("/pages/productsUser");
        productsService.remove(id);
        Page<Products> page = productPagingAndSortingRepository.findAll(pageable);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    private int exists(String id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getTenSP().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }


}
