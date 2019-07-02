package com.example.demo.service;


import com.example.demo.dao.IGoodMapper;
import com.example.demo.model.CommonResult;
import com.example.demo.model.IGood;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {

    static  final Logger log = Logger.getLogger(GoodsService.class);

    static final  String filePath = "/Users/zhu/Documents/Java/2019/5.16 Spring Test/uploadImages";

    @Resource
    IGoodMapper goodMapper;


    public CommonResult getGoodsByCondition(IGood good){
        CommonResult result = new CommonResult();
        List<IGood> goods = goodMapper.selectGoodsByCondition(good);
        result.setStatusCode(0);
        result.setErrorMesg("");
        result.setResult(goods);

        return  result;
    }

    public CommonResult getAllGoods(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<IGood> goods = goodMapper.selectAllGoods();

        CommonResult result = new CommonResult();

        result.setStatusCode(0);
        result.setErrorMesg("");
        result.setResult(goods);

        log.info("total count is " + goods.size());

        return  result;
    }

    public CommonResult getGoodById(Integer goodId){
        IGood good = goodMapper.selectByPrimaryKey(goodId);

        CommonResult result = new CommonResult();

        if (good != null){
            result.setStatusCode(0);
            result.setErrorMesg("find a good");
            result.setResult(good);
        }else {
            result.setStatusCode(-1);
            result.setErrorMesg("not records");
            result.setResult(null);
        }


        return  result;
    }


    public CommonResult addNewGoods(IGood good){
        int res = goodMapper.insertSelective(good);
        CommonResult result = new CommonResult();
        if (res == 1){

            result.setStatusCode(0);
            result.setErrorMesg("");
            result.setResult(good);
        }else {
            result.setStatusCode(-1);
            result.setErrorMesg("add new record fail");
            result.setResult(null);
        }

        return result;
    }


    public CommonResult deleteGoodById(Integer goodId){
        int res = goodMapper.deleteByPrimaryKey(goodId);
        CommonResult result = new CommonResult();
        if (res == 1){
            result.setStatusCode(0);
            result.setErrorMesg("delte sucess");
        }else {
            result.setStatusCode(-1);
            result.setErrorMesg("not found the record");
            result.setResult(null);
        }

        return result;
    }

    public CommonResult upDateGoodInfo(IGood good){
        CommonResult result = new CommonResult();
        if (good.getId() == null){
            result.setStatusCode(-1);
            result.setErrorMesg("id can not be null");
            result.setResult(null);
            return result;
        }

        int res = goodMapper.updateByPrimaryKeySelective(good);

        if (res == 1){
            result.setStatusCode(0);
            result.setErrorMesg("update success ");
        }else {
            result.setStatusCode(-1);
            result.setErrorMesg("not found the record");
            result.setResult(null);
        }

        return result;
    }


    public CommonResult handleFormData(HttpServletRequest request){
        CommonResult result = new CommonResult();

        File dir = new File(filePath + File.separator + "temp.log");
        File fileParent = dir.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        MultipartHttpServletRequest multipartHttpServletRequest = ((MultipartHttpServletRequest)request);

//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();

        IGood good = new IGood();
        String goodName = multipartHttpServletRequest.getParameter("goodName");
        if (!StringUtils.isEmpty(goodName)){
            good.setGoodName(goodName);
        }

        String goodDesc = multipartHttpServletRequest.getParameter("goodDesc");
        if (!StringUtils.isEmpty(goodDesc)){
            good.setGoodDesc(goodDesc);
        }

        String goodLeft = multipartHttpServletRequest.getParameter("goodLeft");
        if (!StringUtils.isEmpty(goodLeft)){
            good.setGoodLeft(Integer.parseInt(goodLeft));
        }

        good.setCreateDate(new Date());

        List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
        int success = 0;
        if (files != null){
            for (MultipartFile file : files) {
                if (file.isEmpty()){
                    success = -1;
                    break;
                }

                Date date = new Date();

                File newFile = new File( filePath + File.separator +  date.getTime() + file.getOriginalFilename());
                try {

                    file.transferTo(newFile);
                }catch (IOException e){
                    success = -1;
                    log.error("trans file error!" + file.getOriginalFilename());
                }

            }
        }else {
            success = -1;
        }

        if (success  == -1){
            result.setStatusCode(-1);
            result.setErrorMesg("file is empty");
        }else {
            result = addNewGoods(good);
        }

        return result;
    }
}
