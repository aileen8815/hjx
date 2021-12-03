package com.wwyl.controller.settings;

import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Product;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductCategoryService;
import com.wwyl.service.settings.ProductService;

/**
 * @author yche
 */
@Controller
@RequestMapping("/settings/product")
public class ProductController extends BaseController {
    @Resource
    ProductService productService;
    @Resource
    MeasureUnitService measureUnitService;
    @Resource
    ProductCategoryService productCategoryService;
    @Resource
    PackingService packingService;

    // 库存商品首页
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return indexPage;
    }

    @RequestMapping(value = "tree", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> tree() {
        return productService.findRootProducts();
    }

    // 展示库存商品
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
                                    @RequestParam(value = "name", defaultValue = "") String name) {
        Page<Product> products = productService.findByCodeOrNameLike(page, rows, name);
        return toEasyUiDatagridResult(products);
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String add(ModelMap model, Long parentId) {
        Product product = new Product();
        if (parentId != null && parentId > 0) {
            Product parent = productService.findOne(parentId);
            product.setParent(parent);
        }
        model.addAttribute("_method", "post");
        model.addAttribute("product", product);
        prepareInput(model);
        return formPage;
    }

    private void prepareInput(ModelMap model) {
        model.addAttribute("measureUnit", measureUnitService.findAll());
        model.addAttribute("productCategorys", productCategoryService.findAll());
        model.addAttribute("packings", packingService.findAll());
    }

    // 保存库存商品
    @RequestMapping(method = RequestMethod.POST)
    public String save(@ModelAttribute("product") @Valid Product product, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            prepareInput(model);
            return formPage;
        }
        productService.save(product);
        return "redirect:/settings/product";
    }

    // 修改库存商品
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        Product product = productService.findOne(id);
        prepareInput(model);
        model.addAttribute("product", product);
        return formPage;
    }

    // 保存修改信息
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute("product") @Valid Product product, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            prepareInput(model);
            return formPage;
        }
        productService.save(product);
        return "redirect:/settings/product";
    }


    @RequestMapping(value = "/{id}/organise", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Long id, Long parentId, String point) {
        Product product = productService.findOne(id);
        if ("append".equals(point)) {
            Product parent = productService.findOne(parentId);
            product.setParent(parent);
        } else {
            product.setParent(null);
        }
        try {
            productService.save(product);
            return printMessage(0);
        } catch (Exception e) {
            return printMessage(1, e.getMessage());
        }
    }

    // 删除商品库存
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, ModelMap model) {
        productService.delete(id);
        return "redirect:/settings/product";
    }

    @RequestMapping(value = "/valid-code", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity validCode(String oldCode, String code) {
        if (StringUtils.equals(oldCode, code)) {
            return new ResponseEntity(HttpStatus.OK);
        }

        if (productService.exists(code)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/ztree")
    @ResponseBody
    public List<?> ztree(Long id) {
        List<Product> products = null;
        if (id == null) {
            products = productService.findRootProducts();
        } else {
            Product product = productService.findOne(id);
            if(product != null){
                products = new ArrayList<Product>(product.getChildren());
            }
        }
        List<Map<String, Object>> results = new LinkedList<Map<String, Object>>();
        for (Product product : products) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", product.getId());
            map.put("name", product.getText());
            map.put("isParent", CollectionUtils.isNotEmpty(product.getChildren()));
            results.add(map);
        }
        return results;
    }
    @RequestMapping(value = "/add-product")
    @ResponseBody
    public Product addProduct(@ModelAttribute("product")  Product product,Long customer) {
    		return  productService.saveCustomerProduct(product,customer);
    }
	@RequestMapping(value = "/getproduct", method = RequestMethod.GET)
	@ResponseBody
	public Product getProduct(@RequestParam(value = "id", defaultValue = "0") Long id) {
		Product product= productService.findOne(id);
		return product;
	}
}
