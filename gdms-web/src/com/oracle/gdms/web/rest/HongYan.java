package com.oracle.gdms.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.oracle.gdms.entity.GoodsModel;
import com.oracle.gdms.entity.GoodsType;
import com.oracle.gdms.entity.ResponseEntity;
import com.oracle.gdms.service.GoodsService;
import com.oracle.gdms.util.Factory;

@Path("/hongyan")
public class HongYan {
	
	@Path("/sing")
	@GET
	public String sing() {
		System.out.print("sing");
		return "HELLO";
	}
	
	@Path("/sing/ci") //rest/hongyan/sing/ci?name=""
	@GET
	public String singOne(@QueryParam("name") String name) {
		System.out.println("歌词："+name);
		return "ok";
	}
	
	@Path("/push/one") 
	@POST
	public String push(@FormParam("name") String name, @FormParam("sex") String sex) {
		System.out.println("商品名称："+name);
		System.out.println("商品名称："+sex);
		return "form";
	}
	
	@Path("/push/json")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String pushJson(String jsonparam) {
		System.out.println(jsonparam);
		JSONObject j = JSONObject.parseObject(jsonparam);
		String name = j.getString("name");
		String sex = j.getString("sex");
		String age = j.getString("age");
		System.out.println("name:"+name);
		System.out.println("sex:"+sex);
		System.out.println("age:"+age);
		return "json";
	}
	
	@Path("/goods/update/type")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  //规定返回的为JSON对象
	public String updateGoodsType(String jsonparam) {
		JSONObject j = JSONObject.parseObject(jsonparam);
		String goodsid = j.getString("goodsid");
		String gtid = j.getString("gtid");
		System.out.println("要修改的商品ID=" + goodsid + "目标类别ID=" + gtid);
		
		GoodsService service = (GoodsService) Factory.getInstance().getObject("update.goods.type");
		int count = service.updataGoodsType(goodsid,gtid);
		if (count > 0) {
			j.put("code", 0);
			j.put("msg", "更新成功");
		} else {
			j.put("code", 1);
			j.put("msg", "更新商品失败");
		}
		
		return j.toJSONString();
	}
	
	
	
	@Path("/goods/push/bytype")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	@Consumes(MediaType.APPLICATION_JSON)  //规定返回的结果为JSON对象
	public List<GoodsModel> pushGoodsType(GoodsType type){
		List<GoodsModel> list = null;
		
		GoodsService service = (GoodsService) Factory.getInstance().getObject("goods.sevice.impl");
		list = service.findByType(type.getGtid());
		System.out.println("size="+list.size());
		return list;
	}
	
	
	@Path("/goods/push/one")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	@Consumes(MediaType.APPLICATION_JSON)  //规定返回的结果为JSON对象
	public ResponseEntity pushGoodsOne(String jsonparam) {
		
		
		ResponseEntity r = new ResponseEntity();
		
		try {
			//{"goods":{"goodsid": 42, "name":"矿泉水","price":"3.5"}}
			JSONObject j = JSONObject.parseObject(jsonparam);
			
			String gs = j.getString("goods");
			GoodsModel goods = JSONObject.parseObject(gs, GoodsModel.class);
			System.out.println("服务端收到了：");
			System.out.println("商品ID：" + goods.getGoodsid());
			System.out.println("商品名称：" + goods.getName());
			System.out.println("生产地址：" + goods.getArea());
			r.setCode(0);
			r.setMessage("推送成功");
		} catch (Exception e) {
			e.printStackTrace();
			r.setCode(1184);
			r.setMessage("推送失败,商品数据不合法：" + jsonparam);
		}
		
		return r;
		
	}
}
