package com.yanjian.boot05web2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yanjian.boot05web2.bean.Address;
import com.yanjian.boot05web2.bean.User;
import com.yanjian.boot05web2.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AddressController {
    @Autowired
    AddressService addressService;
    @GetMapping("/address")
    public String getAll( Model model, HttpSession httpSession){
        User loginUser = (User) httpSession.getAttribute("loginUser");
        Integer uid = loginUser.getUid();
        List<Address> uid1 = addressService.list(new QueryWrapper<Address>().eq("uid", uid).eq("pri",0));
        Address one = addressService.getOne(new QueryWrapper<Address>().eq("uid", uid).eq("pri", 1));
        model.addAttribute("addresses",uid1);
        model.addAttribute("pri",one);
        return "address";
    }
  /*  @GetMapping("/address/add")
    public String add(@RequestParam("uid")String uid){
        return "adda";
    }*/
  @GetMapping("/adda")
  public String adda(){

      return "adda";
  }
    @PostMapping("/address/add")
    public String add(@RequestParam("uid") String uid,
                      @RequestParam("address")String address,
                      @RequestParam("name")String name,
                      @RequestParam("telphone")String telphone){
        System.out.println("add");

        Address address1 = new Address();
        address1.setAddress(address);
        address1.setTelphone(telphone);
        address1.setName(name);
        address1.setUid(Integer.valueOf(uid));
        addressService.save(address1);
        System.out.println("ok");
        return "redirect:/address";
    }
    //删除
    @GetMapping("/address/delete")
    public String delete(@RequestParam("aid") Integer aid){
      addressService.remove(new QueryWrapper<Address>().eq("aid",aid));
        return "redirect:/address";
    }
    @GetMapping("/upaddress")
    public String update(@RequestParam("aid") Integer aid,Model model){
        model.addAttribute("aid",aid);
        Address aid1 = addressService.getOne(new QueryWrapper<Address>().eq("aid", aid));
        model.addAttribute("address",aid1);
        return "upaddress";
    }
    @PostMapping("/address/update")
    public String delete1(Address address){
        System.out.println(address);
//        addressService.saveOrUpdate(address);
   addressService.update(new UpdateWrapper<Address>().set("address",address.getAddress()).set("name",address.getName())
                            .set("telphone",address.getTelphone()).eq("aid",address.getAid()));
        System.out.println("saveorupdate");
        return "redirect:/address";
    }
    @GetMapping("/address/pri")
    public String pri(@RequestParam("aid") Integer aid,Model model){
        Address pri = addressService.getOne(new QueryWrapper<Address>().eq("pri", 1));
        if(pri!=null){
            addressService.update(new UpdateWrapper<Address>().eq("aid",pri.getAid()).set("pri",0));
        }
        addressService.update(new UpdateWrapper<Address>().eq("aid",aid).set("pri",1));

        return "redirect:/address";
    }
}
