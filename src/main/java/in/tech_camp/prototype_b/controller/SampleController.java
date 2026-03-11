package in.tech_camp.prototype_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// TODO: デプロイ確認のサンプルクラス、確認が出来たら削除する
@Controller
public class SampleController {

  @GetMapping("/")
  public String sample() {
      return "sample";
  }
  
}
