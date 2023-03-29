package cn.maodun.controller;

import cn.maodun.service.EsService;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author DELL
 * @date 2023/3/29
 */
@RequestMapping("/es")
@RestController
@Controller
public class EsController {

    @Autowired
    private EsService esService;

    @GetMapping("/index")
    public CreateIndexResponse createIndex(String index) throws IOException {
         return esService.createIndex(index);
    }
}
