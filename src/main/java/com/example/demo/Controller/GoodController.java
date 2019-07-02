package com.example.demo.Controller;

import com.example.demo.model.CommonResult;
import com.example.demo.model.IGood;
import com.example.demo.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Date;

@RestController
@RequestMapping("/goods")
public class GoodController {

    @Resource
    GoodsService goodsService;

    @RequestMapping(value = "/getAllGoods", method = RequestMethod.GET)
    public CommonResult getAllGoods(@RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo ,@RequestParam(value = "pageSize" ,defaultValue = "10") Integer pageSize){
        return goodsService.getAllGoods(pageNo,pageSize);
    }

    @PostMapping(value = "/getGoodsByCondition",consumes = "application/x-www-form-urlencoded")
    public CommonResult getGoodsByCondition(@RequestParam(name = "id", required = false) Integer id, @RequestParam(name = "goodName", required = false) String goodName, @RequestParam(name = "goodLeft", required = false) Integer goodLeft, @RequestParam(name = "goodDesc", required = false) String goodDesc, @RequestParam(name = "createDate", required = false) Date createDate){
        IGood good = new IGood();
        good.setId(id);
        good.setGoodName(goodName);
        good.setGoodDesc(goodDesc);
        good.setGoodLeft(goodLeft);
        good.setCreateDate(createDate);

        return goodsService.getGoodsByCondition(good);
    }

    @RequestMapping(value = "/addNewGood", method = RequestMethod.POST ,consumes = "application/json")
    public  CommonResult addNewGood(@RequestBody IGood good){
        return  goodsService.addNewGoods(good);
    }

    @RequestMapping(value = "/delteGoodById",method = RequestMethod.GET)
    public CommonResult deleteGood(@RequestParam(name = "id", required = true) Integer goodId){
        return goodsService.deleteGoodById(goodId);
    }

    @RequestMapping(value = "/findGoodById/{goodid}",method = RequestMethod.GET)
    public CommonResult findGoodById(@PathVariable("goodid") Integer goodId){
        return goodsService.getGoodById(goodId);
    }

    @RequestMapping(value = "/updateGood", method = RequestMethod.POST ,consumes = "application/json")
    public  CommonResult updateGood(@RequestBody IGood good){
        return  goodsService.upDateGoodInfo(good);
    }

    @PostMapping("/uploadForm")
    public CommonResult handleFormData(HttpServletRequest request){
        return goodsService.handleFormData(request);
    }

}





